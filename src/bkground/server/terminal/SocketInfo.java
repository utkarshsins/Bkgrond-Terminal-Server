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

	public User user;

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

	public static class User {

		public String username;

		public String firstname;

		public String lastname;

		public int id;

		public User(int userid, String username, String firstname,
				String lastname) {

			this.username = username;
			this.firstname = firstname;
			this.lastname = lastname;
			this.id = userid;

		}

		public User(jdbc_connect connection, String username, String password)
				throws Exception {

			connection.authorize_user_db(this, username, password);

			if (this.username == null)
				throw new Exception("User can't be authorized.");

		}

	}
}
