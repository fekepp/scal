package net.fekepp.ldfu.server.converter;

import java.io.IOException;

import org.apache.commons.compress.utils.IOUtils;

import net.fekepp.ldfu.server.exceptions.ParseException;
import net.fekepp.ldfu.server.exceptions.ParserException;
import net.fekepp.ldfu.server.formats.Format;

public class BinaryCopyConverter extends AbstractFormatConverter {

	public BinaryCopyConverter(Format inputFormat, Format outputFormat) {
		super(inputFormat, outputFormat);
	}

	@Override
	public void convert() throws ParseException, ParserException, IOException, InterruptedException {
		IOUtils.copy(inputStream, outputStream);
	}

}