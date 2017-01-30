package net.fekepp.ldfu.server.webapi;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.fekepp.controllers.BaseJettyJerseyController;

/**
 * @author "Felix Leif Keppmann"
 */
public class ServerController extends BaseJettyJerseyController {

	/**
	 * Logger
	 */
	private final Logger logger = Logger.getLogger(getClass().getName());

	@Override
	public void startup() {

		logger.log(Level.INFO, "public void startup()");

		// Register servlets
		Servlet.setController(this);
		getResourceConfig().register(Servlet.class);

		// Continue startup
		super.startup();

	}

	@Override
	public void shutdown() {
		logger.log(Level.INFO, "public void shutdown()");
		super.shutdown();
	}

}