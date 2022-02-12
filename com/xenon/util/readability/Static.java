package com.xenon.util.readability;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Indicates that a class isn't mean to be instantiate at all. Possibly initialized with an <code>init</code> method though.
 * @author VoidAlchemist
 * @since v2.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Static {}