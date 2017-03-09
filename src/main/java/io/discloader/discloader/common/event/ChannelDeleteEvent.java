package io.discloader.discloader.common.event;

import io.discloader.discloader.entity.channels.Channel;

/**
 * @author Perry Berman
 *
 */
public class ChannelDeleteEvent extends DLEvent {

	/**
	 * The channel that was deleted
	 */
	public final Channel channel;

	public ChannelDeleteEvent(Channel channel) {
		super(channel.loader);

		this.channel = channel;
	}

}