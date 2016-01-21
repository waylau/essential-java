/**
 * 
 */
package com.waylau.essentialjava.concurrency;

/**
 * @author <a href="http://www.waylau.com">waylau.com</a>
 * @date 2016年1月21日
 */
public class HelloRunnable implements Runnable {
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		System.out.println("Hello from a thread!");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        (new Thread(new HelloRunnable())).start();
	}
}
