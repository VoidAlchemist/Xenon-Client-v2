package com.xenon.client.gui.modulesconf.components;

import com.xenon.client.gui.components.RoundedButton;
import com.xenon.util.RenderUtils;

import net.minecraft.client.Minecraft;
/**
 * 
 * @author VoidAlchemist
 *
 */
public class Tab extends RoundedButton{
	
	public boolean isSelected;
	
	public Tab(int id, String tabTitle, int x, int y, int width, int height, String... info) {
		super(id, tabTitle, x, y, width, height, info);
		this.curveRatio = 0.2d;
	}
	public Tab(int id, String tabTitle, String... info) {
		super(id, tabTitle, 0, 0, 0, 0, info);
		this.curveRatio = 0.2d;
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	public void setPos(int x, int y) {
		this.xPosition = x;
		this.yPosition = y;
	}
	
	public Tab setSelected(boolean b) {
		isSelected = b;
		return this;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		RenderUtils.drawGradientRoundedRect((int)this.zLevel, xPosition, yPosition, width, height, 0xaa99E2E3, 0xaa99E3F0, curveRatio * (double)height, 10);
		
		if (displayString != null && displayString.length() > 0) {
			RenderUtils.font.drawCenteredString(displayString, xPosition + (width >> 1), yPosition + (height >> 1) - 5, 0xFFFFFFFF);
			RenderUtils.resetTextureState();
		}
		// isMouseOver called here wasn't the override one for some reason, so had to put manually isSelected.
		if (isMouseOver(mouseX, mouseY) || isSelected) {
			RenderUtils.drawRoundedRect(xPosition, yPosition, width, height, 0x88FFFFFF, curveRatio * (double)height, 10);			
		}
			
	}
}