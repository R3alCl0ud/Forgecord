package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.entity.Message;
import io.discloader.discloader.entity.channels.TextChannel;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.gateway.json.MessageJSON;
import io.discloader.discloader.util.Constants;

/**
 * @author Perry Berman
 *
 */
public class MessageDelete extends DiscPacket {

	/**
	 * 
	 */
	public MessageDelete(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		MessageJSON data = this.gson.fromJson(this.gson.toJson(packet.d), MessageJSON.class);
		TextChannel channel = this.socket.loader.textChannels.get(data.channel_id);
		if (channel == null)
			channel = this.socket.loader.privateChannels.get(data.channel_id);
		this.socket.loader.emit(Constants.Events.MESSAGE_DELETE, new Message(channel, data));
	}

}
