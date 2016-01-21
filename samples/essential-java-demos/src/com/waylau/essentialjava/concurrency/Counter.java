/**
 * 
 */
package com.waylau.essentialjava.concurrency;

/**
 * @author <a href="http://www.waylau.com">waylau.com</a>
 * @date 2016年1月21日
 */
public class Counter {
	private int c = 0;

	public void increment() {
		c++;
	}

	public void decrement() {
		c--;
	}

	public int value() {
		return c;
	}
}
