package com.xenon.client.cape;

import org.lwjgl.opengl.GL11;

import com.xenon.util.PRNG;
import com.xenon.util.RenderUtils;

import net.minecraft.client.renderer.GlStateManager;
/**
 * 
 * @author VoidAlchemist
 *
 */
public class Sparkle {

	private static final int lifespan = 700;
	private double x, y, motion, size;
	private long spawnTime;
	public boolean dead;
	
	/**
	 * Creates a new sparkle, initially dead.
	 */
	public Sparkle() {
		dead = true;
	}
	
	/**
	 * Allows Object re-use.
	 * @param rand
	 */
	public void construct(PRNG rand) {
		dead = false;
		x = rand.nextDouble() * 400d;
		y = rand.nextDouble() * 1800d;
		spawnTime = System.currentTimeMillis();
		motion = 10d * (rand.nextDouble() - 0.5d);
		size = rand.nextDouble() + 0.9d;
	}
	
	public void render() {
		if (dead)	return;
		
		long time = System.currentTimeMillis() - spawnTime;
		double scale = (double)time / (double)lifespan / size;
		
		if (time >= lifespan)
			dead = true;
		else {
			GlStateManager.pushMatrix();
            GlStateManager.scale(5.0E-4D, 5.0E-4D, 5.0E-4D);
            GlStateManager.scale(scale, scale, scale);
            GlStateManager.enableBlend();
            GL11.glColor4f(255.0F, 255.0F, 255.0F, (float)(0.6000000238418579D - scale));
            GlStateManager.translate((this.x - 200.0D - (scale + this.motion) * 100.0D) / scale, (this.y + 150.0D - scale * 100.0D) / scale, -135.0D / scale);
            RenderUtils.drawTexturedModalRect(0, 0, 0, 0, 240, 250);
            GlStateManager.disableBlend();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
		}
	}
	
	
	public Sparkle setDead(boolean b) {
		dead = b;
		return this;
	}
	
}
