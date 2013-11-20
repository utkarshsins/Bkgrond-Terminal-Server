package bkground.server.terminal.listeners;

import bkground.server.terminal.ServerInfo;

public class DataForwardingThread extends Thread {

	ServerInfo serverInfo;

	public DataForwardingThread(ServerInfo serverInfo, Runnable arg0) {

		super(arg0);
		this.serverInfo = serverInfo;

	}

}
