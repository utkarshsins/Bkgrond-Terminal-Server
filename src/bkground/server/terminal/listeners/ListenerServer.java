package bkground.server.terminal.listeners;

import bkground.server.terminal.ServerInfo;

/**
 * Thread that will listen for incoming connections
 */
public class ListenerServer extends Thread {

	private static final String THREAD_NAME = "THREAD_SERVER";

	private int port;

	public ListenerServer() {
		super(new ThreadRunnable());
		setName(THREAD_NAME);

		this.port = ServerInfo.SERVER_INFO_DEFAULT_PORT;
	}

	public ListenerServer(int port) {
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

	@Override
	public synchronized void start() {
		super.start();
	}

	private static class ThreadRunnable implements Runnable {

		@Override
		public void run() {
		}
	}

}
