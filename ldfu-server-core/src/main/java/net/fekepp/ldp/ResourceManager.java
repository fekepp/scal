package net.fekepp.ldp;

import java.io.IOException;
import java.util.Set;

import net.fekepp.ldp.exception.ContainerIdentifierExpectedException;
import net.fekepp.ldp.exception.ConverterException;
import net.fekepp.ldp.exception.ParentNotFoundException;
import net.fekepp.ldp.exception.ParseException;
import net.fekepp.ldp.exception.ParserException;
import net.fekepp.ldp.exception.ResourceIdentifierExpectedException;
import net.fekepp.ldp.exception.ResourceNotFoundException;

public interface ResourceManager {

	public Source getResource(Description description) throws ResourceNotFoundException,
			ContainerIdentifierExpectedException, ResourceIdentifierExpectedException, IOException;

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