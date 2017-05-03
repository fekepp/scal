package net.fekepp.ldps;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.fekepp.ldps.exception.ConverterException;
import net.fekepp.ldps.exception.ParseException;
import net.fekepp.ldps.exception.ParserException;

public interface Sink extends Description {

	public OutputStream getOutputStream();

	public void setOutputStream(OutputStream outputStream);

	public FormatConverter getFormatConverter();

	public void setFormatConverter(FormatConverter formatConverter);

	public void streamFrom(InputStream inputStream)
			throws ParseException, ParserException, ConverterException, IOException, InterruptedException;

	// public void streamFrom(Source source);

}