/**
 * 
 */
package com.waylau.essentialjava.array.arraydemo;

/**
 * @author <a href="http://www.waylau.com">waylau.com</a>
 * @date 2015年12月26日
 */
class ArrayCopyDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		char[] copyFrom = { 'd', 'e', 'c', 'a', 'f', 'f', 'e', 'i', 'n', 'a', 't', 'e', 'd' };
		char[] copyTo = new char[7];

		System.arraycopy(copyFrom, 2, copyTo, 0, 7);
		System.out.println(new String(copyTo));
	}
}
