package com.xenon.client.gui.modulesconf.components;

public abstract class OnOff extends Component{

	protected boolean value;
	protected final boolean autonomous;
	
	public OnOff(boolean autonomous, boolean initialValue) {
		super(30, 15);
		value = initialValue;
		this.autonomous = autonomous;
	}

	
	@Override
	public final void paint(Graphics g) {
		super.paint(g);
		g.drawRect(width, height, 0x99352323);
		int quartw = width/4;
		if (value) {
			g.drawRect(width, height, 0x3010FF10);
			g.drawGradientRect(quartw, height, 0x80404040, 0x80202020);
		}else {
			g.drawRect(width, height, 0x30FF1010);
			g.drawGradientRect(x + width - quartw, y, quartw, height, 0x80404040, 0x80202020);
		}
		if (isMouseOver(g.mouseX, g.mouseY))
			g.drawRect(width, height, 0x30FFFFFF);
		
		if (autonomous)
			g.translatebrush(0, height);
	}
	
	@Override
	public void clickListener(int mouseX, int mouseY) {
		value = !value;
	}

}
