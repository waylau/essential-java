/**
 * 
 */
package com.waylau.essentialjava.flow.continuedemo;

/**
 * @author <a href="http://www.waylau.com">waylau.com</a>
 * @date 2015年12月28日
 */
class ContinueDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String searchMe = "peter piper picked a " + "peck of pickled peppers";
        int max = searchMe.length();
        int numPs = 0;

        for (int i = 0; i < max; i++) {
            // interested only in p's
            if (searchMe.charAt(i) != 'p')
                continue;

            // process p's
            numPs++;
        }
        System.out.println("Found " + numPs + " p's in the string.");
	}

}
