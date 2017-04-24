package net.fekepp.ldp.parser;

import java.io.InputStream;

import net.fekepp.ldp.Parser;

public abstract class AbstractParser implements Parser {

	private InputStream inputStream;

	public AbstractParser(InputStream inputStream) {
		this.inputStream = inputStream;
	}

}