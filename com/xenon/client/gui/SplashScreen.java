package com.xenon.client.gui;

import org.lwjgl.opengl.GL11;

import com.xenon.util.RenderUtils;
import com.xenon.util.readability.Hook;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
/**
 * 
 * @author VoidAlchemist
 * @see com.xenon.XenonClient#splashScreen
 */
public class SplashScreen {

	private final int max_progress = 9;
	private int current_progress = 0;
	private String current_display = null;
	private final ResourceLocation background;
	
	public SplashScreen() {
		background = new ResourceLocation("xenon/background_splash.png");
	}
	
	@Hook("com.xenon.XenonClient#init() -> line 52")
	public void setProgress(String newDisplay) {
		if (current_progress >= max_progress)	return;
		
		current_display = newDisplay;
		current_progress++;
		draw();
	}
	
	private void draw() {
		if (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().gameSettings == null)	return;
		
		mainDraw(new ScaledResolution(Minecraft.getMinecraft()));
	}
	
	/**
	 * Boiler-plate code with GL11 & Framebuffer is from Hyperium client {@link https://hyperium.cc}. all credits go to them
	 * @param sr
	 * @since v1.0
	 */
	private void mainDraw(ScaledResolution sr) {
		Framebuffer fb = new Framebuffer(sr.getScaledWidth() * sr.getScaleFactor(), sr.getScaledHeight() * sr.getScaleFactor(), true);
		fb.bindFramebuffer(false);
		
		GlStateManager.matrixMode(GL11.GL_PROJECTION);
		GlStateManager.loadIdentity();
		GlStateManager.ortho(0d, sr.getScaledWidth(), sr.getScaledHeight(), 0d, 1000d, 3000d);
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		GlStateManager.loadIdentity();
		GlStateManager.translate(0f, 0f, -2000f);
		GlStateManager.disableLighting();
		GlStateManager.disableFog();
		GlStateManager.disableDepth();
		GlStateManager.enableTexture2D();
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(background);
		
		Gui.drawScaledCustomSizeModalRect(0, 0, 0, 0, 1920, 1001, sr.getScaledWidth(), sr.getScaledHeight(), 1920, 1001);
		
		GlStateManager.resetColor();
		GlStateManager.color(1f, 1f, 1f, 1f);
		
		// interesting part start
		
		double ratio = (double)current_progress/(double)max_progress;
		Gui.drawRect(0, (int) (sr.getScaledHeight()*0.97), (int) (ratio*(double)sr.getScaledWidth()),sr.getScaledHeight(), 0xFFF4CA30);
		GlStateManager.resetColor();
		RenderUtils.font.drawStringScaled(current_display, 10, (int)(sr.getScaledHeight()*0.95) - 20, 0xF4CA30, 2d);
		// current_progress + "/" + max_progress
		RenderUtils.font.drawStringScaled(new StringBuilder(Integer.toString(current_progress)).append('/').append(max_progress).toString(), sr.getScaledWidth() - 45, (int)(sr.getScaledHeight()*0.95) - 20, 0xF4CA30, 2d);
		RenderUtils.resetTextureState();
		
		// interesting part end
		
		GlStateManager.color(1f, 1f, 1f, 1f);		
		
		fb.unbindFramebuffer();
		fb.framebufferRender(sr.getScaledWidth() * sr.getScaleFactor(), sr.getScaledHeight() * sr.getScaleFactor());
		
		GlStateManager.enableAlpha();
		GlStateManager.alphaFunc(516, 0.1f);
		
		Minecraft.getMinecraft().updateDisplay();
	}
	
	
}
