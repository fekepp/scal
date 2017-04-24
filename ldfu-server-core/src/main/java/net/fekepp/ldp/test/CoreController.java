package net.fekepp.ldp.test;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fekepp.controllers.AbstractController;

public class CoreController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private ConcurrentMap<String, String> paths = new ConcurrentHashMap<String, String>();

	private ConcurrentMap<String, String> programs = new ConcurrentHashMap<String, String>();

	private ConcurrentMap<String, String> queries = new ConcurrentHashMap<String, String>();

	private ConcurrentMap<String, String> resources = new ConcurrentHashMap<String, String>();

	private Path path;

	private Set<Path> pathsIgnored;

	@Override
	protected void startup() throws Exception {

	}

	@Override
	protected void shutdown() throws Exception {

	}

	private synchronized void scanResources() {

		try {
			Files.walkFileTree(path, new FileVisitor<Path>() {
				@Override
				public FileVisitResult postVisitDirectory(Path path, IOException exception) throws IOException {
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attributes) throws IOException {
					if (pathsIgnored.contains(path)) {
						return FileVisitResult.SKIP_SUBTREE;
					}
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path path, BasicFileAttributes attributes) throws IOException {
					path.toFile();
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path path, IOException exception) throws IOException {
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			logger.warn("Could not taverse the file tree.", e);
		}
	}

}
