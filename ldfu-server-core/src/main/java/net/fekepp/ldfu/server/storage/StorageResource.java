package net.fekepp.ldfu.server.storage;

import java.io.InputStream;

import net.fekepp.ldfu.server.formats.Format;

public interface StorageResource {

	public String getIdentifier();

	public Format getFormat();

//	public Set<Node[]> getRdfRepresentation();

//	public InputStream getBinaryRepresentation();
	
	public InputStream getData();

	public boolean isRdfResource();

	public boolean isContainerResource();

}