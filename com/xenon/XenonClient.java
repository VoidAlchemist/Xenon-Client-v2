package com.xenon;

import com.xenon.client.gui.GuiDraggable;
import com.xenon.client.gui.SplashScreen;
import com.xenon.modules.ModSettings;
import com.xenon.modules.api.CPSCounter;
import com.xenon.modules.api.DiscordRPAPI;
import com.xenon.modules.api.GlintEnchantAPI;
import com.xenon.util.FileManager;
import com.xenon.util.PRNG;
import com.xenon.util.Printer;
import com.xenon.util.RenderUtils;
import com.xenon.util.readability.Hook;
import com.xenon.util.readability.Singleton;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

/**
 * XenonClient v2.0 now released.<br>
 * The source code now contains a Hook annotation which indicates the target where we call the function.<br>
 * This is mainly a replacement for an event system.<br><br>
 * 
 * Silent Patches (the user can't disable them) : <br>
 * 
 * <br>
 * In {@link net.minecraft.client.entity.EntityPlayerSP}, overrode getLook, added the method line 139:
 * <br><br><code>
 *  public Vec3 getLook(float partialTicks) {<br>
 *     return this.getVectorForRotation(this.rotationPitch, this.rotationYaw);<br>
 * }<br></code><br>
 * 
 * In {@link net.minecraft.client.gui.GuiMainMenu}, removed 
 * <br><code>
 * private static final AtomicInteger field_175373_f = new AtomicInteger(0);
 * </code><br>
 * which isn't used anywhere (in the entire project).
 * <br><br>
 * Replace every single occurrence of {@link java.util.Random} with {@link com.xenon.util.PRNG}, in order for MC to no longer use a thread-safe version,<br>
 * which may impact the performance greatly on singleplayer (multi-threading).
 * Note : {@link java.util.concurrent.ThreadLocalRandom} is great and all but you cannot set the seed.
 * <br><br>
 * 
 * Chat messages now have their own rectangle background width, allowing for less hindrance HUD-wise :<br>
 * In {@link net.minecraft.client.gui.GuiNewChat#drawChat(int)}, line 80 is now <br><code>
 * drawRect(0, j2 - 9, mc.fontRendererObj.getStringWidth(s) + 4, j2, l1 / 2 << 24);</code>
 * <br><br>
 * 
 * 
 * 
 * @see com.xenon.client.patches
 * @author VoidAlchemist
 * @version 2.0
 */
@Singleton
public final class XenonClient {

	public DiscordRPAPI discordRP = new DiscordRPAPI();
	public CPSCounter cpsCounter = new CPSCounter();
	public boolean shouldToggleSprint = true;
	public final Printer printer = new Printer();
	public ModSettings settings;
	public SplashScreen splashScreen;
	public Minecraft minecraft;
	public PRNG random = new PRNG();
	
	
	public static final XenonClient instance = new XenonClient();
	private XenonClient() {}
	
	@Hook("net.minecraft.client.main.Main#main -> line 117")
	public void preInit() {
		printer.info("initializing client without GL11 context...");
		FileManager.init();
		settings = FileManager.instanciateModSettings();
		if (settings.discordRP)
			discordRP.start();
	}
	
	@Hook("net.minecraft.client.Minecraft#startGame() -> line 504")
	public void init() {
		printer.info("initializing client with GL11 context...");
		RenderUtils.init();
		splashScreen = new SplashScreen();
		splashScreen.setProgress("XenonClient | Finishing initialization");
	}	
	
	@Hook("net.minecraft.client.Minecraft#startGame() -> line 583")
	public void postInit() {
		printer.info("Finished initializing client");
		splashScreen = null;	//discard the splashScreen object.
		minecraft = Minecraft.getMinecraft();
	}
	
	@Hook("net.minecraft.client.Minecraft#shutdownMinecraftApplet() -> line 1058")
	public void shutdown() {
		printer.info("shuting down client...");
		FileManager.writeToJson(FileManager.main_conf, settings);
		
		if (discordRP.running)
			discordRP.shutdown();
	}
	
	@Hook("net.minecraft.client.Minecraft#onTick() -> line 2277")
	public void tick() {
		
		if (settings.glintChroma)
			GlintEnchantAPI.call();
		
		if (minecraft.gameSettings.xenonBindModules.isKeyDown())
			minecraft.displayGuiScreen(new GuiDraggable());
	}
	
	@Hook("net.minecraft.world.World#joinEntityInSurroundings(Entity) -> line 3459"
			+ "#spawnEntityInWorld(Entity) -> line 1172"
			+ "#loadEntities(Collection) -> line 3145")
	public void onJoinWorld(Entity entity, boolean remote) {
	}
	
}