package net.fekepp.ldfu.server.storage;

import java.io.InputStream;

import net.fekepp.ldfu.server.formats.Format;

public interface StorageResource {

	public String getIdentifier();

	public Format getFormat();

	public InputStream getInputStream();

	public boolean isRdfResource();

	public boolean isContainerResource();

}