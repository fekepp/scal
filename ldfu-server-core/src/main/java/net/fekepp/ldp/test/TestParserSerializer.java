package net.fekepp.ldp.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.semanticweb.yars.nx.parser.NxParser;
import org.semanticweb.yars.nx.parser.ParseException;
import org.semanticweb.yars.turtle.TurtleParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.aifb.datafu.io.serialisers.NTriplesSerialiser;
import net.fekepp.ldp.serializer.CallbackTurtleSerializer;

public class TestParserSerializer {

	private static final Logger logger = LoggerFactory.getLogger(TestParserSerializer.class.getName());

	public static void main(String[] args)
			throws URISyntaxException, IOException, InterruptedException, ParseException {

		URI base = new URI("http://test.fekepp.net/");
		Path directory = Paths.get("../doc");
		Path input = directory.resolve("input.ttl");
		Path output = directory.resolve("output.ttl");

		logger.info("input > path={} | exists={}", input, Files.exists(input));
		logger.info("output > path={} | exists={}", output, Files.exists(output));

		InputStream inputStream = Files.newInputStream(input);
		OutputStream outputStream = Files.newOutputStream(output);

		TurtleParser turtleParser = new TurtleParser(inputStream, base);
		// TurtleSerialiser turtleSerializer = new
		// TurtleSerialiser(outputStream);
		CallbackTurtleSerializer callbackTurtleSerializer = new CallbackTurtleSerializer(outputStream);

		NxParser ntriplesParser = new NxParser(inputStream);
		NTriplesSerialiser ntriplesSerializer = new NTriplesSerialiser(outputStream);

		// Callback callbackLogger = new CallbackLogger();

		turtleParser.parse(callbackTurtleSerializer);

		// serializer.consume(arg0);

		// NxParser nxParser = new
		// NxParser(Files.newInputStream(filePathAbsoluteNormalized));
		// NxParser nxParser = new
		// NxParser(Files.newInputStream(filePathAbsoluteNormalized), base);
		// nxParser.parse(callbackLogger);
	}

}