package net.fekepp.ldfu.server.storage;

import java.io.IOException;

import net.fekepp.ldfu.server.exceptions.ContainerIdentifierExpectedException;
import net.fekepp.ldfu.server.exceptions.ParentNotFoundException;
import net.fekepp.ldfu.server.exceptions.ParseException;
import net.fekepp.ldfu.server.exceptions.ParserException;
import net.fekepp.ldfu.server.exceptions.ResourceIdentifierExpectedException;
import net.fekepp.ldfu.server.exceptions.ResourceNotFoundException;
import net.fekepp.ldfu.server.resource.Description;
import net.fekepp.ldfu.server.resource.Source;

public interface Storage {

	// NEW

	public Source getResource(Description description) throws ResourceNotFoundException,
			ContainerIdentifierExpectedException, ResourceIdentifierExpectedException, IOException;

	public void setResource(Source source)
			throws ContainerIdentifierExpectedException, ResourceIdentifierExpectedException, ParentNotFoundException,
			ParseException, ParserException, InterruptedException, IOException;

	public void delResource(Description description) throws ResourceNotFoundException,
			ResourceIdentifierExpectedException, ContainerIdentifierExpectedException, IOException;

	// OLD

	// public StorageResource getResource(String identifier)
	// throws ResourceNotFoundException, ContainerIdentifierExpectedException,
	// ResourceIdentifierExpectedException;
	//
	// public void setResource(String identifier, StorageResource resource)
	// throws ContainerIdentifierExpectedException,
	// ResourceIdentifierExpectedException, ParentNotFoundException,
	// IOException;
	//
	// public void delResource(String identifier) throws
	// ResourceNotFoundException, ResourceIdentifierExpectedException,
	// ContainerIdentifierExpectedException, IOException;

}