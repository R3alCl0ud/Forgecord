/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.client.command.CommandHandler;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.IEventAdapter;
import io.discloader.discloader.common.event.MessageCreateEvent;
import io.discloader.discloader.entity.Message;
import io.discloader.discloader.entity.channels.TextChannel;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.MessageJSON;
import io.discloader.discloader.util.Constants;

/**
 * @author Perry Berman
 *
 */
public class MessageCreate extends DiscPacket {

	/**
	 * @param socket
	 */
	public MessageCreate(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		MessageJSON data = this.gson.fromJson(gson.toJson(packet.d), MessageJSON.class);
		TextChannel channel = this.socket.loader.textChannels.get(data.channel_id);
		if (channel == null)
			channel = this.socket.loader.privateChannels.get(data.channel_id);
		Message message = new Message(channel, data);
		channel.messages.put(message.id, message);
		MessageCreateEvent event = new MessageCreateEvent(message);
		if (channel.type.equals("text")) {
			this.socket.loader.emit(Constants.Events.MESSAGE_CREATE, event);
			for (IEventAdapter e : DiscLoader.handlers.values()) {
				e.MessageCreate(event);
			}
		} else {
			this.loader.emit(Constants.Events.PRIVATE_MESSAGE_CREATE, event);
			for (IEventAdapter e : DiscLoader.handlers.values()) {
				e.PrivateMessageCreate(event);
			}
		}
		CommandHandler.handleMessageCreate(event);

	}

}
