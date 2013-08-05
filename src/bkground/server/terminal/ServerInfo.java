package bkground.server.terminal;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import bkground.server.terminal.listeners.ListenerServer;

public class ServerInfo {

	public static final int SERVER_INFO_DEFAULT_PORT = 4040;

	public int threadCount;

	public ConcurrentHashMap<Thread, Thread> listenerSockets;

	public ListenerServer listenerServer;

	public ServerInfo() throws IOException {
		this.threadCount = Runtime.getRuntime().availableProcessors();
		this.listenerSockets = new ConcurrentHashMap<Thread, Thread>();
		this.listenerServer = new ListenerServer();
	}

	public void init() throws IOException {
		listenerServer.startServer();
	}

	/**
	 * Set the port on which the server will listen for new sockets.
	 * 
	 * @return {@link ServerInfo} The current instance of the ServerInfo for
	 *         chaining
	 * @throws IllegalAccessException
	 *             If listener server has already started
	 */
	public ServerInfo setPort(int port) throws IllegalAccessException {
		listenerServer.setPort(port);
		return this;
	}
}
