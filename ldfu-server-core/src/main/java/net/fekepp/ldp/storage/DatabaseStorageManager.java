package net.fekepp.ldp.storage;

import java.io.IOException;

import net.fekepp.ldp.Description;
import net.fekepp.ldp.Source;
import net.fekepp.ldp.StorageManager;
import net.fekepp.ldp.exception.ContainerIdentifierExpectedException;
import net.fekepp.ldp.exception.ParentNotFoundException;
import net.fekepp.ldp.exception.ParserException;
import net.fekepp.ldp.exception.ResourceIdentifierExpectedException;
import net.fekepp.ldp.exception.ResourceNotFoundException;

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
			IOException, net.fekepp.ldp.exception.ParseException, ParserException, InterruptedException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delResource(Description description) throws ResourceNotFoundException,
			ResourceIdentifierExpectedException, ContainerIdentifierExpectedException, IOException {
		// TODO Auto-generated method stub

	}

}