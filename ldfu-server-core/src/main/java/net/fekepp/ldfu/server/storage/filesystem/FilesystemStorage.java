package net.fekepp.ldfu.server.storage.filesystem;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fekepp.ldfu.server.exceptions.ContainerIdentifierExpectedException;
import net.fekepp.ldfu.server.exceptions.ParentNotFoundException;
import net.fekepp.ldfu.server.exceptions.ResourceIdentifierExpectedException;
import net.fekepp.ldfu.server.exceptions.ResourceNotFoundException;
import net.fekepp.ldfu.server.mediatype.Format;
import net.fekepp.ldfu.server.mediatype.RdfFormatGroup;
import net.fekepp.ldfu.server.storage.Storage;
import net.fekepp.ldfu.server.storage.StorageResource;

public class FilesystemStorage implements Storage {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private Path rootPath;

	private Map<String, Format> fileExtensionToFormatMap = RdfFormatGroup.getInstance().getFileExtensionsMap();

	public FilesystemStorage(Path rootPath) {
		this.rootPath = rootPath;
	}

	@Override
	public StorageResource getResource(String identifier) throws ResourceNotFoundException,
			ContainerIdentifierExpectedException, ResourceIdentifierExpectedException {

		logger.info("getResource(String identifier) > identifier={}", identifier);

		Path path = rootPath.resolve("./" + identifier).normalize();

		logger.info("path > {}", path);

		if (Files.isDirectory(path)) {

			logger.info("path is directory");

			if (!identifier.equals("") && !identifier.endsWith("/")) {
				throw new ContainerIdentifierExpectedException();
			}

			return new FilesystemStorageContainerResource(identifier, path);

		}

		else {

			if (identifier.endsWith("/")) {
				throw new ResourceIdentifierExpectedException();
			}

			if (Files.exists(path)) {

				logger.info("Path is file without extension");

				return new FilesystemStorageNonRdfResource(identifier, path, null);

			}

			else {

				Path pathWithExtension = null;
				Format pathWithExtensionFormat = null;

				for (Entry<String, Format> fileExtensionToFormatEntry : fileExtensionToFormatMap.entrySet()) {
					Path pathTest = rootPath.resolve("./" + identifier + fileExtensionToFormatEntry.getKey())
							.normalize();
					// Path pathTest =
					// path.resolve(fileExtensionToFormatEntry.getKey());
					logger.info("Testing path > {}", pathTest);
					if (Files.exists(pathTest)) {
						pathWithExtension = pathTest;
						pathWithExtensionFormat = fileExtensionToFormatEntry.getValue();
					}
				}

				if (pathWithExtension != null
						&& RdfFormatGroup.getInstance().equals(pathWithExtensionFormat.getFormatGroup())) {
					logger.info("Path is file with RDF extension");
					return new FilesystemStorageRdfResource(identifier, pathWithExtension, pathWithExtensionFormat);

				}

				else if (pathWithExtension != null && pathWithExtensionFormat != null) {
					logger.info("Path is file with extension");
					return new FilesystemStorageNonRdfResource(identifier, pathWithExtension, pathWithExtensionFormat);
				}

				else {
					logger.info("Path is not existing");
					throw new ResourceNotFoundException();
				}

			}

		}

	}

	@Override
	public void setResource(String identifier, StorageResource resource) throws ContainerIdentifierExpectedException,
			ResourceIdentifierExpectedException, ParentNotFoundException, IOException {

		logger.info("setResource(String identifier) > identifier={}", identifier);

		// TODO Ensure that the resulting path in in the root directory
		Path path = rootPath.resolve("./" + identifier).normalize();

		logger.info("path > {}", path);

		// If parent directory exists
		if (Files.exists(path.getParent())) {

			if (resource.isContainerResource()) {
				// Create directory
			} else {
				// Create file with or without extensions
			}

		}

		// Else if parent directory does not exist
		else {
			throw new ParentNotFoundException();
		}

		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}

		});

		if (Files.isDirectory(path)) {

		}

	}

	@Override
	public void delResource(String identifier) throws ResourceNotFoundException, ResourceIdentifierExpectedException,
			ContainerIdentifierExpectedException, IOException {

		logger.info("delResource(String identifier) > identifier={}", identifier);

		Path path = rootPath.resolve("./" + identifier).normalize();

		logger.info("path > {}", path);

		if (Files.isDirectory(path)) {

			logger.info("path is directory");

			if (!identifier.equals("") && !identifier.endsWith("/")) {
				throw new ContainerIdentifierExpectedException();
			}

			// return new FilesystemStorageContainerResource(identifier, path);

		}

		else {

			if (identifier.endsWith("/")) {
				throw new ResourceIdentifierExpectedException();
			}

			if (Files.exists(path)) {

				logger.info("Path is file without extension");

				// return new FilesystemStorageNonRdfResource(identifier, path);

			}

			else {

				Path pathWithExtension = null;
				Format pathWithExtensionFormat = null;

				for (Entry<String, Format> fileExtensionToFormatEntry : fileExtensionToFormatMap.entrySet()) {
					Path pathTest = path.resolve(fileExtensionToFormatEntry.getKey());
					if (Files.exists(pathTest)) {
						pathWithExtension = pathTest;
						pathWithExtensionFormat = fileExtensionToFormatEntry.getValue();
					}
				}

				if (pathWithExtension != null
						&& RdfFormatGroup.getInstance().equals(pathWithExtensionFormat.getFormatGroup())) {
					logger.info("Path is file with RDF extension");
					// return new FilesystemStorageRdfResource(identifier,
					// path);

				}

				else if (pathWithExtension != null) {
					logger.info("Path is file with extension");
					// return new FilesystemStorageNonRdfResource(identifier,
					// path);
				}

				else {
					logger.info("Path is not existing");
					throw new ResourceNotFoundException();
				}

			}

		}

		// Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
		//
		// @Override
		// public FileVisitResult visitFile(Path file, BasicFileAttributes
		// attrs) throws IOException {
		// Files.delete(file);
		// return FileVisitResult.CONTINUE;
		// }
		//
		// @Override
		// public FileVisitResult postVisitDirectory(Path dir, IOException exc)
		// throws IOException {
		// Files.delete(dir);
		// return FileVisitResult.CONTINUE;
		// }
		//
		// });

		// logger.info("delResource(String identifier) > identifier={}",
		// identifier);
		//
		// // TODO Ensure that the resulting path in in the root directory
		// Path path = rootPath.resolve("./" + identifier).normalize();
		//
		// logger.info("path > {}", path);
		//
		// if (Files.isDirectory(path)) {
		//
		// logger.info("path is directory");
		//
		// if (!identifier.endsWith("/")) {
		// throw new ContainerIdentifierExpectedException();
		// }
		//
		// }
		//
		// else {
		//
		// if (identifier.endsWith("/")) {
		// throw new ResourceIdentifierExpectedException();
		// }
		//
		// if (Files.exists(path)) {
		//
		// } else {
		//
		// Path pathWithExtension = null;
		// Format pathWithExtensionFormat = null;
		//
		// for (Entry<String, Format> fileExtensionToFormatEntry :
		// fileExtensionToFormatMap.entrySet()) {
		// Path pathTest = path.resolve(fileExtensionToFormatEntry.getKey());
		// if (Files.exists(pathTest)) {
		// pathWithExtension = pathTest;
		// pathWithExtensionFormat = fileExtensionToFormatEntry.getValue();
		// }
		// }
		//
		// if (pathWithExtension != null) {
		//
		// }
		//
		// else {
		// throw new ResourceNotFoundException();
		// }
		//
		// }
		//
		// }

	}

}