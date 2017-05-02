package net.fekepp.scal.run;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import net.fekepp.ldp.Format;
import net.fekepp.ldp.exception.ConverterException;
import net.fekepp.ldp.exception.ParseException;
import net.fekepp.ldp.exception.ParserException;
import net.fekepp.ldp.resource.ResourceSource;

public class QuerySinkResourceBridgeSource extends ResourceSource {

	private OutputStream outputStream;

	public QuerySinkResourceBridgeSource(URI baseUri, String identifier) {
		super(baseUri, identifier);
	}

	public QuerySinkResourceBridgeSource(URI baseUri, String identifier, Format format) {
		super(baseUri, identifier, format);
	}

	@Override
	public void streamTo(OutputStream outputStream)
			throws ParseException, ParserException, ConverterException, IOException, InterruptedException {
		this.outputStream = outputStream;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

}
