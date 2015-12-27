/**
 * 
 */
package com.waylau.essentialjava.expression.blockdemo;

/**
 * @author <a href="http://www.waylau.com">waylau.com</a>
 * @date 2015年12月27日
 */
class BlockDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean condition = true;
		if (condition) { // begin block 1
			System.out.println("Condition is true.");
		} // end block one
		else { // begin block 2
			System.out.println("Condition is false.");
		} // end block 2
	}
}
