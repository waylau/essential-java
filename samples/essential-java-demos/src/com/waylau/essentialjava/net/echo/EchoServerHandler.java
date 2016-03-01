/**
 * 
 */
package com.waylau.essentialjava.net.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * EchoServer Handler
 * @author <a href="http://waylau.com">waylau.com</a>
 * @date 2016年3月1日
 */
public class EchoServerHandler implements Runnable {
	private Socket clientSocket;

	public EchoServerHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				out.println(inputLine);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
