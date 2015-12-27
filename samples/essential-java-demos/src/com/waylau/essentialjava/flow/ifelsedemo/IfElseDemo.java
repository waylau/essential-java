/**
 * 
 */
package com.waylau.essentialjava.flow.ifelsedemo;

/**
 * @author <a href="http://www.waylau.com">waylau.com</a>
 * @date 2015年12月27日
 */
class IfElseDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int testscore = 76;
		char grade;

		if (testscore >= 90) {
			grade = 'A';
		} else if (testscore >= 80) {
			grade = 'B';
		} else if (testscore >= 70) {
			grade = 'C';
		} else if (testscore >= 60) {
			grade = 'D';
		} else {
			grade = 'F';
		}
		System.out.println("Grade = " + grade);
	}
}
