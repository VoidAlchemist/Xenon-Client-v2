package com.xenon.client.gui.modulesconf;

import com.xenon.client.gui.modulesconf.components.Category;
import com.xenon.client.gui.modulesconf.components.Component;
import com.xenon.client.gui.modulesconf.components.Frame;
import com.xenon.client.gui.modulesconf.components.Slider;

public class ConfFrame extends Frame{

	/**
	 * Creates a new rendering list with unknown capacity.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public ConfFrame(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	/**
	 * Creates a new rendering list with fixed capacity.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param capacity the maximum numbers of objects rendered in this frame.
	 */
	public ConfFrame(int x, int y, int width, int height, int capacity) {
		super(x, y, width, height, capacity);
	}
	
	/**
	 * Called when the mouse is being dragged. Used by Slider buttons.
	 * @param mouseX
	 * @param mouseY
	 */
	public void mouseDragged(int mouseX, int mouseY) {
		for (Component component : renderlist)
			if (component instanceof Category)
				((Category)component).mouseDragged(mouseX, mouseY);
			else if (component instanceof Slider)
				((Slider)component).mouseDragged(mouseX, mouseY);
	}
	
	/**
	 * Called when the mouse is released. Used by Slider buttons.
	 */
	public void mouseReleased() {
		for (Component component : renderlist)
			if (component instanceof Category)
				((Category)component).mouseReleased();
			else if (component instanceof Slider)
				((Slider)component).mouseReleased();
	}

}
