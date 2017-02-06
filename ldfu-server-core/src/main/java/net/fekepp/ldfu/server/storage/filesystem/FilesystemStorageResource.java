package net.fekepp.ldfu.server.storage.filesystem;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.namespace.LDP;
import org.semanticweb.yars.nx.namespace.RDF;

import net.fekepp.ldfu.server.storage.StorageResource;

public class FilesystemStorageResource implements StorageResource {

	private String directory;

	private String path;

	private boolean rdfResource = true;

	private boolean containerResource = true;

	@Override
	public Set<Node[]> getRdfRepresentation() {

		// Create the representation to be returned
		Set<Node[]> representation = new HashSet<Node[]>();

		// Create a base URI for the requested resource
		Resource identifierResource = new Resource(path);

		// Let the resource be a LDP Container
		representation.add(new Node[] { identifierResource, RDF.TYPE, LDP.CONTAINER });

		// Let the resource be a LDP Basic Container
		representation.add(new Node[] { identifierResource, RDF.TYPE, LDP.BASIC_CONTAINER });

		return representation;

	}

	@Override
	public InputStream getBinaryRepresentation() {
		// String path = "C:\\user\\data\\thefile.txt";
		// File file = new File(path);
		// FileInputStream fileInputStream = new
		// FileInputStream(file);
		return null;
	}

	@Override
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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

}
