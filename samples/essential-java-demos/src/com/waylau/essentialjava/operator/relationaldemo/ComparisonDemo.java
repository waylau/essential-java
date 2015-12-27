/**
 * 
 */
package com.waylau.essentialjava.operator.relationaldemo;

/**
 * @author <a href="http://www.waylau.com">waylau.com</a>
 * @date 2015年12月27日
 */
class ComparisonDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int value1 = 1;
		int value2 = 2;
		if (value1 == value2)
			System.out.println("value1 == value2");
		if (value1 != value2)
			System.out.println("value1 != value2");
		if (value1 > value2)
			System.out.println("value1 > value2");
		if (value1 < value2)
			System.out.println("value1 < value2");
		if (value1 <= value2)
			System.out.println("value1 <= value2");
	}

}
