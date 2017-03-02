package io.discloader.discloader.network.json;

/**
 * @author Perry Berman
 *
 */
public class GuildJSON {
	public String id;
	public String name;
	public String icon;
	public String splash;
	public String owner_id;
	public String embed_channel_id;
	public String[] features;
	public String region;

	public int verification_level;
	public int default_message_notifications;
	public int member_count;

	public boolean large;
	public boolean embed_enabled;
	public boolean unavailable;

	public MemberJSON[] members;
	public ChannelJSON[] channels;
	public RoleJSON[] roles;
	public PresenceJSON[] presences;
	public VoiceStateJSON[] voice_states;
	public EmojiJSON[] emojis;

}
