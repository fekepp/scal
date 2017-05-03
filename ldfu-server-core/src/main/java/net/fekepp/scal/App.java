package net.fekepp.scal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import net.fekepp.scal.server.ServerController;

public class App {

	private static final Logger loggerStatic = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {

		// Remove existing handlers attached to j.u.l root logger (optional)
		 SLF4JBridgeHandler.removeHandlersForRootLogger();

		// Add SLF4JBridgeHandler to j.u.l's root logger
		 SLF4JBridgeHandler.install();

		loggerStatic.info("SCAL App > Main");

		ServerController controller = new ServerController();
		controller.setPort(8888);
		controller.start();

	}

}