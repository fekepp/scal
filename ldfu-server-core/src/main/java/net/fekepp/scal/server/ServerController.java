package net.fekepp.scal.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fekepp.scal.RunManager;
import net.fekepp.scal.run.DefaultRunManager;

public class ServerController extends net.fekepp.ldp.server.ServerController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

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
