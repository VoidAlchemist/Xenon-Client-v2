package com.xenon.client.gui.modulesconf;

import com.xenon.client.gui.modulesconf.components.Category;
import com.xenon.client.gui.modulesconf.components.Component;
import com.xenon.client.gui.modulesconf.components.Graphics;
import com.xenon.client.gui.modulesconf.components.Slider;
import com.xenon.util.RenderUtils;

/**
 * Special container only used for glint because we need to have a rectangle that indicates the current selected glint color.
 * @author VoidAlchemist
 *
 */
public class GlintCategory extends Category{

	protected final Component header;
	
	/**
	 * 
	 * @param height equation : <code>height := 14 + header.height + lineCapacity * (buttons.height + 2)</code> 
	 * @param lineCapacity the known maximum number of lines in this category
	 * @param title
	 */
	public GlintCategory(int height, int lineCapacity, String title, Component header) {
		super(height, lineCapacity, title);
		this.header = header;
	}
	
	
	@Override
	public void paint(Graphics g) {
		// here we can't super cz we extends Category, and not directly Component
		this.x = g.x;
		this.y = g.y;
		
		g.drawCenteredString(title, (g.x1() + g.x2()) >> 1, g.y, 0xFFFFFFFF);
		g.translatebrushY(12);
		g.translatebrushX((g.width() >> 1) - (header.width >> 1));
		header.paint(g);
		g.x = g.x1();
		g.translatebrushY(header.height + 2);
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
		if (header.onMouseclicked(mouseX, mouseY))	return true;
		else
		return super.onMouseclicked(mouseX, mouseY);
	}
	
	@Override
	public void mouseReleased() {
		if (header instanceof Category)
			((Category)header).mouseReleased();
		else if (header instanceof Slider)
			((Slider)header).mouseReleased();
		else
		super.mouseReleased();
	}
	
	@Override
	public void mouseDragged(int mouseX, int mouseY) {
		if (header instanceof Category)
			((Category)header).mouseDragged(mouseX, mouseY);
		
		else if (header instanceof Slider)
			((Slider)header).mouseDragged(mouseX, mouseY);
		else
		super.mouseDragged(mouseX, mouseY);
	}

}
