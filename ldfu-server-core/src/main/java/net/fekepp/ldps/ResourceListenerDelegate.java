package net.fekepp.ldps;

import java.io.IOException;

import net.fekepp.ldps.exception.ContainerIdentifierExpectedException;
import net.fekepp.ldps.exception.ConverterException;
import net.fekepp.ldps.exception.ParentNotFoundException;
import net.fekepp.ldps.exception.ParseException;
import net.fekepp.ldps.exception.ParserException;
import net.fekepp.ldps.exception.ResourceIdentifierExpectedException;

public interface ResourceListenerDelegate {

	public Source process(Source storage, Source input)
			throws ContainerIdentifierExpectedException, ResourceIdentifierExpectedException, ParentNotFoundException,
			ParseException, ParserException, ConverterException, InterruptedException, IOException;

}
