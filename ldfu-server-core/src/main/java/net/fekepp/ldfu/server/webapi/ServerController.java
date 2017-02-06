package net.fekepp.ldfu.server.webapi;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.fekepp.controllers.BaseJettyJerseyController;
import net.fekepp.ldfu.server.storage.Storage;
import net.fekepp.ldfu.server.storage.filesystem.FilesystemStorage;

/**
 * @author "Felix Leif Keppmann"
 */
public class ServerController extends BaseJettyJerseyController {

	private final Logger logger = Logger.getLogger(getClass().getName());
	
	private static Storage storage = new FilesystemStorage();

	@Override
	public void startup() {

		logger.log(Level.INFO, "public void startup()");

		// Register servlets
		Servlet.setController(this);
		Servlet.setStorage(storage);
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