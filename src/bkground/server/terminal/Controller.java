package bkground.server.terminal;

import bkground.server.terminal.listeners.ListenerAdministrator;

public class Controller implements Runnable {

	@Override
	public void run() {

		ListenerAdministrator admin = new ListenerAdministrator();
		admin.start();

	}

}
