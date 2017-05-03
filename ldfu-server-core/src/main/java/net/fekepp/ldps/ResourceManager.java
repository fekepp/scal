package net.fekepp.ldps;

import java.io.IOException;
import java.util.Set;

import net.fekepp.ldps.exception.ContainerIdentifierExpectedException;
import net.fekepp.ldps.exception.ConverterException;
import net.fekepp.ldps.exception.ParentNotFoundException;
import net.fekepp.ldps.exception.ParseException;
import net.fekepp.ldps.exception.ParserException;
import net.fekepp.ldps.exception.ResourceIdentifierExpectedException;
import net.fekepp.ldps.exception.ResourceNotFoundException;

public interface ResourceManager {

	public Source getResource(Description description) throws ResourceNotFoundException,
			ContainerIdentifierExpectedException, ResourceIdentifierExpectedException, IOException,
			ParentNotFoundException, ParseException, ParserException, ConverterException, InterruptedException;

	public void setResource(Source source)
			throws ContainerIdentifierExpectedException, ResourceIdentifierExpectedException, ParentNotFoundException,
			ParseException, ParserException, ConverterException, InterruptedException, IOException;

	public void delResource(Description resource) throws ResourceNotFoundException, ResourceIdentifierExpectedException,
			ContainerIdentifierExpectedException, IOException;

	public Source proResource(Source input, Format outputFormat) throws ResourceNotFoundException,
			ContainerIdentifierExpectedException, ResourceIdentifierExpectedException, ParentNotFoundException,
			ParseException, ParserException, ConverterException, InterruptedException, IOException;

	public Set<ResourceListener> getResourceListeners();

}