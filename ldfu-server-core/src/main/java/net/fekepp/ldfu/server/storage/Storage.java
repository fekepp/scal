package net.fekepp.ldfu.server.storage;

import java.io.IOException;

import net.fekepp.ldfu.server.exceptions.ContainerIdentifierExpectedException;
import net.fekepp.ldfu.server.exceptions.ParentNotFoundException;
import net.fekepp.ldfu.server.exceptions.ResourceIdentifierExpectedException;
import net.fekepp.ldfu.server.exceptions.ResourceNotFoundException;

public interface Storage {

	public StorageResource getResource(String identifier)
			throws ResourceNotFoundException, ContainerIdentifierExpectedException, ResourceIdentifierExpectedException;

	public void setResource(String identifier, StorageResource resource) throws ContainerIdentifierExpectedException,
			ResourceIdentifierExpectedException, ParentNotFoundException, IOException;

	public void delResource(String identifier) throws ResourceNotFoundException, ResourceIdentifierExpectedException,
			ContainerIdentifierExpectedException, IOException;

}