package io.disc.discloader;

import io.disc.discloader.common.ObjectHandler;
import io.disc.discloader.events.DiscPreInitEvent;
import io.disc.discloader.objects.annotations.EventHandler;
import io.disc.discloader.objects.annotations.Mod;
import io.disc.discloader.objects.annotations.Mod.Instance;

/**
 * @author Perry Berman
 *
 */
@Mod(desc = HelpMod.DESC, modid = HelpMod.MODID, name = HelpMod.NAME, version = HelpMod.VERSION)
public class HelpMod {

	public static final String DESC = "Help Mod";
	public static final String MODID = "helpmod";
	public static final String NAME = "HelpMod";
	public static final String VERSION = "0.0.0";

	@Instance(MODID)
	public static HelpMod instance;
	
	public HelpMod() {
		
	}
	
	@EventHandler
	public void preInit(DiscPreInitEvent e) {
		ObjectHandler.register();
	}

	@EventHandler
	public void raw(String raw) {
		System.out.println(raw);
	}

}