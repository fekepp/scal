package net.fekepp.ldps.converter;

import java.io.IOException;

import org.semanticweb.yars.nx.parser.Callback;
import org.semanticweb.yars.nx.parser.InternalParserError;
import org.semanticweb.yars.nx.parser.NxParser;
import org.semanticweb.yars.nx.parser.RdfParser;
import org.semanticweb.yars.rdfxml.RdfXmlParser;
import org.semanticweb.yars.turtle.TurtleParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fekepp.ldps.Format;
import net.fekepp.ldps.FormatConverterListener;
import net.fekepp.ldps.exception.ConverterException;
import net.fekepp.ldps.exception.ParseException;
import net.fekepp.ldps.exception.ParserException;
import net.fekepp.ldps.format.NtriplesFormat;
import net.fekepp.ldps.format.RdfXmlFormat;
import net.fekepp.ldps.format.TurtleFormat;
import net.fekepp.ldps.serializer.CallbackNtriplesSerializer;
import net.fekepp.ldps.serializer.CallbackRdfXmlSerializer;
import net.fekepp.ldps.serializer.CallbackTurtleSerializer;

public class RdfConverter extends AbstractFormatConverter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public RdfConverter(Format inputFormat, Format outputFormat) {
		super(inputFormat, outputFormat);
	}

	@Override
	public void convert()
			throws ParseException, ParserException, ConverterException, IOException, InterruptedException {

		if (base == null) {
			logger.error("Base URI is null > Abort conversion");
			throw new ConverterException();
		}

		// The parser that will parse the input of the input stream
		RdfParser parser = null;

		// Create a parser for the input format based on the input stream
		if (inputFormat.equals(NtriplesFormat.getInstance())) {
			parser = new NxParser(inputStream);
		} else if (inputFormat.equals(TurtleFormat.getInstance())) {
			parser = new TurtleParser(inputStream, base);
		} else if (inputFormat.equals(RdfXmlFormat.getInstance())) {
			parser = new RdfXmlParser(inputStream, base);
		}

		if (parser != null) {
			logger.info("Parser found > inputFormat={} | parser={}", inputFormat, parser);
		} else {
			logger.error("Parser not found > Abort conversion > inputFormat={}", inputFormat);
			throw new ConverterException();
		}

		// The current callback that can be encapsulated by further callbacks
		Callback callback = null;

		// Create a serializer based on the output format as last callback based
		// on the output stream
		if (outputFormat.equals(NtriplesFormat.getInstance())) {
			callback = new CallbackNtriplesSerializer(outputStream);
		} else if (outputFormat.equals(TurtleFormat.getInstance())) {
			callback = new CallbackTurtleSerializer(outputStream);
		} else if (outputFormat.equals(RdfXmlFormat.getInstance())) {
			callback = new CallbackRdfXmlSerializer(outputStream);
		}

		if (callback != null) {
			logger.info("Serializer found > outputFormat={} | serializer={}", outputFormat, callback);
		} else {
			logger.error("Serializer not found > Abort conversion > outputFormat={}", outputFormat);
			throw new ConverterException();
		}

		// Layer all listener callbacks on top of the serializer callback
		for (FormatConverterListener formatConverterListener : formatConverterListeners) {
			if (formatConverterListener instanceof RdfConverterTripleListener) {
				((RdfConverterTripleListener) formatConverterListener).setCallback(callback);
				callback = (RdfConverterTripleListener) formatConverterListener;
			}
		}

		logger.info("Parsing > inputFormat={} | inputStream={} | outputFormat={} | outputStream={} | callback={}",
				inputFormat, inputStream, outputFormat, outputStream, callback);

		// Parse the input from the input stream and pipe the output through the
		// layered callbacks to the output stream
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