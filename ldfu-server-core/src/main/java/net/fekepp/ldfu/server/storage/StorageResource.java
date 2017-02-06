package net.fekepp.ldfu.server.storage;

import java.io.InputStream;
import java.util.Set;

import org.semanticweb.yars.nx.Node;

public interface StorageResource {

	public String getPath();
	
	public Set<Node[]> getRdfRepresentation();

	public InputStream getBinaryRepresentation();

	public boolean isRdfResource();

	public boolean isContainerResource();

}