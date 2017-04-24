package net.fekepp.scal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fekepp.scal.server.ServerController;

public class App {

	private static final Logger loggerStatic = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {

		loggerStatic.info("SCAL App > Main");

		ServerController controller = new ServerController();
		controller.setPort(8888);
		controller.start();

	}

}