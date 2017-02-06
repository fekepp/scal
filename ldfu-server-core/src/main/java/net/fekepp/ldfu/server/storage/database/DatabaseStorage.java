package net.fekepp.ldfu.server.storage.database;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.MediaType;

import org.semanticweb.yars.nx.parser.ParseException;

import net.fekepp.ldfu.server.storage.ContainerIdentifierExpectedException;
import net.fekepp.ldfu.server.storage.ParentNotFoundException;
import net.fekepp.ldfu.server.storage.ProcessingNotSupportedException;
import net.fekepp.ldfu.server.storage.ResourceIdentifierExpectedException;
import net.fekepp.ldfu.server.storage.ResourceNotFoundException;
import net.fekepp.ldfu.server.storage.Storage;
import net.fekepp.ldfu.server.storage.StorageResource;

public class DatabaseStorage implements Storage {

	@Override
	public StorageResource getResource(String identifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StorageResource setResource(String identifier, MediaType mediaType, InputStream inputStream)
			throws ContainerIdentifierExpectedException, ResourceIdentifierExpectedException, ParentNotFoundException,
			ParseException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StorageResource deleteResource(String identifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StorageResource processResource(String identifier, MediaType mediaType, InputStream inputStream)
			throws ResourceNotFoundException, ResourceIdentifierExpectedException, ContainerIdentifierExpectedException,
			ProcessingNotSupportedException, ParseException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

}