package com.xenon.client.gui.modulesconf.components;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import com.xenon.util.RenderUtils;

import net.minecraft.client.renderer.GlStateManager;
/**
 * Test of writing a maintainable class for clean implementation of a frame system in MC GUIs.
 * @author VoidAlchemist
 * @since v2.0
 */
public class Frame {

	/**
	 * Holds the current drawing state such as the frame bounds, the starting x & y coordinates for the components to be rendered, etc.
	 */
	protected final Graphics graphics;
	/**
	 * Stores all the objects to be rendered here.
	 */
	protected final List<Component> renderlist;
	
	/**
	 * Represents the amount scrolled vertically by the user.
	 */
	private int scrollY;
	
	/**
	 * The sum of all the rendering list's components' height. Used for scrolling.
	 */
	private int totalheight;
	
	/**
	 * Creates a new rendering list with unknown capacity.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public Frame(int x, int y, int width, int height) {
		graphics = new Graphics(x, y, width, height);
		renderlist = new ArrayList<>();
	}

	/**
	 * Creates a new rendering list with fixed capacity.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param capacity the maximum numbers of objects rendered in this frame.
	 */
	public Frame(int x, int y, int width, int height, int capacity) {
		graphics = new Graphics(x, y, width, height);
		renderlist = new ArrayList<>(capacity);
	}
	
	
	/**
	 * Call this function in order to update the frame bounds.<br>
	 * Needed in case of window resizing, otherwise <code>onclick</code> method might not work.
	 * @param x the new bounds top-left corner's x coordinate
	 * @param y the new bounds top-left corner's y coordinate
	 * @param width the new bounds' width
	 * @param height the new bounds' height
	 */
	public void updateBounds(int x, int y, int width, int height) {
		graphics.updateBounds(x, y, width, height);
	}
	
	/**
	 * Call this function in order to update the mouse position of the frame's graphics.<br>
	 * Needed in case of window resizing, otherwise <code>onclick</code> method might not work.
	 * @param x
	 * @param y
	 */
	public void updateMousePos(int x, int y) {
		graphics.mouseX = x;
		graphics.mouseY = y;
	}
	
	/**
	 * Clears all the components in this frame and reset the scrolled amount;
	 */
	public void reset() {
		totalheight = 0;
		renderlist.clear();
		scrollY = 0;
	}
	
	/**
	 * Draws all the component to screen without the frame's bounds.<br>
	 * Also handles mouse scrolling.<br>
	 */
	public void draw() {
		GlStateManager.color(0.1f, 0.1f, 0.1f, 0.5f);
		RenderUtils.drawRect(graphics.x1() - 3, graphics.y1(), graphics.x1() - 2, graphics.y2());
		RenderUtils.drawRect(graphics.x2() + 2, graphics.y1(), graphics.x2() + 3, graphics.y2());
		
		graphics.resetBrush();
		if (renderlist.size() < 1)	return;
		
		int wheel = Mouse.getDWheel();
		
		if (wheel < 0 && totalheight + graphics.y + scrollY > graphics.y2())	scrollY -= 20;
		else if (wheel > 0 && graphics.y + scrollY < graphics.y1())	scrollY += 20;
		
		graphics.y += scrollY;
		
		for (Component component : renderlist)
			component.paint(graphics);
	}
	
	/**
	 * Call this function on a <code>MouseListener</code> event.<br>
	 * Warning :<br>
	 * an initial check is done to know if the mouse is inside the frame's bounds, so be sure to call {@link #updateBounds(int, int, int, int)} regularly.
	 */
	public void onclick() {
		if (graphics.isMouseInBounds()) {
			for (Component component : renderlist)
				if (component.onMouseclicked(graphics.mouseX, graphics.mouseY))
					return;
		}
	}
	
	/**
	 * Adds a component to the rendering list.
	 * @param component
	 */
	public void addComponent(Component component) {
		renderlist.add(component);
		totalheight += component.height + 5;	// 5 "MC pixels" between each component.
	}
	/**
	 * Adds multiple components to the rendering list.
	 * @param components
	 */
	public void addAllComponent(Component... components) {
		for (Component component : components)
			addComponent(component);
	}
	
	/**
	 * Adds multiple components to the rendering list.
	 * @param components
	 */
	public void addAllComponent(List<Component> components) {
		for (Component component : components)
			addComponent(component);
	}
	
	
	
	
}
