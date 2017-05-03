package net.fekepp.ldps.storage;

import java.io.IOException;

import net.fekepp.ldps.Description;
import net.fekepp.ldps.Source;
import net.fekepp.ldps.StorageManager;
import net.fekepp.ldps.exception.ContainerIdentifierExpectedException;
import net.fekepp.ldps.exception.ParentNotFoundException;
import net.fekepp.ldps.exception.ParserException;
import net.fekepp.ldps.exception.ResourceIdentifierExpectedException;
import net.fekepp.ldps.exception.ResourceNotFoundException;

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
			IOException, net.fekepp.ldps.exception.ParseException, ParserException, InterruptedException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delResource(Description description) throws ResourceNotFoundException,
			ResourceIdentifierExpectedException, ContainerIdentifierExpectedException, IOException {
		// TODO Auto-generated method stub

	}

}