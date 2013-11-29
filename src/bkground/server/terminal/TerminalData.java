package bkground.server.terminal;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

				return "<password>" + body + "</password>";

			}
		}

		@Override
		public String toString() {

			return "<authentication>" + getChild(0).toString()
					+ getChild(1).toString() + "</authentication>";

		}

	}

	public static class Message extends TerminalDataBase {

		public Message(SocketInfo socketInfo) {
			socketInfo.data.state = STATE.MESSAGE;
		}
		
		public static class SubscriptionID extends TerminalDataBase {
			public SubscriptionID(SocketInfo socketInfo) {
				body = new String();
				socketInfo.data.state = STATE.SUBSCRIPTIONID;
			}

			@Override
			public String toString() {

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

				return "<messagebody>" + body + "</messagebody>";

			}
		}

		@Override
		public String toString() {

			return "<message>" + getChild(0).toString()
					+ getChild(1).toString() + "</message>";

		}

	}
	
	public static class MessageBack extends Message {
		
		public List<Integer> recipients;
		
		public MessageBack(SocketInfo socketInfo) {
			super(socketInfo);
			recipients = new ArrayList<Integer>();
			socketInfo.data.state = STATE.MESSAGEBACK;
		}
		
		public static class Recipients extends TerminalDataBase {
			public Recipients(SocketInfo socketInfo) {
				body = new String();
				socketInfo.data.state = STATE.RECIPIENTS;
			}
			
			public List<Integer> processRecipients() {
				List<Integer> recipients = new ArrayList<Integer>();
				
				Scanner scan = new Scanner(body);
				while(scan.hasNext()) {
					recipients.add(scan.nextInt());
				}
				scan.close();
				
				return recipients;
			}
		}
		
		public void processRecipients() {
			if(getChild(2) instanceof Recipients) {
				Recipients recipientsChild = (Recipients) getChild(2);
				recipients = recipientsChild.processRecipients();
			}
		}
		
		@Override
		public boolean close() {
			processRecipients();
			return super.close();
		}
	}

	@Override
	public String toString() {

		return "<bkground>" + getChild(0).toString() + "</bkground>";

	}

}
