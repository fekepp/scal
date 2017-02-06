package net.fekepp.ldfu.server.storage;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.MediaType;

import org.semanticweb.yars.nx.parser.ParseException;

public interface Storage {

	public StorageResource getResource(String identifier);
	// throws ContainerIdentifierRequiredException,
	// ResourceIdentifierRequiredException;

	public StorageResource setResource(String identifier, MediaType mediaType, InputStream inputStream)
			throws ContainerIdentifierExpectedException, ResourceIdentifierExpectedException, ParentNotFoundException,
			ParseException, IOException;

	public StorageResource deleteResource(String identifier);

	public StorageResource processResource(String identifier, MediaType mediaType, InputStream inputStream)
			throws ResourceNotFoundException, ResourceIdentifierExpectedException, ContainerIdentifierExpectedException,
			ProcessingNotSupportedException, ParseException, IOException;

	// handleRead
	// handleCreateOrUpdate
	// handleDelete
	// handleProcessing
}