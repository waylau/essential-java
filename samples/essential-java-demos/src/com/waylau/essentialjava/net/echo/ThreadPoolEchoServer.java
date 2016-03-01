/**
 * 
 */
package com.waylau.essentialjava.net.echo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * EchoServer(ThreadPool Model)
 * @author <a href="http://waylau.com">waylau.com</a>
 * @date 2016年3月1日
 */
public class ThreadPoolEchoServer {
	public static int DEFAULT_PORT = 7;

	public static void main(String[] args) throws IOException {

		int port;

		try {
			port = Integer.parseInt(args[0]);
		} catch (RuntimeException ex) {
			port = DEFAULT_PORT;
		}
		ExecutorService threadPool = Executors.newFixedThreadPool(5);
		Socket clientSocket = null;
		try (ServerSocket serverSocket = new ServerSocket(port);) {
			while (true) {
				clientSocket = serverSocket.accept();
				
				// Thread Pool
				threadPool.submit(new Thread(new EchoServerHandler(clientSocket)));
			}
		} catch (IOException e) {
			System.out.println(
					"Exception caught when trying to listen on port " + port + " or listening for a connection");
			System.out.println(e.getMessage());
		}
	}
}
