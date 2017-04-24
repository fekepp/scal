package net.fekepp.ldp.test;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fekepp.controllers.AbstractController;

/**
 * @author "Felix Leif Keppmann"
 */
public class DelayedRecursiveWatchController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private Path path;

	private Set<Path> pathsIgnored;

	private long delay;

	private Timer timer;

	private WatchService watchService;

	private Map<Path, WatchKey> watchKeys = new HashMap<Path, WatchKey>();

	private DelayedRecursiveWatchControllerDelegate delayedRecursiveWatchControllerDelegate;

	public DelayedRecursiveWatchController(Path path, Set<Path> pathsIgnored, long delay) {
		this.path = path;
		this.pathsIgnored = pathsIgnored;
		this.delay = delay;
	}

	public DelayedRecursiveWatchController(Path path, Set<Path> pathsIgnored, long delay,
			DelayedRecursiveWatchControllerDelegate delayedRecursiveWatchControllerDelegate) {
		this(path, pathsIgnored, delay);
		this.delayedRecursiveWatchControllerDelegate = delayedRecursiveWatchControllerDelegate;
	}

	@Override
	protected void startup() throws Exception {
		watchService = FileSystems.getDefault().newWatchService();
		registerWatchKeys();
	}

	protected void execute() throws Exception {
		while (execution && !shutdown) {
			// The method watchService.take() is blocking
			WatchKey watchKey = watchService.take();
			watchKey.pollEvents();
			watchKey.reset();
			registerWatchKeysDelayed();
		}
	}

	@Override
	protected void shutdown() throws Exception {
		watchService.close();
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	private synchronized void registerWatchKeysDelayed() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		timer = new Timer(this.getClass().getSimpleName() + "Timer");
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				registerWatchKeys();
				cleanupWatchKeys();
				if (delayedRecursiveWatchControllerDelegate != null) {
					delayedRecursiveWatchControllerDelegate.onWatchEvent();
				}
			}
		}, delay);
	}

	private synchronized void registerWatchKeys() {
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
					registerWatchKey(path);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path path, BasicFileAttributes attributes) throws IOException {
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

	private synchronized void cleanupWatchKeys() {
		for (Path path : watchKeys.keySet()) {
			if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
				deregisterWatchKey(path);
			} else {
				for (Path pathIgnored : pathsIgnored) {
					if (path.startsWith(pathIgnored)) {
						deregisterWatchKey(path);
					}
				}
			}
		}
	}

	private synchronized void registerWatchKey(Path path) {
		if (!watchKeys.containsKey(path)) {
			try {
				WatchKey watchKey = path.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY, OVERFLOW);
				watchKeys.put(path, watchKey);
			} catch (IOException e) {
				logger.warn("Could not register watch key for path > {}", path, e);
			}
		}
	}

	private synchronized void deregisterWatchKey(Path path) {
		WatchKey watchKey = watchKeys.get(path);
		if (watchKey != null) {
			watchKey.cancel();
			watchKeys.remove(path);
		}
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public Set<Path> getPathsIgnored() {
		return pathsIgnored;
	}

	public void setPathsIgnored(Set<Path> pathsIgnored) {
		this.pathsIgnored = pathsIgnored;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public DelayedRecursiveWatchControllerDelegate getDelayedRecursiveWatchControllerDelegate() {
		return delayedRecursiveWatchControllerDelegate;
	}

	public void setDelayedRecursiveWatchControllerDelegate(
			DelayedRecursiveWatchControllerDelegate delayedRecursiveWatchControllerDelegate) {
		this.delayedRecursiveWatchControllerDelegate = delayedRecursiveWatchControllerDelegate;
	}

}