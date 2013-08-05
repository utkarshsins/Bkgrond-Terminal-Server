package bkground.server.terminal;

public class Defaults {

	public static final String TERMINAL_SERVER_BANNER = "#####################################\n"
			+ "Terminal Server\n" + "#####################################";

	public static int getDefaultThreadCount() {
		return Runtime.getRuntime().availableProcessors();
	}

}
