package bkground.server.terminal.listeners;

import java.util.NoSuchElementException;
import java.util.Scanner;

import bkground.server.terminal.ServerInfo;

public class ListenerAdministrator extends Thread {
	private static final String THREAD_NAME = "THREAD_ADMIN";

	private static final String ADMIN_TITLE = "Choose from the following options : ";
	private static final String ADMIN_OPTIONS[] = { "1. Stop the server" };

	private Scanner scanner;

	private ServerInfo serverInfo;

	public ListenerAdministrator(Scanner scanner, ServerInfo serverInfo) {
		super();
		setName(THREAD_NAME);

		this.scanner = scanner;
		this.serverInfo = serverInfo;
	}

	@Override
	public void run() {

		int optionVal = 0;

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
				serverInfo.listenerServer.interrupt();
				break;
			}
		}

	}

}
