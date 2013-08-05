package bkground.server.terminal.listeners;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import bkground.server.terminal.ServerInfo;

/**
 * Thread that will listen for incoming connections
 */
public class ListenerServer extends Thread {

	private static final String THREAD_NAME = "THREAD_SERVER";

	private int port;

	private ServerSocketChannel ssc;

	public ListenerServer() throws IOException {
		super();
		setName(THREAD_NAME);

		this.port = ServerInfo.SERVER_INFO_DEFAULT_PORT;
	}

	public ListenerServer(int port) throws IOException {
		this();
		this.port = port;
	}

	/**
	 * Get the port on which the server is listening for new sockets.
	 * 
	 * @return int Port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Set the port on which the server will listen for new sockets.
	 * 
	 * @return {@link ListenerServer} The current instance of the ListenerServer
	 *         for chaining
	 * @throws IllegalAccessException
	 *             If listener server has already started
	 */
	public ListenerServer setPort(int port) throws IllegalAccessException {
		if (isAlive()) {
			throw new IllegalAccessException();
		}
		this.port = port;
		return this;
	}

	/**
	 * Start the server in its separate thread.
	 * 
	 * @throws IOException
	 */
	public synchronized void startServer() throws IOException {
		ssc = ServerSocketChannel.open();
		ssc.configureBlocking(true);
		ssc.socket().bind(new InetSocketAddress(port));

		System.out.println("Server started on port " + port);
		System.out.println();

		super.start();
	}

	/**
	 * @deprecated call startServer() instead
	 */
	@Override
	public synchronized void start() {
		try {
			startServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			try {

				SocketChannel socketChannel = ssc.accept();

				System.out.println("Incoming socket from "
						+ socketChannel.getRemoteAddress());

				// TODO
				// Handle socketChannel

			} catch (AsynchronousCloseException e) {
				System.err.println("Asynchronous close on server listener");
				System.err.println("Stopping Server Listener!!");
				break;
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Stopping Server Listener!!");
				break;
			}
		}
	}

	@Override
	public void interrupt() {
		if (ssc != null) {
			try {
				ssc.close();
			} catch (IOException e) {
				System.err.println("Error closing listener on interrupt");
				e.printStackTrace();
			}
		}
		super.interrupt();
	}

}
