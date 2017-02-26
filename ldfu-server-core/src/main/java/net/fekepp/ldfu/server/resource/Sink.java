package net.fekepp.ldfu.server.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.fekepp.ldfu.server.converter.FormatConverter;
import net.fekepp.ldfu.server.exceptions.ParseException;
import net.fekepp.ldfu.server.exceptions.ParserException;

public interface Sink extends Description {

	public OutputStream getOutputStream();

	public FormatConverter getFormatConverter();

	public void streamFrom(InputStream inputStream)
			throws ParseException, ParserException, IOException, InterruptedException;

	// public void streamFrom(Source source);

}