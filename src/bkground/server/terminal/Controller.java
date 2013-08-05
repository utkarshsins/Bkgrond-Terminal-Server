package bkground.server.terminal;

import java.io.InputStream;
import java.util.Scanner;

import bkground.server.terminal.listeners.ListenerAdministrator;

/**
 * The main controller subsystem of the TerminalServer.
 */
public class Controller implements Runnable {

	/**
	 * Initiate the server, create required threads and listeners.
	 * 
	 * @param scanner
	 * @return ServerInfo Returns the serverInfo instance that contains
	 *         initialization information for the terminal server.
	 */
	private ServerInfo initiate(InputStream inputStream)
			throws IllegalStateException {
		System.out.println("#####################################");
		System.out.println("Terminal Server");
		System.out.println("#####################################");

		ServerInfo serverInfo = new ServerInfo();

		Scanner scanner = new Scanner(inputStream);

		System.out.print("Enter port number to listen on (default "
				+ ServerInfo.SERVER_INFO_DEFAULT_PORT + ") : ");
		String input = scanner.nextLine();

		try {
			serverInfo.setPort(Integer.parseInt(input));
		} catch (NumberFormatException e) {
		} catch (IllegalAccessException e) {
			scanner.close();
			throw new IllegalStateException(e);
		}

		System.out.println();

		scanner.close();
		return serverInfo;
	}

	@Override
	public void run() {

		try {
			initiate(System.in);

			ListenerAdministrator admin = new ListenerAdministrator();
			admin.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}

	}

}
