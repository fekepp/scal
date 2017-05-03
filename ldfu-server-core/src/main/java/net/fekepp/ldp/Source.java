package net.fekepp.ldp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.fekepp.ldp.exception.ConverterException;
import net.fekepp.ldp.exception.ParseException;
import net.fekepp.ldp.exception.ParserException;

public interface Source extends Description {

	public InputStream getInputStream();

	public void setInputStream(InputStream inputStream);

	public FormatConverter getFormatConverter();

	public void setFormatConverter(FormatConverter formatConverter);

	public void streamTo(OutputStream outputStream)
			throws ParseException, ParserException, ConverterException, IOException, InterruptedException;

	// public void streamTo(Sink sink);

}