package net.fekepp.ldps.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.commons.compress.utils.IOUtils;

import net.fekepp.ldps.Format;
import net.fekepp.ldps.FormatConverter;
import net.fekepp.ldps.Source;
import net.fekepp.ldps.exception.ConverterException;
import net.fekepp.ldps.exception.ParseException;
import net.fekepp.ldps.exception.ParserException;

public class ResourceSource extends ResourceDescription implements Source {

	private InputStream inputStream;
	private FormatConverter formatConverter;

	public ResourceSource(URI base, String identifier) {
		this(base, identifier, null);
	}

	public ResourceSource(URI base, String identifier, Format format) {
		this(base, identifier, format, null);
	}

	public ResourceSource(URI base, String identifier, Format format, InputStream inputStream) {
		this(base, identifier, format, inputStream, null);
	}

	public ResourceSource(URI base, String identifier, Format format, InputStream inputStream,
			FormatConverter formatConverter) {
		super(base, identifier, format);
		this.inputStream = inputStream;
		this.formatConverter = formatConverter;
	}

	@Override
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
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
	public void setFormatConverter(FormatConverter formatConverter) {
		this.formatConverter = formatConverter;
	}

	@Override
	public void streamTo(OutputStream outputStream)
			throws ParseException, ParserException, ConverterException, IOException, InterruptedException {
		if (formatConverter != null) {
			formatConverter.setBaseUri(getBase().resolve(getIdentifier()));
			formatConverter.setInputStream(inputStream);
			formatConverter.setOutputStream(outputStream);
			formatConverter.convert();
		} else {
			IOUtils.copy(inputStream, outputStream);
		}
	}

}
