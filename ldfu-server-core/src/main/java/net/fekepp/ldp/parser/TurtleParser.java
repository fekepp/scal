package net.fekepp.ldp.parser;

import java.io.InputStream;

public class TurtleParser extends AbstractParser {

	private org.semanticweb.yars.turtle.TurtleParser parser;

	public TurtleParser(InputStream inputStream) {
		super(inputStream);
	}

	@Override
	public void parse(InputStream inputStream) {

	}

}