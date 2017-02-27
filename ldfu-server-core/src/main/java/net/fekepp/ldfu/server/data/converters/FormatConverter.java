package net.fekepp.ldfu.server.data.converters;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import net.fekepp.ldfu.server.data.formats.Format;
import net.fekepp.ldfu.server.exceptions.ParseException;
import net.fekepp.ldfu.server.exceptions.ParserException;

public interface FormatConverter {

	// Conversion

	public void convert() throws ParseException, ParserException, IOException, InterruptedException;

	// Base

	public URI getBaseUri();

	public void setBaseUri(URI baseUri);

	// Source

	public InputStream getInputStream();

	public void setInputStream(InputStream inputStream);

	public Format getInputFormat();

	// Sink

	public OutputStream getOutputStream();

	public void setOutputStream(OutputStream outputStream);

	public Format getOutputFormat();

}