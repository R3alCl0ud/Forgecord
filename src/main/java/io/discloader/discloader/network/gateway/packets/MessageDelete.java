package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.message.GroupMessageDeleteEvent;
import io.discloader.discloader.common.event.message.GuildMessageDeleteEvent;
import io.discloader.discloader.common.event.message.MessageDeleteEvent;
import io.discloader.discloader.common.event.message.PrivateMessageDeleteEvent;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.channel.ChannelTypes;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.gateway.Gateway;
import io.discloader.discloader.network.json.MessageJSON;

/**
 * @author Perry Berman
 */
public class MessageDelete extends AbstractHandler {

	public MessageDelete(Gateway socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		MessageJSON data = this.gson.fromJson(this.gson.toJson(packet.d), MessageJSON.class);
		long channelID = SnowflakeUtil.parse(data.channel_id);
		ITextChannel channel = EntityRegistry.getTextChannelByID(channelID);
		if (channel == null)
			channel = EntityRegistry.getPrivateChannelByID(channelID);
		if (channel == null)
			channel = EntityRegistry.getGroupChannelByID(data.channel_id);
		if (channel == null)
			return;
		IMessage message = channel.getMessage(data.id);
		if (message == null)
			return;
		channel.getMessages().remove(message.getID());
		MessageDeleteEvent event = new MessageDeleteEvent(message);
		loader.emit(event);
		if (channel.getType() == ChannelTypes.DM) {
			loader.emit(new PrivateMessageDeleteEvent(message));
		} else if (channel.getType() == ChannelTypes.GROUP) {
			loader.emit(new GroupMessageDeleteEvent(message));
		}
		if (message.getGuild() != null) {
			loader.emit(new GuildMessageDeleteEvent(message));
		}
	}

}
