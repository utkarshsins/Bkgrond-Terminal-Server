package bkground.server.terminal;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import bkground.server.terminal.listeners.ListenerServer;
import bkground.server.terminal.listeners.ListenerSocket;

public class ServerInfo {

	public static final int SERVER_INFO_DEFAULT_PORT = 4040;

	public ConcurrentHashMap<ListenerSocket, ListenerSocket> listenerSockets;

	public ListenerServer listenerServer;

	public ServerInfo() {
		this.listenerSockets = new ConcurrentHashMap<ListenerSocket, ListenerSocket>();
		this.listenerServer = new ListenerServer();
	}

	public void init() throws IOException {
		listenerServer.startServer();

		if (listenerSockets.size() == 0) {
			try {
				setThreadCount(Defaults.getDefaultThreadCount());
			} catch (IllegalAccessException e) {
				System.out.println("Should not have happened. Halting.");
				e.printStackTrace();
				System.exit(-1);
			}
		}

		for (ListenerSocket listenerSocket : listenerSockets.values()) {
			listenerSocket.start();
		}
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

	/**
	 * Set the number of listener socket threads.
	 * 
	 * @return {@link ServerInfo} The current instance of the ServerInfo for
	 *         chaining
	 * @throws IllegalAccessException
	 *             If thread count has been set once
	 */
	public ServerInfo setThreadCount(int count) throws IllegalAccessException {
		if (listenerSockets.size() > 0) {
			throw new IllegalAccessException();
		}

		for (int i = 0; i < count; i++) {
			try {
				ListenerSocket listenerSocket = new ListenerSocket(i);
				listenerSockets.put(listenerSocket, listenerSocket);
			} catch (IOException e) {
				count--;
			}
		}

		System.out.println("Started " + count + " "
				+ ListenerSocket.THREAD_NAME);

		return this;
	}

}
