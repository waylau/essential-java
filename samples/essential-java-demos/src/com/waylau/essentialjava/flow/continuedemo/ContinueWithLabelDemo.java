/**
 * 
 */
package com.waylau.essentialjava.flow.continuedemo;

/**
 * @author <a href="http://www.waylau.com">waylau.com</a>
 * @date 2015年12月28日
 */
class ContinueWithLabelDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String searchMe = "Look for a substring in me";
		String substring = "sub";
		boolean foundIt = false;

		int max = searchMe.length() - substring.length();

		test: for (int i = 0; i <= max; i++) {
			int n = substring.length();
			int j = i;
			int k = 0;
			while (n-- != 0) {
				if (searchMe.charAt(j++) != substring.charAt(k++)) {
					continue test;
				}
			}
			foundIt = true;
			break test;
		}
		System.out.println(foundIt ? "Found it" : "Didn't find it");
	}

}
