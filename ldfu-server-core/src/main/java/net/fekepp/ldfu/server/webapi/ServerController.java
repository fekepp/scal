package net.fekepp.ldfu.server.webapi;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.fekepp.controllers.BaseJettyJerseyController;
import net.fekepp.ldfu.server.resource.DefaultResourceManager;
import net.fekepp.ldfu.server.resource.ResourceManager;
import net.fekepp.ldfu.server.storage.FilesystemStorageManager;
import net.fekepp.ldfu.server.storage.StorageManager;

/**
 * @author "Felix Leif Keppmann"
 */
public class ServerController extends BaseJettyJerseyController {

	private final Logger logger = Logger.getLogger(getClass().getName());

	// private URI base;

	private Path storageDirectory;

	private Path storageDirectoryTemporary;

	private static ResourceManager resourceManager;

	private static StorageManager storageManager;

	private static StorageManager storageManagerTemporary;

	@Override
	public void startup() {

		logger.log(Level.INFO, "public void startup()");

		// try {
		// base = new URI("http://" + getHost() + (getPort() == 80 ? "" : ":" +
		// getPort()) + "/");
		// } catch (URISyntaxException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		storageDirectory = Paths.get("../doc/example");
		storageDirectoryTemporary = Paths.get("../tmp");

		storageManager = new FilesystemStorageManager(storageDirectory);
		storageManagerTemporary = new FilesystemStorageManager(storageDirectoryTemporary);

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
		logger.log(Level.INFO, "public void shutdown()");
		super.shutdown();
	}

}