package com.xenon.modules;

public class Modules {
	
	private static Modules instance;
	public static Modules getInstance() {
		if (instance == null)
			instance = new Modules();
		return instance;
	}
	private Modules() {
		
		
	}
	
	public void init() {
		
	}
	
	public void shutdown() {
		
	}
	
}
