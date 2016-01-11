/**
 * 
 */
package com.waylau.essentialjava.generic.boxdemo;

/**
 * @author <a href="http://www.waylau.com">waylau.com</a>
 * @date 2015年12月29日
 */
public class Box {
	private Object object;

	public void set(Object object) {
		this.object = object;
	}

	public Object get() {
		return object;
	}
}
