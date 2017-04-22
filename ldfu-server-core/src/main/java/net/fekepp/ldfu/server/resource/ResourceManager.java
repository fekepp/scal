package net.fekepp.ldfu.server.resource;

import java.io.IOException;

import net.fekepp.ldfu.server.data.Format;
import net.fekepp.ldfu.server.exceptions.ContainerIdentifierExpectedException;
import net.fekepp.ldfu.server.exceptions.ParentNotFoundException;
import net.fekepp.ldfu.server.exceptions.ParseException;
import net.fekepp.ldfu.server.exceptions.ParserException;
import net.fekepp.ldfu.server.exceptions.ResourceIdentifierExpectedException;
import net.fekepp.ldfu.server.exceptions.ResourceNotFoundException;

public interface ResourceManager {

	public Source getResource(Description description) throws ResourceNotFoundException,
			ContainerIdentifierExpectedException, ResourceIdentifierExpectedException, IOException;

	public void setResource(Source source)
			throws ContainerIdentifierExpectedException, ResourceIdentifierExpectedException, ParentNotFoundException,
			ParseException, ParserException, InterruptedException, IOException;

	public void delResource(Description resource) throws ResourceNotFoundException, ResourceIdentifierExpectedException,
			ContainerIdentifierExpectedException, IOException;

	public Source proResource(Source input, Format outputFormat)
			throws ResourceNotFoundException, ContainerIdentifierExpectedException, ResourceIdentifierExpectedException,
			ParentNotFoundException, ParseException, ParserException, InterruptedException, IOException;

}