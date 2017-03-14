/**
 * 
 */
package com.waylau.essentialjava.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

@Documented
@Retention(RUNTIME)
/**
 * 
 * 
 * @since 1.0.0 2017年3月14日
 * @author <a href="https://waylau.com">Way Lau</a> 
 */
public @interface MyAnnotation {
	String company() default "waylau.com";
}
