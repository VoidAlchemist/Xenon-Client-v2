package com.xenon.modules.api;

import java.util.LinkedList;
import java.util.Queue;

import org.lwjgl.input.Mouse;

public class CPSCounter {

	private boolean Lwaspressed, Rwaspressed;
	private Queue<Long> Lclicks, Rclicks;
	
	public CPSCounter() {
		Lclicks = new LinkedList<Long>();
		Rclicks = new LinkedList<Long>();
	}
	
	public void call(long time) {
		boolean pressed = Mouse.isButtonDown(0);
		
		if (pressed != Lwaspressed) {
			Lwaspressed = pressed;
			if (pressed)	this.Lclicks.add(time + 1000L);
		}
		
		pressed = Mouse.isButtonDown(1);
		
		if (pressed != Rwaspressed) {
			Rwaspressed = pressed;
			if (pressed)	this.Rclicks.add(time + 1000L);
		}
	}
	
	public int getLCPS(long currentTime) {
		while (!Lclicks.isEmpty() && Lclicks.peek() < currentTime)
			Lclicks.remove();
		
		return Lclicks.size();
	}
	public int getRCPS(long currentTime) {
		while(!Rclicks.isEmpty() && Rclicks.peek() < currentTime)
			Rclicks.remove();
		
		return Rclicks.size();
	}
	
}
