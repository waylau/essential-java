/**
 * 
 */
package com.waylau.essentialjava.operator.arithmeticdemo;

/**
 * @author <a href="http://www.waylau.com">waylau.com</a>
 * @date 2015年12月26日
 */
class ArithmeticDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int result = 1 + 2;
		// result is now 3
		System.out.println("1 + 2 = " + result);
		int original_result = result;

		result = result - 1;
		// result is now 2
		System.out.println(original_result + " - 1 = " + result);
		original_result = result;

		result = result * 2;
		// result is now 4
		System.out.println(original_result + " * 2 = " + result);
		original_result = result;

		result = result / 2;
		// result is now 2
		System.out.println(original_result + " / 2 = " + result);
		original_result = result;

		result = result + 8;
		// result is now 10
		System.out.println(original_result + " + 8 = " + result);
		original_result = result;

		result = result % 7;
		// result is now 3
		System.out.println(original_result + " % 7 = " + result);
	}

}
