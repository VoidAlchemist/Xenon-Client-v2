package com.xenon.modules.api;

import java.awt.Color;

import com.xenon.XenonClient;
import com.xenon.util.readability.Hook;
import com.xenon.util.readability.Static;
/**
 * Revamped MGlintColor.<br>
 * <strong>v1.0:</strong><br>
 * <code>java.awt.Color</code> instance for actual chroma color that was recreated each tick.<br>
 * <strong>v2.0:</strong><br>
 * now uses primitives because all we need is RGB <code>int</code> value.
 * @author VoidAlchemist
 * @since v2.0
 */
@Static
public class GlintEnchantAPI {

	public static int chromaColor = 0x00;
	private static float hue = 0f;
	
	/**
	 * 
	 * @return the real RGB which depends on whether chroma is enabled.
	 */
	@Hook("net.minecraft.client.renderer.entity.RenderItem#renderEffect(IBakedModel) -> line 245, 253, 260")
	public static int getRGBColor() {
		if (XenonClient.instance.settings.glintChroma)
			return chromaColor;
		return XenonClient.instance.settings.glintColor;
	}
	/**
	 * Ignores alpha.
	 * @return a float array containing all 3 RGB components normalized to 1.0f.
	 * @see java.awt.Color#HSBtoRGB(float hue, float saturation, float brightness)
	 */
	@Hook("net.minecraft.client.renderer.entity.layer.LayerArmorBase#renderGlint(...) -> line 176, 182")
	public static float[] getRGBComponents() {
		int rgb = getRGBColor();
		return new float[] {(float)((rgb >> 16)&255)/255f, (float)((rgb >> 8)&255)/255f, (float)(rgb&255)/255f};
	}
	
	/**
	 * Reset {@link com.xenon.modules.ModSettings#glintColor} back to vanilla value : 128 R, 64 G, 204 B.
	 */
	public static void resetColor() {
		XenonClient.instance.settings.glintColor = -8372020;
	}
	
	/**
	 * Call this method regularly to render a chroma effect.
	 */
	public static void call() {
		hue += 0.01f;
		chromaColor = Color.HSBtoRGB(hue, 0.69f, 0.8f);
		
		if (hue > 1)	hue = 0;
	}
	
}
