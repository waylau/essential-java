/**
 * 
 */
package com.waylau.essentialjava.array.arraydemo;

/**
 * @author <a href="http://www.waylau.com">waylau.com</a>
 * @date 2015年12月26日
 */
class MultiDimArrayDemo {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[][] names = { { "Mr. ", "Mrs. ", "Ms. " }, { "Smith", "Jones" } };
		// Mr. Smith
		System.out.println(names[0][0] + names[1][0]);
		// Ms. Jones
		System.out.println(names[0][2] + names[1][1]);
	}
}