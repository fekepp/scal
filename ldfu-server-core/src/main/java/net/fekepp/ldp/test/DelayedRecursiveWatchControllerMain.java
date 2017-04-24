package net.fekepp.ldp.test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DelayedRecursiveWatchControllerMain implements DelayedRecursiveWatchControllerDelegate {

	public static void main(String[] args) {
		Path path = Paths.get("/home/fekepp/shares/ldfu-server/data");
		Set<Path> pathsIgnored = new HashSet<Path>();
		pathsIgnored.add(Paths.get("/home/fekepp/shares/ldfu-server/data/test-directory-ignore-01"));
		DelayedRecursiveWatchController controller = new DelayedRecursiveWatchController(path, pathsIgnored, 3000,
				new DelayedRecursiveWatchControllerMain());
		controller.start();
	}

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void onWatchEvent() {
		logger.info("Watch event");
	}

}