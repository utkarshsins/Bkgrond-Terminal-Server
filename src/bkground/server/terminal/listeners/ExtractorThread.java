package bkground.server.terminal.listeners;

import bkground.server.terminal.Defaults;
import bkground.server.terminal.jdbc_connect;

public class ExtractorThread extends Thread {

	public jdbc_connect mysqlConnector;

	public ExtractorThread(Runnable arg0) {

		super(arg0);

		mysqlConnector = new jdbc_connect(Defaults.getDefaultDatabaseAddress(),
				Defaults.getDefaultDatabaseName(),
				Defaults.getDefaultDatabaseUsername(),
				Defaults.getDefaultDatabasePassword());

	}

}
