package bkground.server.terminal;

public class TerminalData extends TerminalDataBase {

	public STATE state;

	public TerminalData() {
		state = STATE.BKGROUND;
	}

	@Override
	public boolean close() {
		boolean closed = super.close();

		if (closed)
			state = STATE.NULL;

		return closed;
	}

	public static class Authentication extends TerminalDataBase {

		public Authentication(SocketInfo socketInfo) {
			socketInfo.data.state = STATE.AUTHENTICATION;
		}

		public static class Username extends TerminalDataBase {
			public Username(SocketInfo socketInfo) {
				body = new String();
				socketInfo.data.state = STATE.USERNAME;
			}

			@Override
			public String toString() {

				if (open)
					return null;

				return "<username>" + body + "</username>";

			}
		}

		public static class Password extends TerminalDataBase {
			public Password(SocketInfo socketInfo) {
				body = new String();
				socketInfo.data.state = STATE.PASSWORD;
			}

			@Override
			public String toString() {

				if (open)
					return null;

				return "<password>" + body + "</password>";

			}
		}

		@Override
		public String toString() {

			if (open)
				return null;

			return "<authentication>" + getChild(0).toString()
					+ getChild(1).toString() + "</authentication>";

		}

	}

	public static class Message extends TerminalDataBase {

		public static class SubscriptionID extends TerminalDataBase {
			public SubscriptionID(SocketInfo socketInfo) {
				body = new String();
				socketInfo.data.state = STATE.SUBSCRIPTIONID;
			}

			@Override
			public String toString() {

				if (open)
					return null;

				return "<subscriptionid>" + body + "</subscriptionid>";

			}
		}

		public static class MessageBody extends TerminalDataBase {
			public MessageBody(SocketInfo socketInfo) {
				body = new String();
				socketInfo.data.state = STATE.MESSAGEBODY;
			}

			@Override
			public String toString() {

				if (open)
					return null;

				return "<messagebody>" + body + "</messagebody>";

			}
		}

		@Override
		public String toString() {

			if (open)
				return null;

			return "<message>" + getChild(0).toString()
					+ getChild(1).toString() + "</message>";

		}

	}

	@Override
	public String toString() {

		if (open)
			return null;

		return "<bkground>" + getChild(0).toString() + "</bkground>";

	}

}
