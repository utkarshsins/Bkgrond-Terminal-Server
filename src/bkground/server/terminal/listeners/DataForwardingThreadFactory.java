package bkground.server.terminal.listeners;

import java.util.concurrent.ThreadFactory;

import bkground.server.terminal.ServerInfo;

public class DataForwardingThreadFactory implements ThreadFactory {

	ServerInfo serverInfo;

	public DataForwardingThreadFactory(ServerInfo serverInfo) {

		this.serverInfo = serverInfo;

	}

	@Override
	public Thread newThread(Runnable arg0) {

		return new DataForwardingThread(serverInfo, arg0);

	}

}
