package net.fekepp.scal.server;

import java.util.logging.Logger;

public class ServerController extends net.fekepp.ldp.server.ServerController {

	private final Logger logger = Logger.getLogger(getClass().getName());

	@Override
	public void startup() {
		logger.info("SCAL Server > Startup");
		super.startup();
	}

	@Override
	public void shutdown() {
		logger.info("SCAL Server > Shutdown");
		super.shutdown();
	}

}
