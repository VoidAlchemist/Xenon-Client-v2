package com.xenon.client.gui.modulesconf.components;

/**
 * 
 * @author VoidAlchemist
 *
 */
public abstract class Component {

	public final int width, height;
	protected int x, y;
	
	public Component(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Main painting method<br>
	 * Always super this method before doing anything else, otherwise this Component's x & y might be off.<br>
	 * As components will often be put in line, one should translate the brush by its height but not by its width.<br>
	 * The x position of the brush can easily be reset by calling <code>g.x = g.x1();</code>.
	 * @param g
	 */
	public void paint(Graphics g) {
		x = g.x;
		y = g.y;
	}
	
	/**
	 * Called when the mouse is clicked. If the mouse actually clicked the component, {@link #clickListener(int, int)} is called.<br>
	 * So to modify the behavior when the component is clicked, one should generally override {@link #clickListener(int, int)}, not this method.
	 * @param mouseX
	 * @param mouseY
	 * @return
	 */
	public boolean onMouseclicked(int mouseX, int mouseY) {
		if (isMouseOver(mouseX, mouseY)) {
			clickListener(mouseX, mouseY);
			return true;
		}
		return false;
	}
	
	/**
	 * Called when the component's been clicked.
	 * @param mouseX
	 * @param mouseY
	 */
	public abstract void clickListener(int mouseX, int mouseY);
	
	/**
	 * 
	 * @param mouseX
	 * @param mouseY
	 * @return if the mouse's coordinates are in the component's bounding box
	 */
	protected boolean isMouseOver(int mouseX, int mouseY) {
		return x <= mouseX && mouseX <= x + width &&
				y <= mouseY && mouseY <= y + height;
	}
	
}
