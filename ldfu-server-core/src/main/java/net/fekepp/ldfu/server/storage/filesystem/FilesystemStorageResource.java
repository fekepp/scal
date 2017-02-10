package net.fekepp.ldfu.server.storage.filesystem;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.namespace.LDP;
import org.semanticweb.yars.nx.namespace.RDF;

import net.fekepp.ldfu.server.mediatype.Format;
import net.fekepp.ldfu.server.storage.StorageResource;

public class FilesystemStorageResource implements StorageResource {

	private String identifier;

	private boolean rdfResource = false;

	private boolean containerResource = false;

	private Path path;

	public Set<Node[]> getRdfRepresentation() {

		if (Files.isDirectory(path)) {

			// Create the representation to be returned
			Set<Node[]> representation = new HashSet<Node[]>();

			// Create a base URI for the requested resource
			Resource identifierResource = new Resource(identifier);

			// Let the resource be a LDP Container
			representation.add(new Node[] { identifierResource, RDF.TYPE, LDP.CONTAINER });

			// Let the resource be a LDP Basic Container
			representation.add(new Node[] { identifierResource, RDF.TYPE, LDP.BASIC_CONTAINER });

			// Java7
			for (final File file : path.toFile().listFiles()) {
				representation.add(new Node[] { identifierResource, LDP.CONTAINS,
						new Resource(identifier + file.getName() + (file.isDirectory() ? "/" : "")) });
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

			return representation;

		} else if (path.endsWith(".ttl")) {

		}

		return null;

	}

	public InputStream getBinaryRepresentation() {

		// new FileInputStream(path.toFile());
		// String path = "C:\\user\\data\\thefile.txt";
		// File file = new File(path);
		// FileInputStream fileInputStream = new
		// FileInputStream(file);
		return null;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String path) {
		this.identifier = path;
	}

	@Override
	public Format getFormat() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRdfResource() {
		return rdfResource;
	}

	public void setRdfResource(boolean rdfResource) {
		this.rdfResource = rdfResource;
	}

	@Override
	public boolean isContainerResource() {
		return containerResource;
	}

	public void setContainerResource(boolean containerResource) {
		this.containerResource = containerResource;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	@Override
	public InputStream getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
