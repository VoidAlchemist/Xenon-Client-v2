package com.xenon.modules.api;

import com.xenon.XenonClient;
import com.xenon.util.readability.Hook;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.DiscordUser;
import net.arikia.dev.drpc.callbacks.ReadyCallback;
/**
 * 
 * @author VoidAlchemist
 *
 */
public class DiscordRPAPI {
	
	public boolean running = true, ingame = false;
	private long created = 0;
	
	public void start()
	{
		created = System.currentTimeMillis();
		
		DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(new ReadyCallback() {

			@Override
			public void apply(DiscordUser user) {
				
				XenonClient.instance.printer.info("Hello, "+user.username+"#"+user.discriminator);
				update("Starting the engine...", "color >> 16 & 255");
			}
			
		}).build();
		
		DiscordRPC.discordInitialize("889045723750998057", handlers, true);
		
		new Thread("Discord RP Callback")
		{
			@Override
			public void run()
			{
				while(running)
				{
					DiscordRPC.discordRunCallbacks();
				}
			}
			
		}.start();
	}
	
	public void shutdown()
	{
		running = false;
		DiscordRPC.discordShutdown();
	}
	
	public void update(String line1, String line2)
	{
		DiscordRichPresence.Builder builder = new DiscordRichPresence.Builder(line2);
		builder.setBigImage("large", "");
		builder.setDetails(line1);
		builder.setStartTimestamps(created);
		
		DiscordRPC.discordUpdatePresence(builder.build());
	}
	
	/**
	 * Updates Discord Rich Presence with the message corresponding to the GuiMainMenu.
	 */
	@Hook("com.xenon.gui.GuiMainMenuXenon -> line 64")
	public void mainmenuHook() {
		ingame = false;
		if (running)
		update("Idle", "v2.0 now released!");
	}
	
	/**
	 * Updates Discord Rich Presence with the message corresponding to playing solo.
	 */
	@Hook("net.minecraft.client.Minecraft#launchIntegratedServer() -> line 2359")
	public void entersoloHook() {
		ingame = true;
		if (running)
			update("Wither Impact-ing trees", "A tree swing story");
	}
	
	/**
	 * Updates Discord Rich Presence with the message corresponding to playing on a server.
	 * @param ip
	 */
	@Hook("net.minecraft.client.multiplayer.GuiConnecting#connect(String, String) -> line 52")
	public void enterserverHook(String ip) {
		ingame = true;
		if (running) {
			String s = null;
			if (ip.contains("hypixel"))
				s = "Hypickle-ing a.k.a. lagpixel";
			else if (ip.contains("brwserv"))
				s = "W-S-A-D tapping, 98% aim accuracy";
			else if (ip.contains("blocksmc"))
				s = "Lots of hackers, huh...";
			
			update(s, "Sent from the Silken Courtyard.");
		}
	}

}
