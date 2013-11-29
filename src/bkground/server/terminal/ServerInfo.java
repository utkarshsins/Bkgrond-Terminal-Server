package bkground.server.terminal;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.aalto.AsyncXMLInputFactory;
import com.fasterxml.aalto.stax.InputFactoryImpl;

import bkground.server.terminal.listeners.DataForwardingBackThread;
import bkground.server.terminal.listeners.DataForwardingThreadFactory;
import bkground.server.terminal.listeners.ExtractorThreadFactory;
import bkground.server.terminal.listeners.ListenerServer;
import bkground.server.terminal.listeners.ListenerSocket;

public class ServerInfo {

	public static final int SERVER_INFO_DEFAULT_PORT = 4040;

	public static final String DF_SERVER_DEFAULT_ADDRESS = "127.0.0.1";

	public static final int DF_SERVER_DEFAULT_PORT = 4041;

	public ConcurrentHashMap<Integer, ListenerSocket> listenerSocketMap;

	public ConcurrentHashMap<SocketChannel, ListenerSocket> socketListenersMap;
	
	public ConcurrentHashMap<Integer, SocketInfo> userSocketInfoMap; 

	public ListenerServer listenerServer;

	public ExecutorService streamProcessorPool;

	public ExecutorService dataForwardingPool;

	public AsyncXMLInputFactory xmlInputFactory;

	public static int id;

	public SocketChannel terminalServerSocket;

	public DataForwardingBackThread terminalBackThread;

	public ServerInfo() throws IOException {
		this.listenerSocketMap = new ConcurrentHashMap<Integer, ListenerSocket>();
		this.socketListenersMap = new ConcurrentHashMap<SocketChannel, ListenerSocket>();
		this.userSocketInfoMap = new ConcurrentHashMap<Integer, SocketInfo>();
		this.listenerServer = new ListenerServer(listenerSocketMap, this);
		this.streamProcessorPool = Executors.newFixedThreadPool(5,
				new ExtractorThreadFactory());
		DataForwardingThreadFactory threadFactory = new DataForwardingThreadFactory(
				this);
		this.dataForwardingPool = Executors
				.newSingleThreadExecutor(threadFactory);
		this.terminalBackThread = new DataForwardingBackThread(this, 0);
		this.terminalBackThread.start();
		this.xmlInputFactory = new InputFactoryImpl();
		this.terminalServerSocket = null;
	}

	public void init() throws IOException {

		if (listenerSocketMap.size() == 0) {
			try {
				setThreadCount(Defaults.getDefaultListenerThreadCount());
			} catch (IllegalAccessException e) {
				System.out.println("Should not have happened. Halting.");
				e.printStackTrace();
				System.exit(-1);
			}
		}

		for (ListenerSocket listenerSocket : listenerSocketMap.values()) {
			listenerSocket.start();
		}

		// Must start after listenerSockets
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

	/**
	 * Set the number of listener socket threads.
	 * 
	 * @return {@link ServerInfo} The current instance of the ServerInfo for
	 *         chaining
	 * @throws IllegalAccessException
	 *             If thread count has been set once
	 */
	public ServerInfo setThreadCount(int count) throws IllegalAccessException {
		if (listenerSocketMap.size() > 0) {
			throw new IllegalAccessException();
		}

		for (int i = 0; i < count; i++) {
			try {
				ListenerSocket listenerSocket = new ListenerSocket(this, i);
				listenerSocketMap.put(i, listenerSocket);
			} catch (IOException e) {
				count--;
			}
		}

		System.out.println("Started " + count + " "
				+ ListenerSocket.THREAD_NAME);

		return this;
	}

	public ServerInfo setID(int id) {

		ServerInfo.id = id;
		return this;

	}

}
