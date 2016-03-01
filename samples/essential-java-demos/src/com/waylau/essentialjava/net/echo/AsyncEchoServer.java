package com.waylau.essentialjava.net.echo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
/**
 * Asynchronous Echo Server(Asynchronous IO Model)
 * @author <a href="http://waylau.com">waylau.com</a>
 * @date 2016年3月1日
 */
public class AsyncEchoServer {

	public static int DEFAULT_PORT = 7;

	public static void main(String[] args) throws IOException {
		int port;

		try {
			port = Integer.parseInt(args[0]);
		} catch (RuntimeException ex) {
			port = DEFAULT_PORT;
		}

		ExecutorService taskExecutor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());
		// create asynchronous server socket channel bound to the default group
		try (AsynchronousServerSocketChannel asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open()) {
			if (asynchronousServerSocketChannel.isOpen()) {
				// set some options
				asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
				asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
				// bind the server socket channel to local address
				asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
				// display a waiting message while ... waiting clients
				System.out.println("Waiting for connections ...");
				while (true) {
					Future<AsynchronousSocketChannel> asynchronousSocketChannelFuture = asynchronousServerSocketChannel
							.accept();
					try {
						final AsynchronousSocketChannel asynchronousSocketChannel = asynchronousSocketChannelFuture
								.get();
						Callable<String> worker = new Callable<String>() {
							@Override
							public String call() throws Exception {
								String host = asynchronousSocketChannel.getRemoteAddress().toString();
								System.out.println("Incoming connection from: " + host);
								final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
								// transmitting data
								while (asynchronousSocketChannel.read(buffer).get() != -1) {
									buffer.flip();
									asynchronousSocketChannel.write(buffer).get();
									if (buffer.hasRemaining()) {
										buffer.compact();
									} else {
										buffer.clear();
									}
								}
								asynchronousSocketChannel.close();
								System.out.println(host + " was successfully served!");
								return host;
							}
						};
						taskExecutor.submit(worker);
					} catch (InterruptedException | ExecutionException ex) {
						System.err.println(ex);
						System.err.println("\n Server is shutting down ...");
						// this will make the executor accept no new threads
						// and finish all existing threads in the queue
						taskExecutor.shutdown();
						// wait until all threads are finished
						while (!taskExecutor.isTerminated()) {
						}
						break;
					}
				}
			} else {
				System.out.println("The asynchronous server-socket channel cannot be opened!");
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
}