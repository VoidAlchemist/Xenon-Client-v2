package com.xenon.util;

import com.xenon.util.readability.Static;

@Static
public class MathUtil {

	/**
	 * 
	 * @param value
	 * @param goal
	 * @param range MUST be positive
	 * @return
	 */
	public static boolean isInRange(float value, float goal, float range) {
		return goal - range < value && value < goal + range;
	}
}
