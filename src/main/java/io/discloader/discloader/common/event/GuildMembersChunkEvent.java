package io.discloader.discloader.common.event;

import java.util.HashMap;

import io.discloader.discloader.entity.Guild;
import io.discloader.discloader.entity.GuildMember;

public class GuildMembersChunkEvent extends DLEvent {

	public final Guild guild;

	public final HashMap<String, GuildMember> members;

	public GuildMembersChunkEvent(Guild guild, HashMap<String, GuildMember> members) {
		super(guild.loader);

		this.guild = guild;

		this.members = members;
	}

}
