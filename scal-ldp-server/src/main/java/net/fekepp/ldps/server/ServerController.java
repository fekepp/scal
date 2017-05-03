package net.fekepp.ldps.server;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fekepp.controllers.BaseJettyJerseyController;
import net.fekepp.ldps.ResourceManager;
import net.fekepp.ldps.StorageManager;
import net.fekepp.ldps.resource.DefaultResourceManager;
import net.fekepp.ldps.storage.FilesystemStorageManager;

/**
 * @author "Felix Leif Keppmann"
 */
public class ServerController extends BaseJettyJerseyController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	protected Path storageDirectory;

	protected Path storageDirectoryTemporary;

	protected ResourceManager resourceManager;

	protected StorageManager storageManager;

	protected StorageManager storageManagerTemporary;

	@Override
	public void startup() {

		logger.info("LDP Server > Startup");

		logger.info("LDP Server > Startup > Storage > {}", storageDirectory);
		storageManager = new FilesystemStorageManager(storageDirectory);

		logger.info("LDP Server > Startup > Temporary Storage > {}", storageDirectoryTemporary);
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

	public Path getStorageDirectory() {
		return storageDirectory;
	}

	public void setStorageDirectory(Path storageDirectory) {
		this.storageDirectory = storageDirectory;
	}

	public Path getStorageDirectoryTemporary() {
		return storageDirectoryTemporary;
	}

	public void setStorageDirectoryTemporary(Path storageDirectoryTemporary) {
		this.storageDirectoryTemporary = storageDirectoryTemporary;
	}

}