package net.fekepp.ldfu.server.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.commons.compress.utils.IOUtils;

import net.fekepp.ldfu.server.data.Format;
import net.fekepp.ldfu.server.data.FormatConverter;
import net.fekepp.ldfu.server.exceptions.ConverterException;
import net.fekepp.ldfu.server.exceptions.ParseException;
import net.fekepp.ldfu.server.exceptions.ParserException;

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
			throws ParseException, ParserException, ConverterException, IOException, InterruptedException {
		if (formatConverter != null) {
			formatConverter.setBaseUri(getBaseUri());
			formatConverter.setInputStream(inputStream);
			formatConverter.setOutputStream(outputStream);
			formatConverter.convert();
		} else {
			IOUtils.copy(inputStream, outputStream);
		}
	}

}
