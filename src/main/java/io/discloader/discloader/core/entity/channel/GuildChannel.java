package io.discloader.discloader.core.entity.channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;

import io.discloader.discloader.common.registry.EntityBuilder;
import io.discloader.discloader.core.entity.Overwrite;
import io.discloader.discloader.core.entity.Permission;
import io.discloader.discloader.core.entity.guild.Guild;
import io.discloader.discloader.core.entity.guild.Role;
import io.discloader.discloader.entity.IOverwrite;
import io.discloader.discloader.entity.IPermission;
import io.discloader.discloader.entity.channel.IChannel;
import io.discloader.discloader.entity.channel.IChannelCategory;
import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.guild.IRole;
import io.discloader.discloader.entity.invite.IInvite;
import io.discloader.discloader.entity.util.Permissions;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.rest.RESTOptions;
import io.discloader.discloader.network.rest.actions.channel.FetchInvites;
import io.discloader.discloader.network.rest.actions.channel.SetOverwrite;
import io.discloader.discloader.network.rest.actions.channel.close.CloseGuildChannel;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

/**
 * Represents any channel in a guild
 * 
 * @author Perry Berman
 */
public class GuildChannel extends Channel implements IGuildChannel {

	/**
	 * The {@link Guild} the channel belongs to. <br>
	 * This property <u>must</u> be {@code null} if the {@link #type} of the
	 * channel is {@code "dm"}, or {@code "groupDM"}.
	 * 
	 * @author Perry Berman
	 * @since 0.0.1
	 */
	protected final IGuild guild;

	/**
	 * The channel's name
	 */
	protected String name;

	/**
	 * The channel's position
	 */
	public int position;

	/**
	 * A {@link HashMap} of the channel's {@link Overwrite overwrites}. Indexed
	 * by {@link Overwrite#id}.
	 * 
	 * @author Perry Berman
	 * @since 0.0.1
	 */
	private Map<Long, IOverwrite> overwrites;

	private boolean nsfw;

	private long parentID;

	public GuildChannel(IGuild guild, ChannelJSON channel) {
		super(guild.getLoader(), channel);

		this.guild = guild;

		overwrites = new HashMap<>();
	}

	// @Override
	// public CompletableFuture<IGuildChannel> clone() {
	// return guild.createTextChannel(name);
	// }

	@Override
	public CompletableFuture<IGuildChannel> delete() {
		return new CloseGuildChannel(this).execute();
	}

	@Override
	public CompletableFuture<? extends IGuildChannel> edit(int position, boolean nsfw) {
		return null;
	}

	// /**
	// * Changes the channels settings
	// *
	// * @param name The new name for the channel
	// * @param topic The new topic for the channel
	// * @param position The new position for the channel
	// * @param bitrate The new bitrate
	// * @param userLimit The new userLimit
	// * @return A Future that completes with an IGuildChannel if successful
	// */
	//// @Override
	// public CompletableFuture<IGuildChannel> edit(String name, String topic,
	// int position, int bitrate, int userLimit) {
	// CompletableFuture<IGuildChannel> future = new CompletableFuture<>();
	// loader.rest.request(Methods.PATCH, Endpoints.channel(getID()), new
	// RESTOptions, cls)
	//// loader.rest.modifyGuildChannel(this, name, topic, position, bitrate,
	// userLimit).thenAcceptAsync(channel -> {
	//// future.complete(channel);
	//// });
	// return future;
	// }

	@Override
	public CompletableFuture<? extends IGuildChannel> edit(String name, boolean nsfw) {
		return edit(name, position, nsfw);
	}

	@Override
	public CompletableFuture<? extends IGuildChannel> edit(String name, int position) {
		return edit(name, position, nsfw);
	}

	@Override
	public CompletableFuture<? extends IGuildChannel> edit(String name, int position, boolean nsfw) {
		CompletableFuture<IGuildChannel> future = new CompletableFuture<>();
		JSONObject settings = new JSONObject().put("name", name).put("position", position).put("nsfw", nsfw);
		loader.rest.request(Methods.PATCH, Endpoints.channel(getID()), new RESTOptions(settings), ChannelJSON.class).thenAcceptAsync(data -> {
			IChannel newChannel = EntityBuilder.getChannelFactory().buildChannel(data, guild, false);
			if (newChannel instanceof IGuildChannel) future.complete((IGuildChannel) newChannel);
		}).exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	@Override
	public IChannelCategory getCategory() {
		return guild.getChannelCategoryByID(parentID);
	}

	@Override
	public IGuild getGuild() {
		return guild;
	}

	@Override
	public CompletableFuture<List<IInvite>> getInvites() {
		return new FetchInvites(this).execute();
	}

	@Override
	public Map<Long, IGuildMember> getMembers() {
		Map<Long, IGuildMember> members = new HashMap<>();
		for (IGuildMember member : guild.getMembers().values()) {
			if (permissionsOf(member).hasPermission(Permissions.READ_MESSAGES, false)) members.put(member.getID(), member);
		}
		return members;
	}

	@Override
	public String getName() {
		return name;
	}

	public Map<Long, IOverwrite> getOverwrites() {
		return overwrites;
	}

	@Override
	public int getPosition() {
		return position;
	}

	@Override
	public boolean inCategory() {
		return getCategory() != null;
	}

	@Override
	public boolean inCategory(IChannelCategory category) {
		return inCategory() && getCategory().getID() == category.getID();
	}

	@Override
	public boolean isNSFW() {
		return nsfw;
	}

	@Override
	public boolean isPrivate() {
		return false;
	}

	@Override
	public IOverwrite overwriteOf(IRole role) {
		return overwrites.get(role.getID());
	}

	@Override
	public List<IOverwrite> overwritesOf(IGuildMember member) {
		List<IOverwrite> Overwrites = new ArrayList<>();
		for (IRole role : member.getRoles()) {
			if (role != null && overwrites.get(role.getID()) != null) Overwrites.add(overwrites.get(role.getID()));
		}
		if (overwrites.get(member.getID()) != null) Overwrites.add(overwrites.get(member.getID()));
		return Overwrites;
	}

	@Override
	public IPermission permissionsOf(IGuildMember member) {
		long raw = 0;
		if (guild.isOwner(member)) return new Permission(member, this, 2146958463);
		for (IRole role : member.getRoles()) {
			if (role != null) {
				raw |= role.getPermissions().toLong();
			}
		}
		for (IOverwrite overwrite : overwritesOf(member)) {
			raw |= overwrite.getAllowed();
			raw &= ~overwrite.getDenied();
		}
		return new Permission(member, this, raw);
	}

	@Override
	public CompletableFuture<? extends IGuildChannel> setName(String name) {
		return edit(name, position);
	}

	@Override
	public CompletableFuture<? extends IGuildChannel> setNSFW(boolean nswf) {
		return null;
	}

	@Override
	public CompletableFuture<IOverwrite> setOverwrite(IOverwrite overwrite) {
		return new SetOverwrite(this, overwrite).execute();
	}

	@Override
	public CompletableFuture<IOverwrite> setPermissions(int allow, int deny, IGuildMember member) {
		return new SetOverwrite(this, new Overwrite(allow, deny, member)).execute();
	}

	@Override
	public CompletableFuture<IOverwrite> setPermissions(int allow, int deny, Role role) {
		return new SetOverwrite(this, new Overwrite(allow, deny, role)).execute();
	}

	@Override
	public CompletableFuture<? extends IGuildChannel> setPosition(int position) {
		return edit(name, position);
	}

	@Override
	public void setup(ChannelJSON data) {
		super.setup(data);
		name = data.name;
		position = data.position;

	}

	@Override
	public CompletableFuture<IOverwrite> deleteOverwrite(IOverwrite overwrite) {
		return null;
	}

	@Override
	public CompletableFuture<List<IOverwrite>> deleteOverwrites(IOverwrite... overwrites) {
		return null;
	}

	@Override
	public CompletableFuture<List<IOverwrite>> setOverwrite(IOverwrite... overwrites) {
		return null;
	}

}
