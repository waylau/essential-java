/**
 * 
 */
package com.waylau.essentialjava.net.echo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * EchoServer(MultiThread Model)
 * @author <a href="http://waylau.com">waylau.com</a>
 * @date 2016年3月1日
 */
public class MultiThreadEchoServer {
	public static void main(String[] args) throws IOException {

		if (args.length != 1) {
			System.err.println("Usage: java EchoServer <port number>");
			System.exit(1);
		}

		int portNumber = Integer.parseInt(args[0]);
		Socket clientSocket = null;
		try (ServerSocket serverSocket = new ServerSocket(portNumber);) {
			while (true) {
				clientSocket = serverSocket.accept();
				
				// MultiThread
				new Thread(new EchoServerHandler(clientSocket)).start();
			}
		} catch (IOException e) {
			System.out.println(
					"Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		}
	}
}