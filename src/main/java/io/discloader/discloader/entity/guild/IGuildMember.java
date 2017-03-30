package io.discloader.discloader.entity.guild;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.IPermission;
import io.discloader.discloader.entity.IPresence;
import io.discloader.discloader.entity.ISnowflake;
import io.discloader.discloader.entity.channel.IGuildVoiceChannel;
import io.discloader.discloader.entity.channel.IVoiceChannel;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.voice.VoiceState;

/**
 * @author Perry Berman
 */
public interface IGuildMember extends ISnowflake {

	CompletableFuture<IGuildMember> ban();

	CompletableFuture<IGuildMember> deafen();

	IGuild getGuild();

	DiscLoader getLoader();

	String getNickname();

	String asMention();

	IPermission getPermissions();

	IPresence getPresence();

	Map<String, IRole> getRoles();

	IUser getUser();

	CompletableFuture<IGuildMember> setNick(String nick);

	IVoiceChannel getVoiceChannel();

	VoiceState getVoiceState();

	CompletableFuture<IGuildMember> giveRole(IRole... roles);

	boolean isDeaf();

	boolean isMuted();

	CompletableFuture<IGuildMember> kick();

	CompletableFuture<IGuildMember> mute();

	CompletableFuture<IGuildMember> takeRole(IRole role);

	CompletableFuture<IGuildMember> unDeafen();

	CompletableFuture<IGuildMember> unMute();
	
	/**
	 * @return
	 */
	IRole getHighestRole();
	
	/**
	 * @param channel
	 * @return
	 */
	CompletableFuture<IGuildMember> move(IGuildVoiceChannel channel);
}
