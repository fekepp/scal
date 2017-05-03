package net.fekepp.ldps.storage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Nodes;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.namespace.LDP;
import org.semanticweb.yars.nx.namespace.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fekepp.ldps.Description;
import net.fekepp.ldps.Format;
import net.fekepp.ldps.FormatConverterListenerDelegate;
import net.fekepp.ldps.Models;
import net.fekepp.ldps.Source;
import net.fekepp.ldps.StorageManager;
import net.fekepp.ldps.converter.RdfConverter;
import net.fekepp.ldps.converter.RdfConverterTripleListener;
import net.fekepp.ldps.exception.ContainerIdentifierExpectedException;
import net.fekepp.ldps.exception.ConverterException;
import net.fekepp.ldps.exception.ParentNotFoundException;
import net.fekepp.ldps.exception.ParseException;
import net.fekepp.ldps.exception.ParserException;
import net.fekepp.ldps.exception.ResourceIdentifierExpectedException;
import net.fekepp.ldps.exception.ResourceNotFoundException;
import net.fekepp.ldps.format.TurtleFormat;
import net.fekepp.ldps.model.RdfModel;
import net.fekepp.ldps.resource.ResourceSource;

public class FilesystemStorageManager implements StorageManager {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private Path rootPath;

	private Map<String, Format> fileExtensionToFormatMap = Models.getFileExtensionsMap();

	// TODO Find a more elegant way to skip directory creation for temporary
	// storage
	private boolean temporaryStorage;

	public FilesystemStorageManager(Path rootPath) {
		this(rootPath, false);
	}

	public FilesystemStorageManager(Path rootPath, boolean temporaryStorage) {
		this.rootPath = rootPath;
		this.temporaryStorage = temporaryStorage;
	}

	@Override
	public Source getResource(Description description) throws ResourceNotFoundException,
			ContainerIdentifierExpectedException, ResourceIdentifierExpectedException, IOException {

		logger.info("getResource(Description description) > description.getIdentifier()={}",
				description.getIdentifier());

		String identifier = description.getIdentifier();

		Path path = rootPath.resolve("./" + identifier).normalize();

		logger.info("Path > {}", path);

		if (Files.isDirectory(path)) {

			logger.info("Path is a directory");

			if (!identifier.equals("") && !identifier.endsWith("/")) {
				throw new ContainerIdentifierExpectedException();
			}

			// TODO Replace Turtle by N-Tripels
			return new ResourceSource(null, identifier, TurtleFormat.getInstance(),
					buildContainerInputStream(identifier, path));

		}

		else {

			logger.info("Path is not a directory");

			if (identifier.endsWith("/")) {
				throw new ResourceIdentifierExpectedException();
			}

			if (Files.exists(path)) {

				logger.info("Path is file without extension");

				return new ResourceSource(null, identifier, null, new FileInputStream(path.toString()));

			}

			else {

				Path pathWithExtension = null;
				Format pathWithExtensionFormat = null;

				for (Entry<String, Format> fileExtensionToFormatEntry : fileExtensionToFormatMap.entrySet()) {
					Path pathTest = rootPath.resolve("./" + identifier + fileExtensionToFormatEntry.getKey())
							.normalize();
					logger.debug("Testing path > {}", pathTest);
					if (Files.exists(pathTest)) {
						pathWithExtension = pathTest;
						pathWithExtensionFormat = fileExtensionToFormatEntry.getValue();
					}
				}

				if (pathWithExtension != null && pathWithExtensionFormat != null) {
					logger.info("Path is file with extension and known format > {}", pathWithExtension);

					return new ResourceSource(null, identifier, pathWithExtensionFormat,
							new FileInputStream(pathWithExtension.toString()));

				}

				else {
					logger.info("Path is not existing");
					throw new ResourceNotFoundException();
				}

			}

		}

	}

	@Override
	public void setResource(Source source)
			throws ContainerIdentifierExpectedException, ResourceIdentifierExpectedException, ParentNotFoundException,
			ParseException, ParserException, ConverterException, InterruptedException, IOException {

		logger.info("setResource(Source source) > description.getIdentifier()={}", source.getIdentifier());

		String identifier = source.getIdentifier();

		// TODO Ensure that the resulting path in the root directory
		// Path must be final for Java 7 support related to listeners
		final Path path = rootPath.resolve("./" + identifier).normalize();

		logger.info("path > {}", path);

		// If parent directory exists
		// TODO Improve to handle root directory or root file
		if (Files.exists(path.getParent())) {

			logger.info("Parent path is existing");

			Path pathWithExtension = null;
			Format pathWithExtensionFormat = null;

			for (Entry<String, Format> fileExtensionToFormatEntry : fileExtensionToFormatMap.entrySet()) {
				Path pathTest = rootPath.resolve("./" + identifier + fileExtensionToFormatEntry.getKey()).normalize();
				logger.debug("Testing path > {}", pathTest);
				if (Files.exists(pathTest)) {
					pathWithExtension = pathTest;
					pathWithExtensionFormat = fileExtensionToFormatEntry.getValue();
				}
			}

			// Directory and file with same name and RDF extension exists
			if (Files.isDirectory(path) && pathWithExtension != null && pathWithExtensionFormat != null
					&& pathWithExtensionFormat.getModel().equals(RdfModel.getInstance())) {

				logger.info("Directory and file with same name and RDF extension exists > {}", pathWithExtensionFormat);

				// If source format is not available or if source format is
				// avaliable but not part of the RDF model
				if (source.getFormat() == null || (source.getFormat() != null
						&& !source.getFormat().getModel().equals(RdfModel.getInstance()))) {

					// Delete the directory
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

				}

				// Delete the file with extension
				Files.delete(pathWithExtension);

			}

			// NOT ALLOWED
			// Directory and file with same name and Non-RDF extension exists
			else if (Files.isDirectory(path) && pathWithExtension != null && pathWithExtensionFormat != null
					&& !pathWithExtensionFormat.getModel().equals(RdfModel.getInstance())) {

				logger.error("Inconsistent storage > Directory and non-RDF file > Renaming file > {} | {}", path,
						pathWithExtension);

				// Delete the file with extension
				Files.move(pathWithExtension, Paths.get(pathWithExtension.toString(), ".error"));

			}

			// Directory exists
			else if (Files.isDirectory(path)) {

				logger.info("Directory is existing > {}", path);

				// If source format is not available or if source format is
				// avaliable but not part of the RDF model
				if (source.getFormat() == null || (source.getFormat() != null
						&& !source.getFormat().getModel().equals(RdfModel.getInstance()))) {

					// Delete the directory
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

				}

			}

			// NOT ALLOWED
			// File without and with extension exists
			else if (Files.exists(path) && pathWithExtension != null && pathWithExtensionFormat != null) {

				logger.error(
						"Inconsistent storage > File with and without extension > Renaming file with extension > {} | {}",
						path, pathWithExtension);

				// Delete the file with extension
				Files.move(pathWithExtension, Paths.get(pathWithExtension.toString(), ".error"));

			}

			// File with extension exists
			else if (pathWithExtension != null && pathWithExtensionFormat != null) {

				logger.info("File with extension exists > {}", pathWithExtensionFormat);

				// Delete the file with extension
				Files.delete(pathWithExtension);

			}

			// File without extension exists
			else if (Files.exists(path)) {

				logger.info("File without extension exists > {}", path);

				// Delete the file without extension
				Files.delete(path);

			}

			// If source format is available
			if (source.getFormat() != null) {

				logger.info("Source format is available");

				if (!temporaryStorage && source.getFormat().getModel().equals(RdfModel.getInstance())) {

					logger.info("Register trigger and react on containers");

					logger.info("source.getFormatConverter()={}", source.getFormatConverter());

					if (source.getFormatConverter() instanceof RdfConverter) {

						logger.info("source.getFormatConverter() instanceof RdfConverter");

						RdfConverter rdfConverter = (RdfConverter) source.getFormatConverter();

						RdfConverterTripleListener rdfConverterTripleListener = new RdfConverterTripleListener();
						rdfConverterTripleListener.setPredicate(RDF.TYPE);
						rdfConverterTripleListener.setObject(LDP.CONTAINER);
						logger.info("Register listener > predicate={} | object={}", RDF.TYPE, LDP.CONTAINER);
						rdfConverterTripleListener
								.setFormatConverterListenerDelegate(new FormatConverterListenerDelegate() {

									@Override
									public void process() {
										try {
											logger.info("Create directory for container");
											Files.createDirectory(path);
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}

								});

						rdfConverter.getFormatConverterListeners().add(rdfConverterTripleListener);
					}

				}

				logger.info("Create file with extension > {}",
						path.toString() + source.getFormat().getDefaultFileExtension());

				// Create file with extension
				source.streamTo(new FileOutputStream(path.toString() + source.getFormat().getDefaultFileExtension()));

				return;

			}

			logger.info("Source format is not available");

			logger.info("Create file without extension > {}", path);

			// Create file without extension
			source.streamTo(new FileOutputStream(path.toString()));

			return;

		}

		logger.info("Parent path is not existing");

		// Else if parent directory does not exist
		throw new ParentNotFoundException();

	}

	@Override
	public void delResource(Description description) throws ResourceNotFoundException,
			ResourceIdentifierExpectedException, ContainerIdentifierExpectedException, IOException {

		String identifier = description.getIdentifier();

		logger.info("delResource(Description description) > description.getIdentifier()={}", identifier);

		Path path = rootPath.resolve("./" + identifier).normalize();

		logger.info("Path > {}", path);

		if (Files.isDirectory(path)) {

			logger.info("Path is a directory");

			if (!identifier.equals("") && !identifier.endsWith("/")) {
				throw new ContainerIdentifierExpectedException();
			}

			// Delete the directory
			Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					logger.info("Deleting file > {}", file);
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					logger.info("Deleting directory > {}", dir);
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				}

			});

			// Delete file with the same name that contains additional data
			Path pathWithExtension = null;

			for (Entry<String, Format> fileExtensionToFormatEntry : fileExtensionToFormatMap.entrySet()) {
				Path pathTest = rootPath.resolve(
						"./" + identifier.substring(0, identifier.length() - 1) + fileExtensionToFormatEntry.getKey())
						.normalize();
				logger.debug("Testing path > {}", pathTest);
				if (Files.exists(pathTest)) {
					logger.info("Deleting file > {}", pathTest);
					Files.delete(pathTest);
					pathWithExtension = pathTest;
				}
			}

			if (pathWithExtension == null) {
				logger.info("Path is not existing");
				throw new ResourceNotFoundException();
			}

		}

		else {

			logger.info("Path is not a directory");

			if (identifier.endsWith("/")) {
				throw new ResourceIdentifierExpectedException();
			}

			if (Files.exists(path)) {

				logger.info("Path is file without extension");

				logger.info("Deleting file > {}", path);
				Files.delete(path);

			}

			else {

				Path pathWithExtension = null;

				for (Entry<String, Format> fileExtensionToFormatEntry : fileExtensionToFormatMap.entrySet()) {
					Path pathTest = rootPath.resolve("./" + identifier + fileExtensionToFormatEntry.getKey())
							.normalize();
					logger.debug("Testing path > {}", pathTest);
					if (Files.exists(pathTest)) {
						logger.info("Deleting file > {}", pathTest);
						Files.delete(pathTest);
						pathWithExtension = pathTest;
					}
				}

				if (pathWithExtension == null) {
					logger.info("Path is not existing");
					throw new ResourceNotFoundException();
				}

			}

		}

	}

	private InputStream buildContainerInputStream(String identifier, Path path) {

		// Create the representation to be returned
		Set<Node[]> representation = new HashSet<Node[]>();

		// Create a base URI for the requested resource
		Resource identifierResource = new Resource(identifier);

		// Let the resource be a LDP Container
		representation.add(new Node[] { identifierResource, RDF.TYPE, LDP.CONTAINER });

		// Let the resource be a LDP Basic Container
		representation.add(new Node[] { identifierResource, RDF.TYPE, LDP.BASIC_CONTAINER });

		// Add containment triples for all contained resources
		// Java7
		for (final File file : path.toFile().listFiles()) {
			String name = file.getName();
			int lastDotIndex = name.lastIndexOf(".");
			if (lastDotIndex >= 0) {
				name = name.substring(0, lastDotIndex);
				if (Files.exists(path.resolve(name))) {
					continue;
				}
			}
			representation.add(new Node[] { identifierResource, LDP.CONTAINS,
					new Resource(name + (file.isDirectory() ? "/" : "")) });
		}

		// Java8
		// try (Stream<Path> paths = Files.list(path)) {
		// paths.forEach(path -> {
		// representation.add(new Node[] { identifierResource, LDP.CONTAINS,
		// new Resource(identifier + path.getFileName() +
		// (Files.isDirectory(path) ? "/" : "")) });
		// });
		// }
		//
		// catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// TODO Add triples from RDF file that contains additional triple

		StringBuffer stringBuffer = new StringBuffer();
		for (Node[] node : representation) {
			stringBuffer.append(Nodes.toString(node));
			stringBuffer.append(System.getProperty("line.separator"));
		}

		// TODO Include additional RDF file via SequenceInputStream

		return new ByteArrayInputStream(stringBuffer.toString().getBytes());

	}

}