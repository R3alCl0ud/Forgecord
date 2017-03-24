package io.discloader.discloader.entity.channels.impl;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.RichEmbed;
import io.discloader.discloader.entity.channels.ITextChannel;
import io.discloader.discloader.entity.message.Message;
import io.discloader.discloader.entity.message.MessageFetchOptions;
import io.discloader.discloader.entity.sendable.Attachment;
import io.discloader.discloader.entity.user.User;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.rest.actions.BulkDelete;
import io.discloader.discloader.network.rest.actions.FetchMessage;
import io.discloader.discloader.network.rest.actions.FetchMessages;
import io.discloader.discloader.network.rest.actions.PinMessage;
import io.discloader.discloader.network.rest.actions.PinnedMessages;
import io.discloader.discloader.network.rest.actions.StartTyping;
import io.discloader.discloader.network.rest.actions.UnpinMessage;
import io.discloader.discloader.util.DLUtil.ChannelType;

/**
 * Represents a DM Channel on discord
 * 
 * @author Perry Berman
 */
public class PrivateChannel extends Channel implements ITextChannel {

	private HashMap<String, Message> messages;

	private HashMap<String, User> typing;

	/**
	 * The user that this DM Channel was opened with
	 * 
	 * @author Perry Berman
	 */
	public User recipient;

	public PrivateChannel(DiscLoader loader, ChannelJSON data) {
		super(loader, data);
		this.type = ChannelType.DM;
		this.messages = new HashMap<>();
		typing = new HashMap<>();
	}
	
	public CompletableFuture<PrivateChannel> close() {
		return null;
	}

	@Override
	public CompletableFuture<HashMap<String, Message>> deleteMessages(HashMap<String, Message> messages) {
		return new BulkDelete(this, messages).execute();
	}

	@Override
	public CompletableFuture<HashMap<String, Message>> deleteMessages(Message... messages) {
		HashMap<String, Message> msgs = new HashMap<>();
		for (Message message : messages) {
			msgs.put(message.id, message);
		}
		return deleteMessages(msgs);
	}

	@Override
	public CompletableFuture<Message> fetchMessage(String id) {
		return new FetchMessage(this, id).execute();
	}

	@Override
	public CompletableFuture<HashMap<String, Message>> fetchMessages() {
		return fetchMessages(new MessageFetchOptions());
	}

	@Override
	public CompletableFuture<HashMap<String, Message>> fetchMessages(MessageFetchOptions options) {
		return new FetchMessages(this, options).execute();
	}

	@Override
	public Message getMessage(String id) {
		return this.messages.get(id);
	}

	@Override
	public HashMap<String, Message> getMessages() {
		return this.messages;
	}

	@Override
	public CompletableFuture<HashMap<String, Message>> fetchPinnedMessages() {
		return new PinnedMessages(this).execute();
	}

	@Override
	public HashMap<String, Message> getPinnedMessages() {
		HashMap<String, Message> pins = new HashMap<>();
		for (Message message : messages.values()) {
			if (message.isPinned()) pins.put(message.id, message);
		}
		return pins;
	}

	@Override
	public HashMap<String, User> getTyping() {
		return typing;
	}

	@Override
	public boolean isTyping(User user) {
		return typing.containsKey(user.id);
	}

	@Override
	public CompletableFuture<Message> pinMessage(Message message) {
		return new PinMessage(message).execute();
	}

	public CompletableFuture<Message> sendEmbed(RichEmbed embed) {
		File file = null;
		Attachment attachment = null;
		if (embed.thumbnail != null && embed.thumbnail.file != null) {
			file = embed.thumbnail.file;
			embed.thumbnail.file = null;
			attachment = new Attachment(file.getName());
		}
		return this.loader.rest.sendMessage(this, null, embed, attachment, file);
	}

	public CompletableFuture<Message> sendMessage(String content) {
		return this.loader.rest.sendMessage(this, content, null, null, null);
	}

	public CompletableFuture<Message> sendMessage(String content, RichEmbed embed) {
		File file = null;
		Attachment attachment = null;
		if (embed.thumbnail != null && embed.thumbnail.file != null) {
			file = embed.thumbnail.file;
			embed.thumbnail.file = null;
			attachment = new Attachment(file.getName());
		}
		return this.loader.rest.sendMessage(this, content, embed, attachment, file);
	}

	public void setup(ChannelJSON data) {
		super.setup(data);
		if (data.recipients[0] != null) {
			recipient = loader.users.get(data.recipients[0].id);
			if (recipient == null) {
				recipient = loader.addUser(data.recipients[0]);
			}
		}
	}

	@Override
	public CompletableFuture<HashMap<String, User>> startTyping() {
		return new StartTyping(this).execute();
	}

	@Override
	public CompletableFuture<Message> unpinMessage(Message message) {
		return new UnpinMessage(message).execute();
	}

}