package net.fekepp.ldfu.server.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.fekepp.ldfu.server.data.converters.FormatConverter;
import net.fekepp.ldfu.server.exceptions.ParseException;
import net.fekepp.ldfu.server.exceptions.ParserException;

public interface Source extends Description {

	public InputStream getInputStream();

	public FormatConverter getFormatConverter();

	public void streamTo(OutputStream outputStream)
			throws ParseException, ParserException, IOException, InterruptedException;

	// public void streamTo(Sink sink);

}