package net.fekepp.ldp.converter;

import java.io.IOException;

import org.apache.commons.compress.utils.IOUtils;

import net.fekepp.ldp.Format;
import net.fekepp.ldp.exception.ParseException;
import net.fekepp.ldp.exception.ParserException;

public class BinaryCopyConverter extends AbstractFormatConverter {

	public BinaryCopyConverter(Format inputFormat, Format outputFormat) {
		super(inputFormat, outputFormat);
	}

	@Override
	public void convert() throws ParseException, ParserException, IOException, InterruptedException {
		IOUtils.copy(inputStream, outputStream);
	}

}