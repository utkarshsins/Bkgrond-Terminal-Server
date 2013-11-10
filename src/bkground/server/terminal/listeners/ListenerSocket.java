package bkground.server.terminal.listeners;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import bkground.server.terminal.ServerInfo;

public class ListenerSocket extends Thread {

	public static final String THREAD_NAME = "THREAD_SOCKET";

	/**
	 * This is the selector that sockets for this data listener pool will wait
	 * on. Any incoming data from a socket will continue the blocked loop in
	 * {@link #run() run()}
	 */
	private Selector selector;

	private ServerInfo serverInfo;

	/**
	 * TODO Use this hash map to store information about socket channels that
	 * are registered on this server listener thread. Replace Integer with
	 * whatever key you want to use (possibly user-id), and replace
	 * SocketChannel with an object of a new class that contains SocketChannel
	 * as well as other relevant information needed about the Socket (like the
	 * affiliated user-details, status-of-socket, etc).
	 */
	// private ConcurrentHashMap<Integer, SocketChannel> socketChannelsMap;

	/**
	 * Constructor that will create a new selector on which sockets added to
	 * this thread through {@link #addSocketChannel(SocketChannel)} will be
	 * registered to listen for any incoming data.
	 * 
	 * @param i
	 * @throws IOException
	 */
	public ListenerSocket(ServerInfo serverInfo, int i) throws IOException {
		super();
		setName(THREAD_NAME + "_" + i);

		this.serverInfo = serverInfo;

		this.selector = Selector.open();
	}

	/**
	 * Get the selector that this thread will block on.
	 * 
	 * @return {@link Selector} The selector that this thread blocks on (Null if
	 *         no selector is set)
	 */
	public Selector getSelector() {
		return selector;
	}

	/**
	 * This is the executor function of the socket selector loop. The function
	 * blocks on select() function and waits until there are some sockets with
	 * data ready to be read
	 */
	@Override
	public void run() {
		int selected = 0;
		while (selector.isOpen()) {
			try {

				selected = selector.select();
				System.out.println("Selected " + selected);

				// TODO
				// Code to process data from selector's selected keys
				Set<SelectionKey> readyKeys = selector.selectedKeys();
				List<Future<Boolean>> processedFutures = new ArrayList<Future<Boolean>>();

				for (SelectionKey readyKey : readyKeys) {
					SelectableChannel channel = readyKey.channel();
					processedFutures.add(serverInfo.socketProcessorPool
							.submit(new RegisterReadTask(channel)));

				}

				int counter = 0;
				for (Future<Boolean> future : processedFutures) {
					try {
						boolean futureResult = future.get();
						System.out.println("Successful data recovery from "
								+ counter + " channel");
					} catch (InterruptedException | ExecutionException e) {
						System.err.println("Something went wrong when trying "
								+ "to wait for registering an incoming "
								+ "data-request. Check the processing "
								+ "in RegisterReadTask class.");
						e.printStackTrace();
					}
				}

			} catch (IOException e) {
				System.err.println("select() failed for " + getName());
				e.printStackTrace();
			}
		}
	}

	@Override
	public void interrupt() {
		try {
			selector.close();
		} catch (IOException e) {
			System.err.println("Error closing selector for " + getName());
			e.printStackTrace();
		}
		super.interrupt();
	}

	/**
	 * This function will register a new incoming socket request with this
	 * data-listener thread. Any data that will come on this thread will then be
	 * forwarded for extraction from the NIO APIs.
	 * 
	 * @param
	 * @return boolean true if registration was successful
	 * @throws ClosedChannelException
	 * @throws IOException
	 */
	public synchronized boolean addSocketChannel(SocketChannel socketChannel)
			throws ClosedChannelException, IOException {

		socketChannel.configureBlocking(false);
		socketChannel.register(selector, SelectionKey.OP_READ);

		System.out.println("Registered socket to " + getName());

		return true;

	}

	private class RegisterReadTask implements Callable<Boolean> {
		SelectableChannel channel;

		public RegisterReadTask(SelectableChannel channel) {
			this.channel = channel;
		}

		@Override
		public Boolean call() throws Exception {

			// TODO
			// Extract data from socket into a data structure here and return
			// true. Socket should be reset to not contain any available data
			// otherwise select will go into an infinite loop I think. Whatever
			// you do, reference the NIO APIs properly. They will explain stuff.
			System.out.println("Supposed to extract data from channel "
					+ channel + " here.");

			return true;
		}

	}

}
