package io.discloader.discloader.entity.impl;

import io.discloader.discloader.entity.channels.Channel;
import io.discloader.discloader.entity.message.Message;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.util.DLUtil.ChannelType;

public interface IChannel {

    /**
     * @return The Snowflake ID of the channel
     */
    String getID();

    /**
     * @return The type of the channel
     */
    ChannelType getType();

    /**
     * Whether or not the channel is a dm channel. Is always {@literal true} if {@link Channel#type type} is {@literal "groupDM"} or
     * {@literal "dm"}
     * 
     * @return true if the channel is a dm/groupDM channel, false otherwise
     * @author Perry Berman
     */
    boolean isPrivate();

    /**
     * Sets up the channel for use.
     * 
     * @param data Channel data.
     */
    void setup(ChannelJSON data);

    /**
     * @return A string that is in the correct format for mentioning this channel in a {@link Message}
     */
    String toMention();

}