package com.xenon.client.gui.modulesconf.components;

import com.xenon.util.RenderUtils;

/**
 * 
 * @author VoidAlchemist
 *
 */
public class Category extends Component{

	protected final String title;
	protected final String[] lineText;
	protected final Component[] lineBtn;
	protected int size;

	/**
	 * 
	 * @param height equation : <code>height := 12 + lineCapacity * (buttons.height + 2)</code>
	 * @param lineCapacity the known maximum number of lines in this category
	 * @param title
	 */
	public Category(int height, int lineCapacity, String title) {
		super(0, height);
		this.title = title;
		lineText = new String[lineCapacity];
		lineBtn = new Component[lineCapacity];
	}
	
	
	/**
	 * Adds a text-button line to the category.
	 * @param text
	 * @param button Slider and OnOff buttons should be non-autonomous (they don't translate the graphics' brush by their height).
	 */
	public void addLine(String text, Component button) {
		lineText[size] = text;
		lineBtn[size] = button;
		size++;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		g.drawCenteredString(title, (g.x1() + g.x2()) >> 1, g.y, 0xFFFFFFFF);
		g.translatebrushY(12);
		for (int i=0; i < size; i++) {
			Component btn = lineBtn[i];
			
			g.drawRect(g.width(), btn.height + 2, 0xaa99E2E3);
			g.translatebrushY(1);
			g.translatebrushX(2);
			g.drawString(lineText[i], 0xFFFFFFFF);
			g.translatebrushX(g.width() - btn.width - 4);
			btn.paint(g);
			g.translatebrushY(btn.height + 1);
			g.x = g.x1();
		}
	}

	
	
	
	
	@Override
	public boolean onMouseclicked(int mouseX, int mouseY) {
		for (Component component : lineBtn)
			if (component.onMouseclicked(mouseX, mouseY))	return true;
		return false;
	}
	
	/**
	 * Called when the mouse released.
	 * The event is then passed to all slider held by this instance or to a category if this instance contains one.
	 */
	public void mouseReleased() {
		for (Component btn : lineBtn) {
			if (btn instanceof Category)
				((Category)btn).mouseReleased();
			else if (btn instanceof Slider)
				((Slider)btn).mouseReleased();
		}		
	}
	
	/**
	 * Called when the mouse is being dragged.
	 * The event is then passed to all slider held by this instance or to a category if this instance contains one.
	 * @param mouseX
	 * @param mouseY
	 */
	public void mouseDragged(int mouseX, int mouseY) {
		for (Component btn : lineBtn) {
			if (btn instanceof Category)
				((Category)btn).mouseDragged(mouseX, mouseY);
			else if (btn instanceof Slider)
				((Slider)btn).mouseDragged(mouseX, mouseY);
		}
	}
	

	
	/**
	 * This component is just a list of other components, so its behavior when clicked is different.<br>
	 * Thus, this method does nothing.
	 */
	@Override
	public void clickListener(int mouseX, int mouseY) {
	}
}
