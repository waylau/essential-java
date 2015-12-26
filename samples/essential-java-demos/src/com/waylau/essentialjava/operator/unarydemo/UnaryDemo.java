/**
 * 
 */
package com.waylau.essentialjava.operator.unarydemo;

/**
 * @author <a href="http://www.waylau.com">waylau.com</a>
 * @date 2015年12月26日
 */
class UnaryDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int result = +1;
        // result is now 1
        System.out.println(result);

        result--;
        // result is now 0
        System.out.println(result);

        result++;
        // result is now 1
        System.out.println(result);

        result = -result;
        // result is now -1
        System.out.println(result);

        boolean success = false;
        // false
        System.out.println(success);
        // true
        System.out.println(!success);
	}

}
