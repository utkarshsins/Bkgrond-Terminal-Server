package bkground.server.terminal.listeners;

import java.io.IOException;
import java.nio.channels.Selector;

public class ListenerSocket extends Thread {

	private static final String THREAD_NAME = "THREAD_SOCKET";

	private Selector selector;

	public ListenerSocket() throws IOException {
		super(new ThreadRunnable());
		setName(THREAD_NAME);

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

	private static class ThreadRunnable implements Runnable {

		@Override
		public void run() {
		}
	}

}
