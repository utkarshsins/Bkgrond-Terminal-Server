package bkground.server.terminal;

import java.io.IOException;
import java.util.Scanner;

import bkground.server.terminal.listeners.ListenerAdministrator;

/**
 * The main controller subsystem of the TerminalServer.
 */
public class Controller implements Runnable {

	private ServerInfo serverInfo;

	/**
	 * Initiate the server, create required threads and listeners.
	 * 
	 * @param scanner
	 * @return ServerInfo Returns the serverInfo instance that contains
	 *         initialization information for the terminal server.
	 * @throws IOException
	 *             , IllegalStateException
	 */
	private ServerInfo initiate(Scanner scanner) throws IllegalStateException,
			IOException {
		System.out.println("#####################################");
		System.out.println("Terminal Server");
		System.out.println("#####################################");

		ServerInfo serverInfo = new ServerInfo();

		System.out.print("Enter port number to listen on (default "
				+ ServerInfo.SERVER_INFO_DEFAULT_PORT + ") : ");
		String input = scanner.nextLine();

		try {
			serverInfo.setPort(Integer.parseInt(input));
		} catch (NumberFormatException e) {
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		}

		System.out.println();

		serverInfo.init();

		return serverInfo;
	}

	@Override
	public void run() {

		Scanner scanner = new Scanner(System.in);

		try {

			serverInfo = initiate(scanner);

			ListenerAdministrator admin = new ListenerAdministrator(scanner,
					serverInfo);
			admin.start();
			admin.join();

		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
		}

	}

}
