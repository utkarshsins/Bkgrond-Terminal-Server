package bkground.server.terminal.listeners;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

public class ListenerSocket extends Thread {

	public static final String THREAD_NAME = "THREAD_SOCKET";

	private Selector selector;

	private HashMap<SelectionKey, SocketChannel> keyChannelMap;

	public ListenerSocket(int i) throws IOException {
		super();
		setName(THREAD_NAME + "_" + i);

		this.selector = Selector.open();
		this.keyChannelMap = new HashMap<SelectionKey, SocketChannel>();
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

	@Override
	public void run() {
		int selected = 0;
		while (selector.isOpen()) {
			try {
				selected = selector.select();
				System.out.println("Selected " + selected);
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

	public synchronized boolean addSocketChannel(SocketChannel socketChannel)
			throws ClosedChannelException, IOException {

		socketChannel.configureBlocking(false);
		SelectionKey key = socketChannel.register(selector,
				SelectionKey.OP_READ);
		keyChannelMap.put(key, socketChannel);

		System.out.println("Registered socket to " + getName());

		return true;

	}

}
