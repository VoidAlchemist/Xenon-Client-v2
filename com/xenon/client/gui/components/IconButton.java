package com.xenon.client.gui.components;

import static net.minecraft.client.renderer.GlStateManager.blendFunc;
import static net.minecraft.client.renderer.GlStateManager.color;
import static net.minecraft.client.renderer.GlStateManager.disableBlend;
import static net.minecraft.client.renderer.GlStateManager.enableBlend;
import static net.minecraft.client.renderer.GlStateManager.popMatrix;
import static net.minecraft.client.renderer.GlStateManager.pushMatrix;
import static net.minecraft.client.renderer.GlStateManager.rotate;
import static net.minecraft.client.renderer.GlStateManager.translate;
import static net.minecraft.client.renderer.GlStateManager.tryBlendFuncSeparate;

import com.xenon.util.RenderUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class IconButton extends RoundedButton{

	private final int type;
	private static final ResourceLocation cogwheel = new ResourceLocation("xenon/cogwheel.png");
	
	/**
	 * 
	 * @param buttonId
	 * @param x
	 * @param y
	 * @param widthIn
	 * @param heightIn
	 * @param type the type of the icon : 0 for cross icon, 1 for cogwheel icon.
	 */
	public IconButton(int buttonId, int x, int y, int widthIn, int heightIn, int type) {
		super(buttonId, x, y, widthIn, heightIn);
		this.type = type;
	}
	/**
	 * 
	 * @param buttonId
	 * @param x
	 * @param y
	 * @param widthIn
	 * @param heightIn
	 * @param type the type of the icon : 0 for cross icon, 1 for cogwheel icon.
	 * @param info
	 */
	public IconButton(int buttonId, int x, int y, int widthIn, int heightIn, int type, String... info) {
		super(buttonId, x, y, widthIn, heightIn, info);
		this.type = type;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		RenderUtils.drawGradientRoundedRect((int)this.zLevel, xPosition, yPosition, width, height, 0xaa99E2E3, 0xaa99E3F0, 0.15d * (double)height, 15);
		color(1, 1, 1, 1);
		pushMatrix();
		if (type == 0) {
			double midHeight = (double)height/2d;
			translate((double)xPosition + (double)width/2d,(double)yPosition + midHeight, 0);
			rotate(45, 0, 0, 1);
			double crosshalfheight = midHeight - 2d;
			int crosshalfwidth = width/10;
			RenderUtils.drawRect(-crosshalfwidth, -crosshalfheight, crosshalfwidth, crosshalfheight, 0xFFFFFFFF);
			rotate(-90, 0, 0, 1);
			RenderUtils.drawRect(-crosshalfwidth, -crosshalfheight, crosshalfwidth, crosshalfheight, 0xFFFFFFFF);
		}
		else if (type == 1) {
			mc.getTextureManager().bindTexture(cogwheel);
			enableBlend();
	        tryBlendFuncSeparate(770, 771, 1, 0);
	        blendFunc(770, 771);
			RenderUtils.drawTexturedRect((int)this.zLevel, xPosition + 1, yPosition + 1, width - 2, height - 2);
			disableBlend();
		}
		popMatrix();
		
		if (isMouseOver(mouseX, mouseY))
		{
			RenderUtils.drawRoundedRect(xPosition, yPosition, width, height, 0x88FFFFFF, 0.15d * (double)height, 15);
			if (this.info != null && this.info.length() > 0)
			{
				if (this.info2 != null && this.info2.length() > 0)
					RenderUtils.drawToolTip(mouseX - 1, mouseY + 1, (int)this.zLevel, this.info, this.info2);
				else
					RenderUtils.drawToolTip(mouseX - 1, mouseY + 1, (int)this.zLevel, this.info);
			}
		}
			
	}
}
