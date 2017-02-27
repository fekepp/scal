package net.fekepp.ldfu.server.resource;

import java.io.IOException;

import net.fekepp.ldfu.server.exceptions.ContainerIdentifierExpectedException;
import net.fekepp.ldfu.server.exceptions.ParentNotFoundException;
import net.fekepp.ldfu.server.exceptions.ParseException;
import net.fekepp.ldfu.server.exceptions.ParserException;
import net.fekepp.ldfu.server.exceptions.ResourceIdentifierExpectedException;
import net.fekepp.ldfu.server.exceptions.ResourceNotFoundException;

public interface ResourceManager {

	// NEW

	public Source getResource(Description description) throws ResourceNotFoundException,
			ContainerIdentifierExpectedException, ResourceIdentifierExpectedException, IOException;

	public void setResource(Source source)
			throws ContainerIdentifierExpectedException, ResourceIdentifierExpectedException, ParentNotFoundException,
			IOException, ParseException, ParserException, InterruptedException;

	public void delResource(Description resource) throws ResourceNotFoundException, ResourceIdentifierExpectedException,
			ContainerIdentifierExpectedException, IOException;

	public Source proResource(Source source);

	// // OLD
	//
	// public SourceResourceOLD getResource(String identifier, String mediaType,
	// URI base)
	// throws ResourceNotFoundException, ContainerIdentifierExpectedException,
	// ResourceIdentifierExpectedException;
	//
	// public void setResource(String identifier, String mediaType, InputStream
	// data, URI base)
	// throws ContainerIdentifierExpectedException,
	// ResourceIdentifierExpectedException, ParentNotFoundException,
	// ParseException, IOException;
	//
	// public void delResource(String identifier) throws
	// ResourceNotFoundException, ResourceIdentifierExpectedException,
	// ContainerIdentifierExpectedException, IOException;
	//
	// public OutputStream proResource(String identifier, String mediaType,
	// InputStream data, URI base)
	// throws ResourceNotFoundException, ResourceIdentifierExpectedException,
	// ContainerIdentifierExpectedException;

}