package com.xenon.client.gui.components;

import com.xenon.util.RenderUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class RoundedButton extends GuiButton{
	
	protected String info, info2;
	/*The curve percentage of the button's height*/
	protected double curveRatio = 0.05d;
	
	public RoundedButton(int buttonId, String buttonText, int x, int y, int widthIn, int heightIn, String... info) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
		if (info.length > 0) {
			this.info = info[0];
			if (info.length > 1)
				this.info2 = info[1];
		}
	}
	public RoundedButton(int buttonId, String buttonText, int x, int y, int widthIn, int heightIn) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
	}
	public RoundedButton(int buttonId, int x, int y, int widthIn, int heightIn) {
		super(buttonId, x, y, widthIn, heightIn, null);
	}
	public RoundedButton(int buttonId, int x, int y, int widthIn, int heightIn, String... info) {
		this(buttonId, null, x, y, widthIn, heightIn, info);
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		RenderUtils.drawGradientRoundedRect((int)this.zLevel, xPosition, yPosition, width, height, 0xaa99E2E3, 0xaa99E3F0, curveRatio * (double)height, 10);
		
		if (displayString != null && displayString.length() > 0) {
			RenderUtils.font.drawCenteredString(displayString, xPosition + (width >> 1), yPosition + (height >> 1) - 5, 0xFFFFFFFF);
			RenderUtils.resetTextureState();
		}
		
		if (isMouseOver(mouseX, mouseY)) {
			RenderUtils.drawRoundedRect(xPosition, yPosition, width, height, 0x88FFFFFF, curveRatio * (double)height, 10);
			if (this.info != null && this.info.length() > 0)
			{
				if (this.info2 != null && this.info2.length() > 0)
					RenderUtils.drawToolTip(mouseX - 1, mouseY + 1, (int)this.zLevel, this.info, this.info2);
				else
					RenderUtils.drawToolTip(mouseX - 1, mouseY + 1, (int)this.zLevel, this.info);
			}
			
		}
			
	}

	public boolean isMouseOver(int mouseX, int mouseY) {
		return xPosition <= mouseX && mouseX <= xPosition + width && yPosition <= mouseY && mouseY <= yPosition + height;
	}
	
}
