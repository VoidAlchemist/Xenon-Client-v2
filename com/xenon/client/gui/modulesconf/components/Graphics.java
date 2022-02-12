package com.xenon.client.gui.modulesconf.components;

import com.xenon.util.RenderUtils;

/**
 * 
 * @author VoidAlchemist
 *
 */
public class Graphics {

	/**
	 * Canvas bounds coordinates.
	 */
	private int x1, y1, x2, y2;
	
	/**
	 * The brush current coordinates
	 */
	public int x, y;
	
	/**
	 * Mouse coordinates
	 */
	public int mouseX, mouseY;
	
	/**
	 * Debug constructor
	 */
	public Graphics() {}
	
	/**
	 * Instantiate the bounds.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public Graphics(int x, int y, int width, int height) {
		updateBounds(x, y, width, height);
		resetBrush();
	}
	
	/**
	 * 
	 * @return the top-left bound corner's x coordinate
	 */
	public int x1() {return x1;}
	/**
	 * 
	 * @return the top-left bound corner's y coordinate
	 */
	public int y1() {return y1;}
	/**
	 * 
	 * @return the bottom-right bound corner's x coordinate
	 */
	public int x2() {return x2;}
	/**
	 * 
	 * @return the bottom-right bound corner's y coordinate
	 */
	public int y2() {return y2;}
	
	
	
	/**
	 * Sets the bound top-left corner's x coordinate value
	 * @param x the new value
	 */
	public void x1(int x) {x1 = x;}
	/**
	 * Sets the bound top-left corner's y coordinate value
	 * @param y the new value
	 */
	public void y1(int y) {y1 = y;}
	/**
	 * Sets the bound bottom-right corner's x coordinate value
	 * @param x the new value
	 */
	public void x2(int x) {x2 = x;}
	/**
	 * Sets the bound bottom-right corner's y coordinate value
	 * @param y the new value
	 */
	public void y2(int y) {y2 = y;}
	/**
	 * 
	 * @return the canvas' bounds' width
	 */
	public int width() {return x2 - x1;}
	/**
	 * 
	 * @return the canvas' bounds' height
	 */
	public int height() {return y2 - y1;}
	/**
	 * Sets the canvas' bounds' width value
	 * @param w
	 */
	public void width(int w) {x2 = x1 + w;}
	/**
	 * Sets the canvas' bounds' height value
	 * @param h
	 */
	public void height(int h) {y2 = y1 + h;}
	
	/**
	 * Updates this instance bounds. Equivalent to creating a new <code>Graphics</code> object.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void updateBounds(int x, int y, int width, int height) {
		x1(x);
		y1(y);
		width(width);
		height(height);
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return if x & y are effectively in this instance's bounds
	 */
	public boolean isInBounds(int x, int y) {
		return x1 <= x && x <= x2 && y1 <= y && y <= y2;
	}
	
	/**
	 * 
	 * @return whether the current mouse coordinates held by this instance are in its bounds.
	 */
	public boolean isMouseInBounds() {
		return isInBounds(mouseX, mouseY);
	}
	
	/**
	 * Reset this Graphics instance's brush position.
	 */
	public void resetBrush() {
		x = x1;
		y = y1;
	}
	
	/**
	 * Translates the brush.
	 * @param x the x offset
	 * @param y the y offset
	 */
	public void translatebrush(int x, int y) {
		this.x += x;
		this.y += y;
	}
	/**
	 * Translates the brush.
	 * @param x the x offset
	 */
	public void translatebrushX(int x) {
		this.x += x;
	}
	/**
	 * Translates the brush.
	 * @param y the y offset
	 */
	public void translatebrushY(int y) {
		this.y += y;
	}
	
	/**
	 * Draws a simple string within this instance's bounds.
	 * @param text
	 * @param x
	 * @param y
	 * @param color
	 */
	public void drawString(String text, int x, int y, int color) {
		if (isInBounds(x, y) && isInBounds(x, y + 10)) {
			RenderUtils.font.drawString(text, x, y, color);
			RenderUtils.resetTextureState();
		}
	}
	
	public void drawCenteredString(String text, int x, int y, int color) {
		if (isInBounds(x, y) && isInBounds(x, y + 10)) {
			RenderUtils.font.drawCenteredString(text, x, y, color);
			RenderUtils.resetTextureState();
		}
	}
	/**
	 * Draws a gradient-colored rectangle within this instance's bounds.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param startcolor
	 * @param endcolor
	 */
	public void drawGradientRect(int x, int y, int width, int height, int startcolor, int endcolor) {
		
		int x1 = x + width;
		int y1 = y + height;
		
		if (x < this.x1) {
			if (x1 < this.x1)	return;
			x = this.x1;
		}
		if (x1 > this.x2) {
			if (x > this.x2)	return;
			x1 = this.x2;
		}
		if (y < this.y1) {
			if (y1 < this.y1)	return;
			y = this.y1;
		}
		if (y1 > this.y2) {
			if (y > this.y2)	return;
			y1 = this.y2;
		}
		
		RenderUtils.drawGradientRect(0, x, y, x1, y1, startcolor, endcolor);
	}
	/**
	 * Draws a simple colored rectangle within this instance's bounds.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 */
	public void drawRect(int x, int y, int width, int height, int color) {
		
		int x1 = x + width;
		int y1 = y + height;
		
		if (x < this.x1) {
			if (x1 < this.x1)	return;
			x = this.x1;
		}
		if (x1 > this.x2) {
			if (x > this.x2)	return;
			x1 = this.x2;
		}
		if (y < this.y1) {
			if (y1 < this.y1)	return;
			y = this.y1;
		}
		if (y1 > this.y2) {
			if (y > this.y2)	return;
			y1 = this.y2;
		}
		
		RenderUtils.drawRect(x, y, x1, y1, color);
	}
	
	/**
	 * Same method as {@link #drawRect(int, int, int, int, int)} but uses the brush coordinates to start the rectangle.<br>
	 * Prefer this over {@link #drawRect(int, int, int, int, int)}.
	 * @param width
	 * @param height
	 * @param color
	 */
	public void drawRect(int width, int height, int color) {
		drawRect(this.x, this.y, width, height, color);
	}
	/**
	 * Same method as {@link #drawGradientRect(int, int, int, int, int, int)} but uses the brush coordinates to start the rectangle.<br>
	 * Prefer this over {@link #drawGradientRect(int, int, int, int, int, int)}.
	 * @param width
	 * @param height
	 * @param startcolor
	 * @param endcolor
	 */
	public void drawGradientRect(int width, int height, int startcolor, int endcolor) {
		drawGradientRect(this.x, this.y, width, height, startcolor, endcolor);
	}
	/**
	 * Same method as {@link #drawString(String, int, int, int)} but uses the brush coordinates to start drawing the string.<br>
	 * Prefer this over {@link #drawString(String, int, int, int)}.
	 * @param text
	 * @param color
	 */
	public void drawString(String text, int color) {
		drawString(text, this.x, this.y, color);
	}
	
	public void drawCenteredString(String text, int color) {
		drawCenteredString(text, this.x, this.y, color);
	}
	
	
}
