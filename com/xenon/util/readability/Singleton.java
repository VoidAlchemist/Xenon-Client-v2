package com.xenon.util.readability;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * Indicates that a class possesses only one instance stored in a static field.
 * @author VoidAlchemist
 * @since v2.0
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Singleton {}