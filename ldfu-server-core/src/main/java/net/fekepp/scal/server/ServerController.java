package net.fekepp.scal.server;

import java.util.logging.Logger;

import net.fekepp.scal.RunManager;
import net.fekepp.scal.run.DefaultRunManager;

public class ServerController extends net.fekepp.ldp.server.ServerController {

	private final Logger logger = Logger.getLogger(getClass().getName());

	protected RunManager runManager;

	@Override
	public void startup() {
		logger.info("SCAL Server > Startup");
		super.startup();
		runManager = new DefaultRunManager(resourceManager);
	}

	@Override
	public void shutdown() {
		logger.info("SCAL Server > Shutdown");
		super.shutdown();
	}

}
