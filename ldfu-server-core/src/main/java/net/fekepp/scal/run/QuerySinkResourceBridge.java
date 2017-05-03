package net.fekepp.scal.run;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.semanticweb.yars.nx.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.aifb.datafu.Binding;
import edu.kit.aifb.datafu.Sink;
import edu.kit.aifb.datafu.io.mediatypes.MediaType;
import edu.kit.aifb.datafu.io.serialisers.NTriplesSerialiser;
import edu.kit.aifb.datafu.io.serialisers.RdfXmlSerialiser;
import edu.kit.aifb.datafu.io.serialisers.Serialiser;
import edu.kit.aifb.datafu.io.serialisers.TsvSerialiser;
import edu.kit.aifb.datafu.io.serialisers.TurtleSerialiser;
import net.fekepp.ldps.Description;
import net.fekepp.ldps.ResourceManager;
import net.fekepp.ldps.exception.ContainerIdentifierExpectedException;
import net.fekepp.ldps.exception.ConverterException;
import net.fekepp.ldps.exception.ParentNotFoundException;
import net.fekepp.ldps.exception.ParseException;
import net.fekepp.ldps.exception.ParserException;
import net.fekepp.ldps.exception.ResourceIdentifierExpectedException;
import net.fekepp.ldps.format.NtriplesFormat;
import net.fekepp.ldps.format.RdfXmlFormat;
import net.fekepp.ldps.format.TsvFormat;
import net.fekepp.ldps.format.TurtleFormat;

public class QuerySinkResourceBridge implements Sink {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private ResourceManager resourceManager;
	private Description description;
	private QuerySinkResourceBridgeSource source;
	private OutputStream outputStream;
	private Serialiser serializer;

	public QuerySinkResourceBridge(ResourceManager resourceManager, Description description) {
		this.resourceManager = resourceManager;
		this.description = description;
	}

	@Override
	public void open() throws IOException {

		logger.info("Sink -> Resource > Opened > {}", description.getIdentifier());

		source = new QuerySinkResourceBridgeSource(description.getBase(), description.getIdentifier(),
				description.getFormat());

		try {
			resourceManager.setResource(source);
		} catch (ContainerIdentifierExpectedException | ResourceIdentifierExpectedException | ParentNotFoundException
				| ParseException | ParserException | ConverterException | InterruptedException e) {
			// TODO Appropriate error handling
			throw new IOException(e);
		}

		outputStream = source.getOutputStream();

		if (description.getFormat().equals(NtriplesFormat.getInstance())) {
			serializer = new NTriplesSerialiser(outputStream);
		} else if (description.getFormat().equals(TurtleFormat.getInstance())) {
			serializer = new TurtleSerialiser(outputStream);
		} else if (description.getFormat().equals(RdfXmlFormat.getInstance())) {
			serializer = new RdfXmlSerialiser(outputStream);
		} else if (description.getFormat().equals(TsvFormat.getInstance())) {
			serializer = new TsvSerialiser(outputStream);
		}

		if (serializer == null) {
			throw new IOException("Serializer not found > " + description.getFormat());
		}

		serializer.start();

	}

	@Override
	public void consume(Binding binding) throws InterruptedException {
		logger.info("Consumed binding > {}", binding);
		serializer.consume(binding);
	}

	@Override
	public void consume(Collection<Binding> bindings) throws InterruptedException {
		for (Binding binding : bindings) {
			logger.info("Consumed bindings > {}", binding);
		}
		serializer.consume(bindings);
	}

	@Override
	public void close() throws IOException {

		logger.info("Sink -> Resource > Closed > {}", description.getIdentifier());

		serializer.end();
		IOUtils.closeQuietly(outputStream);

		source = null;
		outputStream = null;
		serializer = null;

	}

	@Override
	public void setMediaType(MediaType mediaType) {
		// Ignore
	}

	@Override
	public MediaType getMediaType() {
		// Ignore
		return null;
	}

	@Override
	public void setVariables(List<Variable> variables) {
		// Ignore
	}

}
