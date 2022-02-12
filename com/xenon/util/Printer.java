package com.xenon.util;
/**
 * False Logger, ngl it just println things.
 * @author VoidAlchemist
 *
 */
public class Printer {

	public void info(Object... args) {
		for (Object arg : args)
			System.out.println("[XenonClient] "+arg);
	}
	
	public void error(Object... args) {
		for (Object arg : args)
			System.err.println("[Xenon Client] "+arg);
	}
}
