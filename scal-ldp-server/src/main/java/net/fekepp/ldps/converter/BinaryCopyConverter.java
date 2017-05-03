package net.fekepp.ldps.converter;

import java.io.IOException;

import org.apache.commons.compress.utils.IOUtils;

import net.fekepp.ldps.Format;
import net.fekepp.ldps.exception.ParseException;
import net.fekepp.ldps.exception.ParserException;

public class BinaryCopyConverter extends AbstractFormatConverter {

	public BinaryCopyConverter(Format inputFormat, Format outputFormat) {
		super(inputFormat, outputFormat);
	}

	@Override
	public void convert() throws ParseException, ParserException, IOException, InterruptedException {
		IOUtils.copy(inputStream, outputStream);
	}

}