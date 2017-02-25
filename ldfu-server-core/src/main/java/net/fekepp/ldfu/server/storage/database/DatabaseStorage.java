package net.fekepp.ldfu.server.storage.database;

import java.io.IOException;
import java.io.InputStream;

import org.semanticweb.yars.nx.parser.ParseException;

import net.fekepp.ldfu.server.exceptions.ContainerIdentifierExpectedException;
import net.fekepp.ldfu.server.exceptions.ParentNotFoundException;
import net.fekepp.ldfu.server.exceptions.ProcessingNotSupportedException;
import net.fekepp.ldfu.server.exceptions.ResourceIdentifierExpectedException;
import net.fekepp.ldfu.server.exceptions.ResourceNotFoundException;
import net.fekepp.ldfu.server.formats.Format;
import net.fekepp.ldfu.server.storage.Storage;
import net.fekepp.ldfu.server.storage.StorageResource;

public class DatabaseStorage implements Storage {

	@Override
	public StorageResource getResource(String identifier) throws ResourceNotFoundException,
			ContainerIdentifierExpectedException, ResourceIdentifierExpectedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setResource(String identifier, StorageResource resource) throws ContainerIdentifierExpectedException,
			ResourceIdentifierExpectedException, ParentNotFoundException, IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delResource(String identifier) throws ResourceNotFoundException, ResourceIdentifierExpectedException,
			ContainerIdentifierExpectedException, IOException {
		// TODO Auto-generated method stub

	}

}