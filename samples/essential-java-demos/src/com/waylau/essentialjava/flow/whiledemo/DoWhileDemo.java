/**
 * 
 */
package com.waylau.essentialjava.flow.whiledemo;

/**
 * @author <a href="http://www.waylau.com">waylau.com</a>
 * @date 2015年12月28日
 */
class DoWhileDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int count = 1;
		do {
			System.out.println("Count is: " + count);
			count++;
		} while (count < 11);
	}

}
