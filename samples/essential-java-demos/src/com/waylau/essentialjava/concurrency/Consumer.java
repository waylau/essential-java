/**
 * 
 */
package com.waylau.essentialjava.concurrency;

import java.util.Random;

/**
 * @author <a href="http://www.waylau.com">waylau.com</a>
 * @date 2016年1月22日
 */
public class Consumer implements Runnable {
	private Drop drop;

	public Consumer(Drop drop) {
		this.drop = drop;
	}

	public void run() {
		Random random = new Random();
		for (String message = drop.take(); !message.equals("DONE"); message = drop.take()) {
			System.out.format("MESSAGE RECEIVED: %s%n", message);
			try {
				Thread.sleep(random.nextInt(5000));
			} catch (InterruptedException e) {
			}
		}
	}
}