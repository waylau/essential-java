/**
 * 
 */
package com.waylau.essentialjava.operator.conditionaldemo;

/**
 * @author <a href="http://www.waylau.com">waylau.com</a>
 * @date 2015年12月27日
 */
class ConditionalDemo1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int value1 = 1;
		int value2 = 2;
		if ((value1 == 1) && (value2 == 2))
			System.out.println("value1 is 1 AND value2 is 2");
		if ((value1 == 1) || (value2 == 1))
			System.out.println("value1 is 1 OR value2 is 1");
	}

}
