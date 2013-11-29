package bkground.server.terminal;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
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
	 *             if listener socket fails to setup
	 * @throws IllegalStateException
	 *             should not happen
	 */
	private ServerInfo initiate(Scanner scanner) throws IllegalStateException,
			IOException {
		System.out.println(Defaults.TERMINAL_SERVER_BANNER);

		ServerInfo serverInfo = new ServerInfo();

		initiatePort(scanner, serverInfo);
		initiateThreadCount(scanner, serverInfo);
		initiateID(scanner, serverInfo);
		initiateDataForwardingConnection(scanner, serverInfo);

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

		scanner.close();

	}

	private void initiatePort(Scanner scanner, ServerInfo serverInfo)
			throws IllegalStateException {
		System.out.print("Enter port number to listen on (default "
				+ ServerInfo.SERVER_INFO_DEFAULT_PORT + ") : ");
		String input = scanner.nextLine();

		try {
			serverInfo.setPort(Integer.parseInt(input));
		} catch (NumberFormatException e) {
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}

	private void initiateThreadCount(Scanner scanner, ServerInfo serverInfo)
			throws IllegalStateException {
		System.out
				.println("Enter number of threads to handle connections (default "
						+ Defaults.getDefaultListenerThreadCount() + ") : ");
		String input = scanner.nextLine();

		try {
			serverInfo.setThreadCount(Integer.parseInt(input));
		} catch (NumberFormatException e) {
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}

	private void initiateID(Scanner scanner, ServerInfo serverInfo) {
		System.out.println("Enter terminal server id (default "
				+ Defaults.getDefaultTerminalTerminalServerID() + ") : ");
		String input = scanner.nextLine();

		try {
			serverInfo.setThreadCount(Integer.parseInt(input));
		} catch (NumberFormatException e) {
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}

	private void initiateDataForwardingConnection(Scanner scanner,
			ServerInfo serverInfo) {
		try {
			System.out.println("Enter data-forwading server ip (default "
					+ ServerInfo.DF_SERVER_DEFAULT_ADDRESS + ") : ");
			String input = scanner.nextLine();
			if (input.equals(""))
				input = ServerInfo.DF_SERVER_DEFAULT_ADDRESS;

			serverInfo.terminalServerSocket = SocketChannel.open();
			serverInfo.terminalServerSocket.configureBlocking(true);
			serverInfo.terminalServerSocket.connect(new InetSocketAddress(
					ServerInfo.DF_SERVER_DEFAULT_ADDRESS,
					ServerInfo.DF_SERVER_DEFAULT_PORT));
			
			serverInfo.terminalBackThread.enqueSocketChannel(serverInfo.terminalServerSocket);
		} catch (IOException e) {
			System.err.println("Unable to connect to Data Forwarding Server");
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
