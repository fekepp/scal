package net.fekepp.ldfu.server.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import net.fekepp.ldfu.server.data.converters.FormatConverter;
import net.fekepp.ldfu.server.data.formats.Format;
import net.fekepp.ldfu.server.exceptions.ParseException;
import net.fekepp.ldfu.server.exceptions.ParserException;

public class ResourceSink extends ResourceDescription implements Sink {

	private FormatConverter formatConverter;
	private OutputStream outputStream;

	public ResourceSink(URI baseUri, String identifier) {
		this(baseUri, identifier, null);
	}

	public ResourceSink(URI baseUri, String identifier, Format format) {
		this(baseUri, identifier, format, null);
	}

	public ResourceSink(URI baseUri, String identifier, Format format, OutputStream outputStream) {
		this(baseUri, identifier, format, outputStream, null);
	}

	public ResourceSink(URI baseUri, String identifier, Format format, OutputStream outputStream,
			FormatConverter formatConverter) {
		super(baseUri, identifier, format);
		this.outputStream = outputStream;
		this.formatConverter = formatConverter;
	}

	@Override
	public OutputStream getOutputStream() {
		return outputStream;
	}

	@Override
	public FormatConverter getFormatConverter() {
		return formatConverter;
	}

	@Override
	public void streamFrom(InputStream inputStream)
			throws ParseException, ParserException, IOException, InterruptedException {
		// TODO Check for null?
		formatConverter.setBaseUri(getBaseUri());
		formatConverter.setInputStream(inputStream);
		formatConverter.setOutputStream(outputStream);
		formatConverter.convert();
	}

}
