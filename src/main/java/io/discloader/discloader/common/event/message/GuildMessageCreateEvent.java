package io.discloader.discloader.common.event.message;

import io.discloader.discloader.entity.channels.impl.TextChannel;
import io.discloader.discloader.entity.guild.Guild;
import io.discloader.discloader.entity.message.Message;

public class GuildMessageCreateEvent extends MessageCreateEvent {

	private Guild guild;

	public GuildMessageCreateEvent(Message message) {
		super(message);
		guild = message.guild;
	}

	/**
	 * @return the guild
	 */
	public Guild getGuild() {
		return guild;
	}

	@Override
	public TextChannel getChannel() {
		return (TextChannel) super.getChannel();
	}

}