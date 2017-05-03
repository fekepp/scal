package net.fekepp.ldps.parser;

import java.io.InputStream;

import net.fekepp.ldps.Parser;

public abstract class AbstractParser implements Parser {

	private InputStream inputStream;

	public AbstractParser(InputStream inputStream) {
		this.inputStream = inputStream;
	}

}