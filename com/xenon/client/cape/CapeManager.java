package com.xenon.client.cape;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.xenon.XenonClient;
import com.xenon.util.readability.Hook;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
/**
 * Revamped the cape system for better performance and to allow hiding all the capes but Xenon's.
 * @author VoidAlchemist
 *
 */
public class CapeManager {

	@Hook("net.minecraft.client.entity.AbstractClientPlayer#AbstractClientPlayer() -> line 48")
	public static final UUID capeowner = UUID.fromString("a6e65b11-337b-4343-8985-d3a49b9337d3");
	@Hook("net.minecraft.client.entity.AbstractClientPlayer#AbstractClientPlayer() -> line 48")
	public static final UUID capeowner1 = UUID.fromString("4332a2a9-f8a1-4397-a847-8812e6e7aaa2");
	private static final ResourceLocation locationCape = new ResourceLocation("xenon/cape.png");
	
	/**
	 * 
	 * @param playerRenderer
	 * @param player
	 * @param partialTicks
	 * @param scale
	 */
	@Hook("net.minecraft.client.renderer.entity.layers.LayerCape#doRenderLayer(...) -> line 23")
	public static void doRenderCape(RenderPlayer playerRenderer, AbstractClientPlayer player, float partialTicks, float scale) {
		boolean xenonOwner = player.xenonOwner;
		
		if (xenonOwner) {
			GlStateManager.color(1f, 1f, 1f, 1f);
			playerRenderer.bindTexture(locationCape);
		}
		else {
			if (!XenonClient.instance.settings.showCapes || !player.hasPlayerInfo() || player.isInvisible() ||
					!player.isWearing(EnumPlayerModelParts.CAPE))
				return;
			
			
			ResourceLocation loc = player.getLocationCape();
			if (loc == null)
				return;
			GlStateManager.color(1f, 1f, 1f, 1f);
			playerRenderer.bindTexture(loc);
		}
		
		
        GlStateManager.pushMatrix();
        GlStateManager.translate(0f, 0f, 0.125F);
        double d0 = player.prevChasingPosX + (player.chasingPosX - player.prevChasingPosX) * (double)partialTicks - (player.prevPosX + (player.posX - player.prevPosX) * (double)partialTicks);
        double d1 = player.prevChasingPosY + (player.chasingPosY - player.prevChasingPosY) * (double)partialTicks - (player.prevPosY + (player.posY - player.prevPosY) * (double)partialTicks);
        double d2 = player.prevChasingPosZ + (player.chasingPosZ - player.prevChasingPosZ) * (double)partialTicks - (player.prevPosZ + (player.posZ - player.prevPosZ) * (double)partialTicks);
        float f = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks;
        double d3 = (double)MathHelper.sin(f * (float)Math.PI / 180f);
        double d4 = (double)(-MathHelper.cos(f * (float)Math.PI / 180f));
        float f1 = (float)d1 * 10f;
        f1 = MathHelper.clamp_float(f1, -6.0F, 32.0F);
        float f2 = (float)(d0 * d3 + d2 * d4) * 100f;
        float f3 = (float)(d0 * d4 - d2 * d3) * 100f;

        if (f2 < 0f)
            f2 = 0f;

        if (f2 > 165.0F)
            f2 = 165.0F;

        if (f1 < -5.0F)
            f1 = -5.0F;

        float f4 = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * partialTicks;
        f1 = f1 + MathHelper.sin((player.prevDistanceWalkedModified + (player.distanceWalkedModified - player.prevDistanceWalkedModified) * partialTicks) * 6.0F) * 32.0F * f4;

        if (player.isSneaking())
        {
            f1 += 25.0F;
            GlStateManager.translate(0f, 0.142F, -0.0178F);
        }

        GlStateManager.rotate(6.0F + f2 / 2.0F + f1, 1f, 0f, 0f);
        GlStateManager.rotate(f3 / 2.0F, 0f, 0f, 1f);
        GlStateManager.rotate(-f3 / 2.0F, 0f, 1f, 0f);
        GlStateManager.rotate(180f, 0f, 1f, 0f);
        playerRenderer.getMainModel().renderCape(0.0625F);
        if (player.xenonCapeManager != null)
        	player.xenonCapeManager.renderSparkles();
        GlStateManager.popMatrix();
	}
	
	private static final ResourceLocation starLoc = new ResourceLocation("xenon/sparkle.png");
	private long lastTimeSpawned;
	/**
	 * Pool of sparkles. First filled in constructor with all the sparkles dead. Then set to true whenever we need them.
	 */
	private final List<Sparkle> sparkles = new ArrayList<>(32);
	/**
	 * The index pointing to the next sparkle that should be set not dead (should be used).
	 */
	private int nextDeadIndex;
	
	public CapeManager() {
		for (int __=0; __ < 32; __++)
			sparkles.add(new Sparkle());
	}
	
	
	private void renderSparkles() {
		Minecraft.getMinecraft().getTextureManager().bindTexture(starLoc);
		
		long currentTime = System.currentTimeMillis();
		if (lastTimeSpawned < currentTime) {
			sparkles.get(nextDeadIndex).construct(XenonClient.instance.random);
			nextDeadIndex = (nextDeadIndex + 1) & 31; // (nextDeadIndex + 1) % 32;
			lastTimeSpawned = currentTime + XenonClient.instance.random.nextInt(1000);
		}
		
		for (Sparkle sparkle : sparkles)
			sparkle.render();
				
	}
	
}
