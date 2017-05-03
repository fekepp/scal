package net.fekepp.ldp.server;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;

import net.fekepp.controllers.BaseJettyJerseyController;
import net.fekepp.ldp.ResourceManager;
import net.fekepp.ldp.StorageManager;
import net.fekepp.ldp.resource.DefaultResourceManager;
import net.fekepp.ldp.storage.FilesystemStorageManager;

/**
 * @author "Felix Leif Keppmann"
 */
public class ServerController extends BaseJettyJerseyController {

	private final Logger logger = Logger.getLogger(getClass().getName());

	protected Path storageDirectory;

	protected Path storageDirectoryTemporary;

	protected ResourceManager resourceManager;

	protected StorageManager storageManager;

	protected StorageManager storageManagerTemporary;

	@Override
	public void startup() {

		logger.info("LDP Server > Startup");

		storageDirectory = Paths.get("../dat");
		storageDirectoryTemporary = Paths.get("../tmp");

		storageManager = new FilesystemStorageManager(storageDirectory);
		storageManagerTemporary = new FilesystemStorageManager(storageDirectoryTemporary, true);

		resourceManager = new DefaultResourceManager(storageManager, storageManagerTemporary);

		// Register servlets
		Servlet.setController(this);
		Servlet.setResourceManager(resourceManager);
		getResourceConfig().register(Servlet.class);

		// Continue startup
		super.startup();

	}

	@Override
	public void shutdown() {
		logger.info("LDP Server > Shutdown");
		super.shutdown();
	}

}