/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.guild.GuildBanRemoveEvent;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.network.gateway.Gateway;
import io.discloader.discloader.network.json.GuildMemberRemoveJSON;

/**
 * @author Perry Berman
 */
public class GuildBanRemove extends AbstractHandler {

	public GuildBanRemove(Gateway socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		String d = gson.toJson(packet.d);
		GuildMemberRemoveJSON data = gson.fromJson(d, GuildMemberRemoveJSON.class);
		IGuild guild = EntityRegistry.getGuildByID(data.guild_id);
		IUser user = EntityRegistry.addUser(data.user);
		GuildBanRemoveEvent event = new GuildBanRemoveEvent(guild, user);
		loader.emit(event);
	}

}
