/**
 * 
 */
package com.waylau.essentialjava.expression.blockdemo;

/**
 * 在一个异常处理程序中处理多个类型的异常.
 * 
 * @author <a href="https://waylau.com">Way Lau</a>
 * @date 2017年7月26日
 */
public class ShareExceptionHandler {
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		
		// not share exception handler
		int[] intArray = new int[3];
		try {
			for (int i = 0; i <= intArray.length; i++) {
				intArray[i] = i;
				System.out.println("intArray[" + i + "] = " + intArray[i]);
				System.out.println("intArray[" + i + "]模 " + (i - 2) + "的值:  " + intArray[i] % (i - 2));
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("异常信息：" + e);
		} catch (ArithmeticException e) {
			System.out.println("异常信息：" + e);
		}
		System.out.println("程序正常结束。");
		
		// share exception handler
		intArray = new int[3];
		try {
			for (int i = 0; i <= intArray.length; i++) {
				intArray[i] = i;
				System.out.println("intArray[" + i + "] = " + intArray[i]);
				System.out.println("intArray[" + i + "]模 " + (i - 2) + "的值:  " + intArray[i] % (i - 2));
			}
		} catch (ArrayIndexOutOfBoundsException | ArithmeticException e) {
			System.out.println("异常信息：" + e);
		}  
		System.out.println("程序正常结束。");
	}

}
