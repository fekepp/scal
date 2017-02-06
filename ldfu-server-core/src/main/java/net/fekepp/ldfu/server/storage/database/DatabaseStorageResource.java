package net.fekepp.ldfu.server.storage.database;

import java.io.InputStream;
import java.util.Set;

import org.semanticweb.yars.nx.Node;

import net.fekepp.ldfu.server.storage.StorageResource;

public class DatabaseStorageResource implements StorageResource {

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Node[]> getRdfRepresentation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getBinaryRepresentation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRdfResource() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isContainerResource() {
		// TODO Auto-generated method stub
		return false;
	}

}
