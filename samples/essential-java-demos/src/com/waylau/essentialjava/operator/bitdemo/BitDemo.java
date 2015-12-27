/**
 * 
 */
package com.waylau.essentialjava.operator.bitdemo;

/**
 * @author <a href="http://www.waylau.com">waylau.com</a>
 * @date 2015年12月27日
 */
class BitDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int bitmask = 0x000F;
		int val = 0x2222;
		// prints "2"
		System.out.println(val & bitmask);
	}
}
