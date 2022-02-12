package com.xenon.client.gui;

import static net.minecraft.client.renderer.GlStateManager.blendFunc;
import static net.minecraft.client.renderer.GlStateManager.disableBlend;
import static net.minecraft.client.renderer.GlStateManager.enableBlend;
import static net.minecraft.client.renderer.GlStateManager.popMatrix;
import static net.minecraft.client.renderer.GlStateManager.pushMatrix;
import static net.minecraft.client.renderer.GlStateManager.rotate;
import static net.minecraft.client.renderer.GlStateManager.translate;
import static net.minecraft.client.renderer.GlStateManager.tryBlendFuncSeparate;

import java.io.IOException;

import org.lwjgl.opengl.GLContext;

import com.xenon.XenonClient;
import com.xenon.client.gui.components.IconButton;
import com.xenon.client.gui.components.RoundedButton;
import com.xenon.util.RenderUtils;
import com.xenon.util.readability.Hook;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

/**
 * A clean custom version of {@link net.minecraft.client.gui.GuiMainMenu}.
 * We strip every non-essential like the diaporama and demo & realms stuff.
 * @author VoidAlchemist
 *
 */
@Hook("net.minecraft.client.Minecraft#displayGuiScreen(GuiScreen) -> line 1026, 1033"
		+ "net.minecraft.client.Minecraft#startGame() -> line 602, 606"
		+ "net.minecraft.client.gui.GuiGameOver#actionPerformed(GuiButton) -> line 79 && #confirmClicked(boolean, int) -> line 96"
		+ "net.minecraft.client.gui.GuiIngameMenu#actionPerformed(GuiButton) -> line 60, 64, 67"
		+ "net.minecraft.client.gui.GuiMemoryErrorScreen#actionPerformed(GuiButton) -> line 29"
		+ "net.minecraft.client.network.NetHandlerPlayClient#onDisconnect(IChatComponent) -> line 813"
		+ "DELETED at net.minecraft.client.renderer.EntityRenderer#frameInit() -> line 2509, 2511 && #updateMainMenu() -> all function"
		+ "net.minecraft.realms.Realms#inTitleScreen() -> line 117"
		+ "DELETED at net.optifine.reflect.Reflector#GuiMainMenu && #GuiMainMenu_splashText")
public class GuiMainMenuXenon extends GuiScreen implements GuiYesNoCallback{

	private static final ResourceLocation background = new ResourceLocation("xenon/background_mainmenu.png");
	private static final ResourceLocation xenonTitle = new ResourceLocation("xenon/xenon.png");
	private static final String quitInfo = "Click to quit MC";
	private static final String[] singleplayerInfo = new String[] {"Ready to explore worlds and defend yourself against monsters ?",
			"Survival, building, redstone, command blocks and more !"};
	private static final String[] multiplayerInfo = new String[] {"Zombies are boring and you seek new horizons.",
			"PvP, mini-games, and lot more than in singleplayer !"};
	private static final String optionsInfo = "Click to see all the marvelous Minecraft options.";
	
	/** OpenGL graphics card warning. */
	private String openGLWarning1;
	/** OpenGL graphics card warning. */
	private String openGLWarning2;
	/** Link to the Mojang Support about minimum requirements */
	private String openGLWarningLink;
	
	private int glWarningWidth;
	private int tick;
	
	public GuiMainMenuXenon() {
		XenonClient.instance.discordRP.mainmenuHook();
		if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported())
        {
			this.openGLWarning1 = I18n.format("title.oldgl1");
            this.openGLWarning2 = I18n.format("title.oldgl2");
            this.openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
        }
	}
	
	/**
	 * The last button seems to bug the entire drawScreen method if not given any String[] info. null doesn't work nor new String[0] nor new String[]{""} does.
	 * Pretty weird, couldn't solve it. Smg to do with blending.
	 */
	@Override
	public void initGui() {
		
		int j = this.height / 4 + 48;
		int w = this.width / 2 - 100;
		this.buttonList.add(new IconButton(0, this.width - 40, 20, 25, 25, 0, quitInfo));
		this.buttonList.add(new RoundedButton(1, I18n.format("menu.singleplayer"), w, j, 200, 20, singleplayerInfo));
        this.buttonList.add(new RoundedButton(2, I18n.format("menu.multiplayer"), w, j + 24, 200, 20, multiplayerInfo));
        this.buttonList.add(new RoundedButton(3, I18n.format("menu.options"), w, j + 84, 200, 20, optionsInfo));
        
        this.glWarningWidth = Math.max(fontRendererObj.getStringWidth(openGLWarning2), fontRendererObj.getStringWidth(openGLWarning1));
	}
	
	private boolean shouldtick;
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.mc.getTextureManager().bindTexture(background);
		RenderUtils.drawTexturedRect((int)this.zLevel, 0, 0, width, height);

		super.drawScreen(mouseX, mouseY, partialTicks);
		 
		this.mc.getTextureManager().bindTexture(xenonTitle);
		pushMatrix();
		enableBlend();
        tryBlendFuncSeparate(770, 771, 1, 0);
        blendFunc(770, 771);
		this.drawTexturedModalRect((this.width >> 1) - 25, this.height/20, 0, 0, 50, 50);
		this.drawTexturedModalRect((this.width >> 1) - 62, this.height/20 +45, 0, 50, 124, 50);
				
		translate((this.width >> 1) - 2, this.height/20 + 34, 0);
		// in degrees !
		float angle = tick * 18f + 18f * partialTicks;
		if (partialTicks < 0.5f)
			shouldtick = true;
		if(partialTicks > 0.6f && shouldtick)
		{
			tick++;
			tick %= 20;	// avoid exceeding the float size limit for the angle. (360 / 18 = 20)
			shouldtick = false;
		}		
		rotate(angle, 0, 0, 1);
		RenderUtils.drawTexturedRect((int)this.zLevel, -13, -13, 26, 26, 0, 0.791016f, 0.20898f, 1f);
		disableBlend();
		popMatrix();
		
		RenderUtils.font.drawString("Xenon Client 2.0 | Minecraft 1.8.9", 2, this.height - 13, 0xFFFFFFFF);
		RenderUtils.font.drawString("©Mojang AB", this.width - 50, this.height - 13, 0xFFFFFFFF);
		RenderUtils.resetTextureState();
		
		if (this.openGLWarning1 != null && this.openGLWarning1.length() > 0)
        {
            Gui.drawRect(2, 2, this.glWarningWidth + 2, 26, 1428160512);
            this.fontRendererObj.drawString(openGLWarning1, 3, 3, -1);
            this.fontRendererObj.drawString(openGLWarning2, 3, 15, -1);
        }
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		switch(button.id) {
		case 0:
			this.mc.shutdown();
			break;
		case 1:
			this.mc.displayGuiScreen(new GuiSelectWorld(this));
			break;
		case 2:
			this.mc.displayGuiScreen(new GuiMultiplayer(this));
			break;
		case 3:
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
			break;
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        
        if (this.openGLWarning1 != null && this.openGLWarning1.length() > 0 && 2 <= mouseX && mouseX <= this.glWarningWidth + 2
        		&& 2 <= mouseY && mouseY <= 26) {
        	GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(this, this.openGLWarningLink, 13, true);
        	guiconfirmopenlink.disableSecurityWarning();
            this.mc.displayGuiScreen(guiconfirmopenlink);
        	
        }
    }
}
