package io.discloader.discloader.client.command;

import java.io.File;

import io.discloader.discloader.client.render.texture.icon.CommandIcon;
import io.discloader.discloader.client.render.util.IIcon;
import io.discloader.discloader.common.event.MessageCreateEvent;
import io.discloader.discloader.common.registry.CommandRegistry;
import io.discloader.discloader.entity.sendable.RichEmbed;
import io.discloader.discloader.util.Constants;

/**
 * 
 * 
 * @author Perry Berman
 */
public class CommandHelp extends Command {

	public CommandHelp() {
		super();
		this.setTextureName("discloader:help").setDescription("Displays information about the available commands")
				.setUsage("help [<command>]");
	}

	public void execute(MessageCreateEvent e) {
		RichEmbed embed = new RichEmbed()
				.setFooter(String.format("type `%shelp <page>` to tab threw the pages", CommandHandler.prefix),
						e.loader.user.avatarURL)
				.setAuthor(e.loader.user.username, "http://discloader.io", e.loader.user.avatarURL).setColor(0x08a2ff);

		if (e.args.length == 2) {
			String cmd = e.args[1];
			Command command = CommandRegistry.commands.get(cmd);
			if (command != null) {
				File icon = Constants.MissingTexture;
				CommandIcon iicon = (CommandIcon) command.getIcon();
				
				if (iicon != null && iicon.getFile() != null) {
					icon = iicon.getFile();
				}
				
				embed.setTitle(command.getUnlocalizedName()).setThumbnail(icon)
						.addField("Description", command.getDescription(), true)
						.addField("Usage", command.getUsage(), true);
			}
		} else {

			embed.setThumbnail(this.getIcon().getFile());

			String commands = "";
			int size = CommandRegistry.commands.entries().size();
			Command[] cmds = CommandRegistry.commands.entries().toArray(new Command[size]);
			for (int i = 0; i < 10 && i < cmds.length; i++) {
				commands = String.format("%s**%s**: %s\n", commands, cmds[i].getUnlocalizedName(),
						cmds[i].getDescription());
			}
			embed.addField("Commands", commands, true);
			embed.setTitle(String.format("Help. Page: 1/%d", (size / 10) + size % 10 != 0 ? 1 : 0));
		}
		e.message.channel.sendEmbed(embed);
	}

}
