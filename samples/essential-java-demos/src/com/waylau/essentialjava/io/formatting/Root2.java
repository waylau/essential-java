/**
 * 
 */
package com.waylau.essentialjava.io.formatting;

/**
 * @author <a href="http://www.waylau.com">waylau.com</a>
 * @date 2016年1月18日
 */
public class Root2 {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int i = 2;
        double r = Math.sqrt(i);
        
        System.out.format("The square root of %d is %f.%n", i, r);
	}
}
