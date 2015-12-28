/**
 * 
 */
package com.waylau.essentialjava.flow.fordemo;

/**
 * @author <a href="http://www.waylau.com">waylau.com</a>
 * @date 2015年12月28日
 */
class EnhancedForDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[] numbers = 
            {1,2,3,4,5,6,7,8,9,10};
        for (int item : numbers) {
            System.out.println("Count is: " + item);
        }
	}

}
