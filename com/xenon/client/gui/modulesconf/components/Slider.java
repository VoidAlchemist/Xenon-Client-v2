package com.xenon.client.gui.modulesconf.components;

import com.xenon.util.RenderUtils;

import net.minecraft.util.MathHelper;

public abstract class Slider extends Component {

	protected boolean dragging;
	protected float value;
	protected String text = "";
	protected final boolean autonomous;
	
	public Slider(boolean autonomous, int width, int height) {
		super(width, height);
		this.autonomous = autonomous;
	}
	public Slider(boolean autonomous, int width) {
		this(autonomous, width, 15);
	}
	
	/**
	 * Sets the initial value of the slider as well of its display text.
	 * @param value the initial integer value
	 * @param max the maximum of the value. used for the normalization of value.
	 * @return the object for one line instantiation-initialization.
	 */
	public Slider setInitialValue(int value, int max) {
		this.value = (float)value / (float)max;
		text = Integer.toString(value);
		return this;
	}
	/**
	 * Same as {@link #setInitialValue(int, int)} but with value already normalized and displayed as a percentage.<br>
	 * Equivalent to <br><code>
	 * setInitialValue((int)(value * 100f), 100);
	 * </code>
	 * @param value
	 * @return the object for one line instantiation-initialization.
	 */
	public Slider setInitialValuePercent(float value) {
		this.value = value;
		text = Integer.toString((int)(value * 100f));
		return this;
	}
	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawRect(width, height, 0x99352323);
		g.drawGradientRect(x + (int)(value * (float)(width - 4)), y, 4, height, 0xFAF0F0F0, 0xFF404040);
		g.drawCenteredString(text, x + (width >> 1), y - 5 + (height >> 1),0xFFFFFFFF);
		
		this.mouseDragged(g.mouseX, g.mouseY);
		if (autonomous)
			g.translatebrush(0, height);
	}

	@Override
	public final void clickListener(int mouseX, int mouseY) {
		this.value = MathHelper.clamp_float((float)(mouseX - this.x - 2)/(float)(this.width - 4), 0f, 1f);
		text = onSlide();
		dragging = true;
	}

	/**
	 * Called when the slider's position changes.
	 * @return the new text to be displayed on the slider.
	 */
	public abstract String onSlide();
	
	/**
	 * Called when the mouse is released, to stop dragging the slider.
	 */
	public final void mouseReleased() {
		dragging = false;
	}
	/**
	 * Called when the mouse is dragged.
	 * @param mouseX
	 * @param mouseY
	 */
	public final void mouseDragged(int mouseX, int mouseY) {
		if (dragging) {
			this.value = MathHelper.clamp_float((float)(mouseX - this.x - 2)/(float)(this.width - 4), 0f, 1f);
			text = onSlide();
		}
	}
}
