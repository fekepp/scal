package net.fekepp.ldfu.server.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.semanticweb.yars.nx.parser.ParseException;

import net.fekepp.ldfu.server.exceptions.ContainerIdentifierExpectedException;
import net.fekepp.ldfu.server.exceptions.ParentNotFoundException;
import net.fekepp.ldfu.server.exceptions.ResourceIdentifierExpectedException;
import net.fekepp.ldfu.server.exceptions.ResourceNotFoundException;

public interface ResourceManager {

	// public OutputStream getResource(String identifier);

	// public OutputStream getResource(String identifier, String mediaType);

	// public OutputStream getResource(String identifier, URI base);

	public InputStream getResource(String identifier, String mediaType, URI base)
			throws ResourceNotFoundException, ContainerIdentifierExpectedException, ResourceIdentifierExpectedException;

	// public void setResource(String identifier, String mediaType, InputStream
	// data);

	public void setResource(String identifier, String mediaType, InputStream data, URI base)
			throws ContainerIdentifierExpectedException, ResourceIdentifierExpectedException, ParentNotFoundException,
			ParseException, IOException;

	public void delResource(String identifier) throws ResourceNotFoundException, ResourceIdentifierExpectedException,
			ContainerIdentifierExpectedException, IOException;

	// public OutputStream proResource(String identifier, String mediaType,
	// InputStream data);

	public OutputStream proResource(String identifier, String mediaType, InputStream data, URI base)
			throws ResourceNotFoundException, ResourceIdentifierExpectedException, ContainerIdentifierExpectedException;

}