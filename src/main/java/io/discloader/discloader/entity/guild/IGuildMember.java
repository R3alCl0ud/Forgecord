package io.discloader.discloader.entity.guild;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.exceptions.PermissionsException;
import io.discloader.discloader.entity.IMentionable;
import io.discloader.discloader.entity.IPermission;
import io.discloader.discloader.entity.channel.IGuildVoiceChannel;
import io.discloader.discloader.entity.presence.IPresence;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.ISnowflake;
import io.discloader.discloader.entity.util.Permissions;
import io.discloader.discloader.entity.voice.VoiceState;

/**
 * @author Perry Berman
 */
public interface IGuildMember extends ISnowflake, IMentionable {
	/**
	 * @return A CompletableFuture that completes with the new {@link IGuildMember}
	 *         object if successful.
	 */
	CompletableFuture<IGuildMember> ban();

	/**
	 * @return A CompletableFuture that completes with the new {@link IGuildMember}
	 *         object if successful.
	 */
	CompletableFuture<IGuildMember> ban(String reason);

	/**
	 * @return A CompletableFuture that completes with the new {@link IGuildMember}
	 *         object if successful.
	 */
	CompletableFuture<IGuildMember> deafen();

	/**
	 * @return A CompletableFuture that completes with the new {@link IGuildMember}
	 *         object if successful.
	 */
	CompletableFuture<IGuildMember> deafen(String reason);

	/**
	 * Returns the {@link IGuild} the {@link IGuildMember} object belongs to.
	 * 
	 * @return The {@link IGuild} the {@link IGuildMember} object belongs to.
	 */
	IGuild getGuild();

	/**
	 * Determines the member's highest role in the {@link #getGuild guild}'s role
	 * hiarchy and then returns it. If the member doesn't have any roles, then
	 * {@code null} is returned
	 * 
	 * @return The member's highest role if they have any roles, {@code null}
	 *         otherwise.
	 */
	IRole getHighestRole();

	/**
	 * Returns an {@link OffsetDateTime} of the moment the member joined the
	 * {@link #getGuild() guild}.
	 * 
	 * @return An {@link OffsetDateTime} of the moment the member joined the
	 *         {@link #getGuild() guild}.
	 */
	OffsetDateTime getJoinTime();

	DiscLoader getLoader();

	/**
	 * Returns the member's nickname if they have one, {@code null} otherwise.
	 * 
	 * @return The member's nickname if they have one, {@code null} otherwise.
	 */
	String getNickname();

	/**
	 * Returns the member's nickname if they have one, {@link IUser#getUsername()}
	 * otherwise.
	 * 
	 * @return The member's nickname if they have one, {@link IUser#getUsername()}
	 *         otherwise.
	 */
	String getName();

	IPermission getPermissions();

	IPresence getPresence();

	/**
	 * Returns a HashMap of roles that the member belongs to.
	 * 
	 * @return A List of the {@link IGuildMember member's} {@link IRole roles}
	 *         sorted by {@link IRole#getPosition()} in order of lowest to highest.
	 */
	List<IRole> getRoles();

	IUser getUser();

	IGuildVoiceChannel getVoiceChannel();

	VoiceState getVoiceState();

	/**
	 * @return A CompletableFuture that completes with the new {@link IGuildMember}
	 *         object if successful.
	 */
	CompletableFuture<IGuildMember> giveRole(IRole... roles);

	CompletableFuture<IGuildMember> giveRole(String reason, IRole... roles);

	boolean hasRole(IRole role);

	boolean hasRole(long roleID);

	boolean hasRole(String roleID);

	/**
	 * Sets the member's role ID array to the array contained in a
	 * PresenceUpdateEvent for the member.
	 * 
	 * @param roleIDs
	 *            The member's roles' IDs.
	 */
	void setRoles(String[] roleIDs);

	CompletableFuture<Boolean> isBanned();

	boolean isDeafened();

	boolean isMuted();

	boolean isOwner();

	/**
	 * @return A CompletableFuture that completes with the new {@link IGuildMember}
	 *         object if successful.
	 */
	CompletableFuture<IGuildMember> kick();

	/**
	 * @return A CompletableFuture that completes with the new {@link IGuildMember}
	 *         object if successful.
	 */
	CompletableFuture<IGuildMember> kick(String reason);

	/**
	 * Moves the guild member into a different voice channel. <br>
	 * The {@link Permissions#MOVE_MEMBERS MOVE_MEMBERS} is required to move
	 * members.
	 * 
	 * @param channel
	 *            The {@link IGuildVoiceChannel} to move the member into.
	 * @return A CompletableFuture that completes with the new {@link IGuildMember}
	 *         object if successful.
	 * @throws PermissionsExecption
	 *             Thrown if the client doesn't have permission to move this member
	 */
	CompletableFuture<IGuildMember> move(IGuildVoiceChannel channel);

	/**
	 * Moves the guild member into a different voice channel. <br>
	 * The {@link Permissions#MOVE_MEMBERS MOVE_MEMBERS} is required to move
	 * members.
	 * 
	 * @param channel
	 *            The {@link IGuildVoiceChannel} to move the member into.
	 * @param reason
	 *            The reason for moving the member.
	 * @return A CompletableFuture that completes with the new {@link IGuildMember}
	 *         object if successful.
	 * @throws PermissionsExecption
	 *             Thrown if the client doesn't have permission to move this member
	 */
	CompletableFuture<IGuildMember> move(IGuildVoiceChannel channel, String reason);

	/**
	 * 
	 * Mutes the guild member<br>
	 * Requires the {@link Permissions#MUTE_MEMBERS} permission
	 * 
	 * @return A CompletableFuture that completes with the new {@link IGuildMember}
	 *         object if successful.
	 */
	CompletableFuture<IGuildMember> mute();

	/**
	 * Mutes the guild member<br>
	 * Requires the {@link Permissions#MUTE_MEMBERS} permission
	 * 
	 * @param reason
	 *            The reason for muting the member
	 * @return A CompletableFuture that completes with the new {@link IGuildMember}
	 *         object if successful.
	 */
	CompletableFuture<IGuildMember> mute(String reason);

	/**
	 * @return A CompletableFuture that completes with the new {@link IGuildMember}
	 *         object if successful.
	 */
	CompletableFuture<IGuildMember> setNick(String nick);

	/**
	 * @return A CompletableFuture that completes with the new {@link IGuildMember}
	 *         object if successful.
	 */
	CompletableFuture<IGuildMember> setNick(String nick, String reason);

	/**
	 * Takes a role away from a member
	 * 
	 * @param roles
	 *            The role(s) to take away from the member
	 * @return A Future that completes with the member if successful.
	 * @throws PermissionsException
	 *             thrown if a role with a higher position than the current user's
	 *             highest role is attempted to be given to the member. Also thrown
	 *             if the current user doesn't have the MANAGE_ROLE permission.
	 */
	CompletableFuture<IGuildMember> takeRole(IRole... roles);

	/**
	 * Takes a role away from a member
	 * 
	 * @param roles
	 *            The role(s) to take away from the member
	 * @return A Future that completes with the member if successful.
	 * @throws PermissionsException
	 *             thrown if a role with a higher position than the current user's
	 *             highest role is attempted to be given to the member. Also thrown
	 *             if the current user doesn't have the MANAGE_ROLE permission.
	 */
	CompletableFuture<IGuildMember> takeRole(String reason, IRole... roles);

	/**
	 * @return A CompletableFuture that completes with the new {@link IGuildMember}
	 *         object if successful.
	 */
	CompletableFuture<IGuildMember> unDeafen();

	/**
	 * @return A CompletableFuture that completes with the new {@link IGuildMember}
	 *         object if successful.
	 */
	CompletableFuture<IGuildMember> unDeafen(String reason);

	/**
	 * @return A CompletableFuture that completes with the new {@link IGuildMember}
	 *         object if successful.
	 */
	CompletableFuture<IGuildMember> unMute();

	/**
	 * @return A CompletableFuture that completes with the new {@link IGuildMember}
	 *         object if successful.
	 */
	CompletableFuture<IGuildMember> unMute(String reason);
}
