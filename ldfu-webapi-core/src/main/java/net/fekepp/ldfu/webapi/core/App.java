package net.fekepp.ldfu.webapi.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

	private static final Logger loggerStatic = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {

		loggerStatic.info("public static void main(String[] args)");

		ServerController controller = new ServerController();
		controller.setPort(8888);
		controller.start();

	}

}