package net.fekepp.ldp.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.commons.compress.utils.IOUtils;

import net.fekepp.ldp.Format;
import net.fekepp.ldp.FormatConverter;
import net.fekepp.ldp.Sink;
import net.fekepp.ldp.exception.ConverterException;
import net.fekepp.ldp.exception.ParseException;
import net.fekepp.ldp.exception.ParserException;

public class ResourceSink extends ResourceDescription implements Sink {

	private FormatConverter formatConverter;
	private OutputStream outputStream;

	public ResourceSink(URI base, String identifier) {
		this(base, identifier, null);
	}

	public ResourceSink(URI base, String identifier, Format format) {
		this(base, identifier, format, null);
	}

	public ResourceSink(URI base, String identifier, Format format, OutputStream outputStream) {
		this(base, identifier, format, outputStream, null);
	}

	public ResourceSink(URI base, String identifier, Format format, OutputStream outputStream,
			FormatConverter formatConverter) {
		super(base, identifier, format);
		this.outputStream = outputStream;
		this.formatConverter = formatConverter;
	}

	@Override
	public OutputStream getOutputStream() {
		return outputStream;
	}

	@Override
	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
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
	public void streamFrom(InputStream inputStream)
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
