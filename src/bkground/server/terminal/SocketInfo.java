package bkground.server.terminal;

import java.nio.channels.SelectionKey;

import com.fasterxml.aalto.AsyncInputFeeder;
import com.fasterxml.aalto.AsyncXMLStreamReader;

public class SocketInfo {

	public SelectionKey readKey;

	public AsyncXMLStreamReader xmlStreamReader;

	public AsyncInputFeeder xmlFeeder;

	public TerminalData data;

	private ServerInfo serverInfo;

	public SocketInfo(ServerInfo serverInfo) {

		this.serverInfo = serverInfo;
		
		refreshReader();

	}

	public void refreshReader() {

		data = null;
		
		xmlStreamReader = serverInfo.xmlInputFactory
				.createAsyncXMLStreamReader();

		xmlFeeder = xmlStreamReader.getInputFeeder();

	}
}
