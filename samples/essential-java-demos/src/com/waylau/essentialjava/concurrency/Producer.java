/**
 * 
 */
package com.waylau.essentialjava.concurrency;

import java.util.Random;

/**
 * @author <a href="http://www.waylau.com">waylau.com</a>
 * @date 2016年1月22日
 */
public class Producer implements Runnable {
	private Drop drop;

	public Producer(Drop drop) {
		this.drop = drop;
	}

	public void run() {
		String importantInfo[] = { "Mares eat oats", "Does eat oats", "Little lambs eat ivy",
				"A kid will eat ivy too" };
		Random random = new Random();

		for (int i = 0; i < importantInfo.length; i++) {
			drop.put(importantInfo[i]);
			try {
				Thread.sleep(random.nextInt(5000));
			} catch (InterruptedException e) {
			}
		}
		drop.put("DONE");
	}
}