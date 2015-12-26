/**
 * 
 */
package com.waylau.essentialjava.array.arraydemo;

/**
 * @author <a href="http://www.waylau.com">waylau.com</a>
 * @date 2015年12月26日
 */
class ArrayCopyOfDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		char[] copyFrom = { 'd', 'e', 'c', 'a', 'f', 'f', 'e', 'i', 'n', 'a', 't', 'e', 'd' };

		char[] copyTo = java.util.Arrays.copyOfRange(copyFrom, 2, 9);

		System.out.println(new String(copyTo));
	}
}
