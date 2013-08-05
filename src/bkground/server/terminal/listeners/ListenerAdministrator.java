package bkground.server.terminal.listeners;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class ListenerAdministrator extends Thread {
	private static final String THREAD_NAME = "THREAD_ADMIN";

	public ListenerAdministrator() {
		super(new ThreadRunnable());
		setName(THREAD_NAME);
	}

	private static class ThreadRunnable implements Runnable {

		private static final String ADMIN_TITLE = "Choose from the following options : ";
		private static final String ADMIN_OPTIONS[] = { "1. Stop the server" };

		@Override
		public void run() {

			int optionVal = 0;

			Scanner scanner = new Scanner(System.in);

			while (true) {
				System.out.println(ADMIN_TITLE);
				for (String option : ADMIN_OPTIONS) {
					System.out.println(option);
				}

				try {
					optionVal = scanner.nextInt();
				} catch (NoSuchElementException | IllegalStateException e) {
					e.printStackTrace();
				}

				if (optionVal == 1) {
					System.err.println("Shutting down server.");
					break;
				}
			}

			scanner.close();

		}

	}

}
