package com.xenon.util;

import static net.minecraft.client.renderer.GlStateManager.activeTextureUnit;
import static net.minecraft.client.renderer.GlStateManager.color;
import static net.minecraft.client.renderer.GlStateManager.disableAlpha;
import static net.minecraft.client.renderer.GlStateManager.disableBlend;
import static net.minecraft.client.renderer.GlStateManager.disableTexture2D;
import static net.minecraft.client.renderer.GlStateManager.enableAlpha;
import static net.minecraft.client.renderer.GlStateManager.enableBlend;
import static net.minecraft.client.renderer.GlStateManager.enableTexture2D;
import static net.minecraft.client.renderer.GlStateManager.popMatrix;
import static net.minecraft.client.renderer.GlStateManager.pushMatrix;
import static net.minecraft.client.renderer.GlStateManager.resetColor;
import static net.minecraft.client.renderer.GlStateManager.shadeModel;
import static net.minecraft.client.renderer.GlStateManager.textureState;
import static net.minecraft.client.renderer.GlStateManager.translate;
import static net.minecraft.client.renderer.GlStateManager.tryBlendFuncSeparate;

import com.xenon.util.readability.Static;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
/**
 * static methods to render various things.
 * @author VoidAlchemist
 * @since v2.0
 */
@Static
public class RenderUtils{

	/**
	 * Warning : there's no 0 in roman, so 1 in roman is accessed with index 0.
	 */
	public static final String[] roman = new String[] {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", "XIII", "XIV", "XV",
			"XVI", "XVII", "XVIII", "XIX", "XX"};
	public static final int WHITE = 0xFFFFFFFF;
	public static final int BLACK = 0x0;
	public static UnicodeFontRenderer font;
	
	/**
	 * UnicodeFontRenderer needs a GL11 context to initialize, thus we need to initialize the font field manually, <br>
	 * in {@link com.xenon.XenonClient#postInit()}
	 */
	public static void init() {
		font = UnicodeFontRenderer.getFontOnPC("bahnschrift", 18);
	}
	
	/**
	 * Used to reset GL11 texture state after drawing custom font.
	 * @see com.xenon.util.UnicodeFontRenderer
	 * @author Eric Golde
	 */
	public static void resetTextureState()
	{
		resetColor();
		textureState[activeTextureUnit].textureName = -1;
	}
	
	public static float[] getRGBAComponents(int color) {
		return new float[] {(float)(color >> 16 & 255)/255f, 
				(float)(color >> 8 & 255)/255f, (float)(color & 255)/255f, (float)(color >> 24 & 255)/255f};
	}
	
	
	public static void drawToolTip(int x, int y, int zlevel, String... lines) {
		int width = 0;
		for (String line : lines)
			if (line.length() > width)	width = line.length();
		width *= 5; // Font size
		
		pushMatrix();
		translate(x - width, y, zlevel);
		drawGradientRoundedRect(0, 0, 0, width + 4, lines.length * 12, 0x66232323, 0x66000000, 2, 5);
		for (int i=0; i < lines.length; i++) {
			font.drawString(lines[i], 2, i*12,0xFFFFFFFF);
		}
		resetTextureState();
		popMatrix();
	}
	
	/**
	 * Vanilla {@link net.minecraft.client.gui.Gui#drawTexturedModalRect(int, int, int, int, int, int)}. Use when no GUI instance is owned.
	 * @param x
	 * @param y
	 * @param textureX
	 * @param textureY
	 * @param width
	 * @param height
	 */
	public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double)(x + 0), (double)(y + height), 0d).tex((double)((float)(textureX + 0) * f), (double)((float)(textureY + height) * f1)).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + height), 0d).tex((double)((float)(textureX + width) * f), (double)((float)(textureY + height) * f1)).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + 0), 0d).tex((double)((float)(textureX + width) * f), (double)((float)(textureY + 0) * f1)).endVertex();
        worldrenderer.pos((double)(x + 0), (double)(y + 0), 0d).tex((double)((float)(textureX + 0) * f), (double)((float)(textureY + 0) * f1)).endVertex();
        tessellator.draw();
    }
	
	/**
	 * Helper method to draw the entire resource image bound in the given rectangle on screen.
	 * @param z
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public static void drawTexturedRect(int z, int x, int y, int width, int height) {
		drawTexturedRect(z, x, y, width, height, 0f, 0f, 1f, 1f);
	}
	/**
	 * Draws a textured rectangle with all the given parameters.
	 * Vanilla methods such as {@link net.minecraft.client.gui.Gui#drawTexturedModalRect(float, float, int, int, int, int)} 
	 * don't support well custom sized images.
	 * @param z
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param minU
	 * @param minV
	 * @param maxU
	 * @param maxV
	 */
	public static void drawTexturedRect(int z, int x, int y, int width, int height, float minU, float minV, float maxU, float maxV) {
		Tessellator t = Tessellator.getInstance();
		WorldRenderer w = t.getWorldRenderer();
		w.begin(7, DefaultVertexFormats.POSITION_TEX);
		w.pos(x, y, z).tex(minU, minV).endVertex();
		w.pos(x, y + height, z).tex(minU, maxV).endVertex();
		w.pos(x + width, y + height, z).tex(maxU, maxV).endVertex();
		w.pos(x + width, y, z).tex(maxU, minV).endVertex();
		t.draw();
	}
	
	/**
	 * Draws a rounded rectangle. Uses MC maths, so it's no use setting a precision higher than 500-1000.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 * @param curveradius
	 * @param precision 100 is a very good value for large rectangles (about 1/10 of the screen).<br>
	 * For smaller rectangles (1/20 of the screen), 10 or 20 is plenty good.
	 */
	public static void drawRoundedRect(int x, int y, int width, int height, int color, double curveradius, int precision) {
		float red = (float)(color >> 16 & 255)/255f;
		float green = (float)(color >> 8 & 255)/255f;
		float blue = (float)(color & 255)/255f;
		float alpha = (float)(color >> 24 & 255)/255f;
		color(red, green, blue, alpha);
		// upper rectangle
		double ux1 = x + curveradius;
		double uy1 = y;
		double ux2 = x + width - curveradius;
		double uy2 = y + curveradius;
		drawRect(ux1, uy1, ux2, uy2);
		
		// middle rectangle
		double mx1 = x;
		double my1 = y + curveradius;
		double mx2 = x + width;
		double my2 = y + height - curveradius;
		drawRect(mx1, my1, mx2, my2);
		
		// lower rectangle
		double lx1 = ux1;
		double ly1 = my2;
		double lx2 = ux2;
		double ly2 = y + height;
		drawRect(lx1, ly1, lx2, ly2);
		
		double angleDelta = Math.PI / (2d * precision);
		double actualAngle = 0;
		double yCached = 0;
		// corners
		for (int i=1; i <= precision; i++) {
			actualAngle += angleDelta;
			double xFromInnerCorner = (double)MathHelper.cos((float) actualAngle) * curveradius;
			double yFromInnerCorner = (double)MathHelper.sin((float)actualAngle) * curveradius;
			double y1 = uy2 - yFromInnerCorner;
			double y2 = uy2 - yCached;
			drawRect(ux1 - xFromInnerCorner, y1, ux1, y2);
			drawRect(ux2, y1, ux2 + xFromInnerCorner, y2);
			y1 = ly1 + yCached;
			y2 = ly1 + yFromInnerCorner;
			drawRect(lx1 - xFromInnerCorner, y1, lx1, y2);
			drawRect(lx2, y1, lx2 + xFromInnerCorner, y2);
			yCached = yFromInnerCorner;
		}
	}
	
	/**
	 * Draws a basic colored rectangle. Clean version of {@link net.minecraft.client.gui.Gui#drawRect(int, int, int, int, int)}.
	 * @param x
	 * @param y
	 * @param x2
	 * @param y2
	 * @param color
	 */
	public static void drawRect(double x, double y, double x2, double y2, int color) {
		float red = (float)(color >> 16 & 255) / 255f;
		float green = (float)(color >> 8 & 255) / 255f;
		float blue = (float)(color & 255) / 255f;
		float alpha = (float)(color >> 24 & 255) / 255f;
		disableTexture2D();
        enableBlend();
        tryBlendFuncSeparate(770, 771, 1, 0);
        color(red, green, blue, alpha);
		Tessellator t = Tessellator.getInstance();
		WorldRenderer w = t.getWorldRenderer();
		w.begin(7, DefaultVertexFormats.POSITION);
        w.pos(x, y, 0).endVertex();
        w.pos(x, y2, 0).endVertex();
        w.pos(x2, y2, 0).endVertex();
        w.pos(x2, y, 0).endVertex();
        t.draw();
        disableBlend();
        enableTexture2D();
	}
	
	
	/**
	 * Draws a basic rectangle. Can be colored by calling <code>GlStateManager.color(r, g, b, a)</code> 
	 * before (avoids recalculating the same color repeatedly).
	 * @param x
	 * @param y
	 * @param x2
	 * @param y2
	 */
	public static void drawRect(double x, double y, double x2, double y2) {
		drawRect(0, x, y, x2, y2);
	}
	/**
	 * Draws a basic rectangle. Can be colored by calling <code>GlStateManager.color(r, g, b, a)</code> 
	 * before (avoids recalculating the same color repeatedly).
	 * @param z
	 * @param x
	 * @param y
	 * @param x2
	 * @param y2
	 */
	public static void drawRect(double z, double x, double y, double x2, double y2) {
		disableTexture2D();
        enableBlend();
        tryBlendFuncSeparate(770, 771, 1, 0);
		Tessellator t = Tessellator.getInstance();
		WorldRenderer w = t.getWorldRenderer();
		w.begin(7, DefaultVertexFormats.POSITION);
        w.pos(x, y, z).endVertex();
        w.pos(x, y2, z).endVertex();
        w.pos(x2, y2, z).endVertex();
        w.pos(x2, y, z).endVertex();
        t.draw();
        disableBlend();
        enableTexture2D();
	}
	
	/**
	 * Draws a vertical-gradient-colored rounded rectangle.
	 * @param z
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param startcolor top color
	 * @param endcolor bottom color
	 * @param curveradius
	 * @param precision
	 */
	public static void drawGradientRoundedRect(int z, int x, int y, int width, int height, int startcolor, int endcolor, 
			double curveradius, int precision) {
		int r1 = startcolor >> 16 & 255;
		int g1 = startcolor >> 8 & 255;
		int b1 = startcolor & 255;
		int a1 = startcolor >> 24 & 255;
		
		int r2 = endcolor >> 16 & 255;
		int g2 = endcolor >> 8 & 255;
		int b2 = endcolor & 255;
		int a2 = endcolor >> 24 & 255;
		
		double slopeRed = (double)(r2 - r1)/(double)height;
		double slopeGreen = (double)(g2 - g1)/(double)height;
		double slopeBlue = (double)(b2 - b1)/(double)height;
		double slopeAlpha = (double)(a2 - a1)/(double)height;
		
		// upper rectangle
		double ux1 = x + curveradius;
		double uy1 = y;
		double ux2 = x + width - curveradius;
		double uy2 = y + curveradius;
		drawGradientRect(z, ux1, uy1, ux2, uy2, (float)r1/255f, (float)g1/255f, (float)b1/255f, (float)a1/255f, 
				(float)getImageLinearColor(slopeRed, y, r1, uy2)/255f, 
				(float)getImageLinearColor(slopeGreen, y, g1, uy2)/255f, 
				(float)getImageLinearColor(slopeBlue, y, b1, uy2)/255f, 
				(float)getImageLinearColor(slopeAlpha, y, a1, uy2)/255f);
		
		// middle rectangle
		double mx1 = x;
		double my1 = y + curveradius;
		double mx2 = x + width;
		double my2 = y + height - curveradius;
		drawGradientRect(z, mx1, my1, mx2, my2, 
				(float)getImageLinearColor(slopeRed, y, r1, my1)/255f, 
				(float)getImageLinearColor(slopeGreen, y, g1, my1)/255f, 
				(float)getImageLinearColor(slopeBlue, y, b1, my1)/255f, 
				(float)getImageLinearColor(slopeAlpha, y, a1, my1)/255f, 
				(float)getImageLinearColor(slopeRed, y, r1, my2)/255f, 
				(float)getImageLinearColor(slopeGreen, y, g1, my2)/255f, 
				(float)getImageLinearColor(slopeBlue, y, b1, my2)/255f, 
				(float)getImageLinearColor(slopeAlpha, y, a1, my2)/255f);
		
		// lower rectangle
		double lx1 = ux1;
		double ly1 = my2;
		double lx2 = ux2;
		double ly2 = y + height;
		drawGradientRect(z, lx1, ly1, lx2, ly2, 
				(float)getImageLinearColor(slopeRed, y, r1, ly1)/255f, 
				(float)getImageLinearColor(slopeGreen, y, g1, ly1)/255f, 
				(float)getImageLinearColor(slopeBlue, y, b1, ly1)/255f, 
				(float)getImageLinearColor(slopeAlpha, y, a1, ly1)/255f, (float)r2/255f, (float)g2/255f, (float)b2/255f, (float)a2/255f);

		double angleDelta = Math.PI / (2d * precision);
		double actualAngle = 0;
		double yCached = 0;
		// corners
		for (int i=1; i <= precision; i++) {
			actualAngle += angleDelta;
			double xFromInnerCorner = (double)MathHelper.cos((float) actualAngle) * curveradius;
			double yFromInnerCorner = (double)MathHelper.sin((float)actualAngle) * curveradius;
			double x1 = ux1 - xFromInnerCorner;
			double y1 = uy2 - yFromInnerCorner;
			double x2 = ux1;
			double y2 = uy2 - yCached;
			drawGradientRect(z, x1, y1, x2, y2, 
					(float)getImageLinearColor(slopeRed, y, r1, y1)/255f, 
					(float)getImageLinearColor(slopeGreen, y, g1, y1)/255f, 
					(float)getImageLinearColor(slopeBlue, y, b1, y1)/255f, 
					(float)getImageLinearColor(slopeAlpha, y, a1, y1)/255f,
					
					(float)getImageLinearColor(slopeRed, y, r1, y2)/255f, 
					(float)getImageLinearColor(slopeGreen, y, g1, y2)/255f, 
					(float)getImageLinearColor(slopeBlue, y, b1, y2)/255f, 
					(float)getImageLinearColor(slopeAlpha, y, a1, y2)/255f);
			x1 = ux2;
			x2 = ux2 + xFromInnerCorner;
			drawGradientRect(z, x1, y1, x2, y2, 
					(float)getImageLinearColor(slopeRed, y, r1, y1)/255f, 
					(float)getImageLinearColor(slopeGreen, y, g1, y1)/255f,
					(float)getImageLinearColor(slopeBlue, y, b1, y1)/255f, 
					(float)getImageLinearColor(slopeAlpha, y, a1, y1)/255f, 
					
					(float)getImageLinearColor(slopeRed, y, r1, y2)/255f, 
					(float)getImageLinearColor(slopeGreen, y, g1, y2)/255f, 
					(float)getImageLinearColor(slopeBlue, y, b1, y2)/255f, 
					(float)getImageLinearColor(slopeAlpha, y, a1, y2)/255f);
			x1 = lx1 - xFromInnerCorner;
			y1 = ly1 + yCached;
			x2 = lx1;
			y2 = ly1 + yFromInnerCorner;
			drawGradientRect(z, x1, y1, x2, y2, 
					(float)getImageLinearColor(slopeRed, y, r1, y1)/255f, 
					(float)getImageLinearColor(slopeGreen, y, g1, y1)/255f, 
					(float)getImageLinearColor(slopeBlue, y, b1, y1)/255f, 
					(float)getImageLinearColor(slopeAlpha, y, a1, y1)/255f, 
					
					(float)getImageLinearColor(slopeRed, y, r1, y2)/255f, 
					(float)getImageLinearColor(slopeGreen, y, g1, y2)/255f, 
					(float)getImageLinearColor(slopeBlue, y, b1, y2)/255f, 
					(float)getImageLinearColor(slopeAlpha, y, a1, y2)/255f);
			x1 = lx2;
			x2 = lx2 + xFromInnerCorner;
			drawGradientRect(z, x1, y1, x2, y2, 
					(float)getImageLinearColor(slopeRed, y, r1, y1)/255f, 
					(float)getImageLinearColor(slopeGreen, y, g1, y1)/255f, 
					(float)getImageLinearColor(slopeBlue, y, b1, y1)/255f, 
					(float)getImageLinearColor(slopeAlpha, y, a1, y1)/255f,
					
					(float)getImageLinearColor(slopeRed, y, r1, y2)/255f, 
					(float)getImageLinearColor(slopeGreen, y, g1, y2)/255f, 
					(float)getImageLinearColor(slopeBlue, y, b1, y2)/255f, 
					(float)getImageLinearColor(slopeAlpha, y, a1, y2)/255f);			
			
			yCached = yFromInnerCorner;
		}
	}
	
	/**
	 * Represents a linear function u -> f(u) | position -> colorComponent, such that f(u) = f'(a)(u - a) + f(a).  
	 * @param slope
	 * @param a the xCoord of a point on the curve of f.
	 * @param fa the yCoord of the point 'a'. integer because a color component.
	 * @param u the abscissa we want to get color for.
	 * @return the image of u by the function given its slope, a point on the curve abscissa & ordinate.
	 */
	private static int getImageLinearColor(double slope, double a, int fa, double u) {
		return (int) (slope * (u - a) + fa);
	}
	
	/**
	 * Draws a basic vertical-gradient-color rectangle. Clean version of {@link net.minecraft.client.gui.Gui#drawGradientRect(int*6)}
	 * @param z
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 * @param startColor
	 * @param endColor
	 */
	public static void drawGradientRect(double z, double left, double top, double right, double bottom, int startColor, int endColor)
    {
        float[] rgba1 = getRGBAComponents(startColor);
        float[] rgba2 = getRGBAComponents(endColor);
        drawGradientRect(z, left, top, right, bottom, rgba1[0], rgba1[1], rgba1[2], rgba1[3], rgba2[0], rgba2[1], rgba2[2], rgba2[3]);
    }
	
	/**
	 * Draws a vertical-gradient colored rectangle. Ugly (on purpose) version of {@link net.minecraft.gui.Gui#drawGradientRect(int*6)}.
	 * @param z
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 * @param red1 top color red
	 * @param green1 top color green
	 * @param blue1 top color blue
	 * @param alpha1 top color alpha
	 * @param red2 bottom color red
	 * @param green2 bottom color green
	 * @param blue2 bottom color blue
	 * @param alpha2 bottom color alpha
	 */
	public static void drawGradientRect(double z, double left, double top, double right, double bottom, float red1, 
			float green1, float blue1, float alpha1, 
			float red2, float green2, float blue2, float alpha2)
    {
        disableTexture2D();
        enableBlend();
        disableAlpha();
        tryBlendFuncSeparate(770, 771, 1, 0);
        shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(right, top, z).color(red1, green1, blue1, alpha1).endVertex();
        worldrenderer.pos(left, top, z).color(red1, green1, blue1, alpha1).endVertex();
        worldrenderer.pos(left, bottom, z).color(red2, green2, blue2, alpha2).endVertex();
        worldrenderer.pos(right, bottom, z).color(red2, green2, blue2, alpha2).endVertex();
        tessellator.draw();
        shadeModel(7424);
        disableBlend();
        enableAlpha();
        enableTexture2D();
    }
}
