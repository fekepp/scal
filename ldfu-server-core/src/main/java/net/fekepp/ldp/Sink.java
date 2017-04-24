package net.fekepp.ldp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.fekepp.ldp.exception.ConverterException;
import net.fekepp.ldp.exception.ParseException;
import net.fekepp.ldp.exception.ParserException;

public interface Sink extends Description {

	public OutputStream getOutputStream();

	public FormatConverter getFormatConverter();

	public void streamFrom(InputStream inputStream)
			throws ParseException, ParserException, ConverterException, IOException, InterruptedException;

	// public void streamFrom(Source source);

}