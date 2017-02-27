package net.fekepp.ldfu.server.data.converters;

import java.io.IOException;

import org.apache.commons.compress.utils.IOUtils;

import net.fekepp.ldfu.server.data.Format;
import net.fekepp.ldfu.server.exceptions.ParseException;
import net.fekepp.ldfu.server.exceptions.ParserException;

public class BinaryCopyConverter extends AbstractFormatConverter {

	public BinaryCopyConverter(Format inputFormat, Format outputFormat) {
		super(inputFormat, outputFormat);
	}

	@Override
	public void convert() throws ParseException, ParserException, IOException, InterruptedException {
		IOUtils.copy(inputStream, outputStream);
	}

}