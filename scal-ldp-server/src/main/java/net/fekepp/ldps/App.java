package net.fekepp.ldps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fekepp.ldps.server.ServerController;

public class App {

	private static final Logger loggerStatic = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {

		loggerStatic.info("LDP App > Main");

		ServerController controller = new ServerController();
		controller.setPort(80);
		controller.start();

	}

}