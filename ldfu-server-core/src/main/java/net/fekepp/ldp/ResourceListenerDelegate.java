package net.fekepp.ldp;

import java.io.IOException;

import net.fekepp.ldp.exception.ContainerIdentifierExpectedException;
import net.fekepp.ldp.exception.ConverterException;
import net.fekepp.ldp.exception.ParentNotFoundException;
import net.fekepp.ldp.exception.ParseException;
import net.fekepp.ldp.exception.ParserException;
import net.fekepp.ldp.exception.ResourceIdentifierExpectedException;

public interface ResourceListenerDelegate {

	public Source process(Source storage, Source input)
			throws ContainerIdentifierExpectedException, ResourceIdentifierExpectedException, ParentNotFoundException,
			ParseException, ParserException, ConverterException, InterruptedException, IOException;

}