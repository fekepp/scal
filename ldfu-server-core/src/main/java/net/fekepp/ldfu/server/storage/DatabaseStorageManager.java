package net.fekepp.ldfu.server.storage;

import java.io.IOException;

import net.fekepp.ldfu.server.exceptions.ContainerIdentifierExpectedException;
import net.fekepp.ldfu.server.exceptions.ParentNotFoundException;
import net.fekepp.ldfu.server.exceptions.ParserException;
import net.fekepp.ldfu.server.exceptions.ResourceIdentifierExpectedException;
import net.fekepp.ldfu.server.exceptions.ResourceNotFoundException;
import net.fekepp.ldfu.server.resource.Description;
import net.fekepp.ldfu.server.resource.Source;

public class DatabaseStorageManager implements StorageManager {

	@Override
	public Source getResource(Description description) throws ResourceNotFoundException,
			ContainerIdentifierExpectedException, ResourceIdentifierExpectedException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setResource(Source source)
			throws ContainerIdentifierExpectedException, ResourceIdentifierExpectedException, ParentNotFoundException,
			IOException, net.fekepp.ldfu.server.exceptions.ParseException, ParserException, InterruptedException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delResource(Description description) throws ResourceNotFoundException,
			ResourceIdentifierExpectedException, ContainerIdentifierExpectedException, IOException {
		// TODO Auto-generated method stub

	}

}