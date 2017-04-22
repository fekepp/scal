package net.fekepp.ldfu.server.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Set;

import net.fekepp.ldfu.server.exceptions.ParseException;
import net.fekepp.ldfu.server.exceptions.ParserException;

public interface FormatConverter {

	// Conversion

	public void convert() throws ParseException, ParserException, IOException, InterruptedException;

	// Listeners
	public Set<FormatConverterListener> getFormatConverterListeners();

	// Base

	public URI getBaseUri();

	public void setBaseUri(URI baseUri);

	// Input

	public InputStream getInputStream();

	public void setInputStream(InputStream inputStream);

	public Format getInputFormat();

	// Output

	public OutputStream getOutputStream();

	public void setOutputStream(OutputStream outputStream);

	public Format getOutputFormat();

}