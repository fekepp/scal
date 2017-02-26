package net.fekepp.ldfu.server.converter;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import net.fekepp.ldfu.server.formats.Format;

public abstract class AbstractFormatConverter implements FormatConverter {

	protected URI base;

	protected Format inputFormat;
	protected InputStream inputStream;

	protected Format outputFormat;
	protected OutputStream outputStream;

	public AbstractFormatConverter(Format inputFormat, Format outputFormat) {
		this.inputFormat = inputFormat;
		this.outputFormat = outputFormat;
	}

	@Override
	public URI getBaseUri() {
		return base;
	}

	@Override
	public void setBaseUri(URI base) {
		this.base = base;
	}

	@Override
	public InputStream getInputStream() {
		return inputStream;
	}

	@Override
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	@Override
	public Format getInputFormat() {
		return inputFormat;
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
	public Format getOutputFormat() {
		return outputFormat;
	}

}
