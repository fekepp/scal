package net.fekepp.ldfu.server.data.converters;

import java.io.IOException;

import org.semanticweb.yars.nx.parser.Callback;
import org.semanticweb.yars.nx.parser.InternalParserError;
import org.semanticweb.yars.nx.parser.NxParser;
import org.semanticweb.yars.nx.parser.RdfParser;
import org.semanticweb.yars.rdfxml.RdfXmlParser;
import org.semanticweb.yars.turtle.TurtleParser;

import net.fekepp.ldfu.server.data.formats.Format;
import net.fekepp.ldfu.server.data.formats.NtriplesFormat;
import net.fekepp.ldfu.server.data.formats.RdfXmlFormat;
import net.fekepp.ldfu.server.data.formats.TurtleFormat;
import net.fekepp.ldfu.server.exceptions.ParseException;
import net.fekepp.ldfu.server.exceptions.ParserException;
import net.fekepp.ldfu.server.formats.serializers.CallbackNtriplesSerializer;
import net.fekepp.ldfu.server.formats.serializers.CallbackRdfXmlSerializer;
import net.fekepp.ldfu.server.formats.serializers.CallbackTurtleSerializer;

public class RdfConverter extends AbstractFormatConverter {

	public RdfConverter(Format inputFormat, Format outputFormat) {
		super(inputFormat, outputFormat);
	}

	@Override
	public void convert() throws ParseException, ParserException, IOException, InterruptedException {

		RdfParser parser = null;
		Callback callback = null;

		if (inputFormat.equals(NtriplesFormat.getInstance())) {
			parser = new NxParser(inputStream);
		} else if (inputFormat.equals(TurtleFormat.getInstance())) {
			parser = new TurtleParser(inputStream, base);
		} else if (inputFormat.equals(RdfXmlFormat.getInstance())) {
			parser = new RdfXmlParser(inputStream, base);
		}

		if (outputFormat.equals(NtriplesFormat.getInstance())) {
			callback = new CallbackNtriplesSerializer(outputStream);
		} else if (outputFormat.equals(TurtleFormat.getInstance())) {
			callback = new CallbackTurtleSerializer(outputStream);
		} else if (outputFormat.equals(RdfXmlFormat.getInstance())) {
			callback = new CallbackRdfXmlSerializer(outputStream);
		}

		try {
			parser.parse(callback);
		}

		catch (org.semanticweb.yars.nx.parser.ParseException e) {
			throw new ParseException();
		}

		catch (InternalParserError e) {
			throw new ParserException();
		}

	}

}