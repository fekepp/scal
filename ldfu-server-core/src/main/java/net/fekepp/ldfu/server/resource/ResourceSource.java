package net.fekepp.ldfu.server.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import net.fekepp.ldfu.server.converter.FormatConverter;
import net.fekepp.ldfu.server.exceptions.ParseException;
import net.fekepp.ldfu.server.exceptions.ParserException;
import net.fekepp.ldfu.server.formats.Format;

public class ResourceSource extends ResourceDescription implements Source {

	private InputStream inputStream;
	private FormatConverter formatConverter;

	public ResourceSource(URI baseUri, String identifier) {
		this(baseUri, identifier, null);
	}

	public ResourceSource(URI baseUri, String identifier, Format format) {
		this(baseUri, identifier, format, null);
	}

	public ResourceSource(URI baseUri, String identifier, Format format, InputStream inputStream) {
		this(baseUri, identifier, format, inputStream, null);
	}

	public ResourceSource(URI baseUri, String identifier, Format format, InputStream inputStream,
			FormatConverter formatConverter) {
		super(baseUri, identifier, format);
		this.inputStream = inputStream;
		this.formatConverter = formatConverter;
	}

	@Override
	public InputStream getInputStream() {
		return inputStream;
	}

	@Override
	public FormatConverter getFormatConverter() {
		return formatConverter;
	}

	@Override
	public void streamTo(OutputStream outputStream)
			throws ParseException, ParserException, IOException, InterruptedException {
		// TODO Check for null?
		formatConverter.setBaseUri(getBaseUri());
		formatConverter.setInputStream(inputStream);
		formatConverter.setOutputStream(outputStream);
		formatConverter.convert();
	}

}
