/**
 * 
 */
package com.waylau.essentialjava.generic.generic.boxdemo;

/**
 * @author <a href="http://www.waylau.com">waylau.com</a>
 * @date 2015年12月29日
 */
public class Box<T> {
	// T stands for "Type"
	private T t;

	public void set(T t) {
		this.t = t;
	}

	public T get() {
		return t;
	}

}
