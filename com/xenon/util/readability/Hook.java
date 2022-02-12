package com.xenon.util.readability;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * Type hint for readers about where the method/class/field is used.
 * Mainly used for outside package calls (net.minecraft per example).
 * Note that this mod's code isn't event-driven; we try to avoid reflection as much as possible.
 * @author VoidAlchemist
 * @since v2.0
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Hook {
	String value();
}
