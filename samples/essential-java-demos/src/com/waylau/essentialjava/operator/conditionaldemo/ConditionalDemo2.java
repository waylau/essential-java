/**
 * 
 */
package com.waylau.essentialjava.operator.conditionaldemo;

/**
 * @author <a href="http://www.waylau.com">waylau.com</a>
 * @date 2015年12月27日
 */
class ConditionalDemo2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int value1 = 1;
		int value2 = 2;
		int result;
		boolean someCondition = true;
		result = someCondition ? value1 : value2;

		System.out.println(result);
	}

}
