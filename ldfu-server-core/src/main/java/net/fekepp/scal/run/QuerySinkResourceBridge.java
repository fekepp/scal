package net.fekepp.scal.run;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.semanticweb.yars.nx.Variable;

import edu.kit.aifb.datafu.Binding;
import edu.kit.aifb.datafu.Sink;
import edu.kit.aifb.datafu.io.mediatypes.MediaType;
import edu.kit.aifb.datafu.io.serialisers.NTriplesSerialiser;
import edu.kit.aifb.datafu.io.serialisers.RdfXmlSerialiser;
import edu.kit.aifb.datafu.io.serialisers.Serialiser;
import edu.kit.aifb.datafu.io.serialisers.TsvSerialiser;
import edu.kit.aifb.datafu.io.serialisers.TurtleSerialiser;
import net.fekepp.ldp.Description;
import net.fekepp.ldp.ResourceManager;
import net.fekepp.ldp.exception.ContainerIdentifierExpectedException;
import net.fekepp.ldp.exception.ConverterException;
import net.fekepp.ldp.exception.ParentNotFoundException;
import net.fekepp.ldp.exception.ParseException;
import net.fekepp.ldp.exception.ParserException;
import net.fekepp.ldp.exception.ResourceIdentifierExpectedException;
import net.fekepp.ldp.format.NtriplesFormat;
import net.fekepp.ldp.format.RdfXmlFormat;
import net.fekepp.ldp.format.TsvFormat;
import net.fekepp.ldp.format.TurtleFormat;

public class QuerySinkResourceBridge implements Sink {

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

		source = new QuerySinkResourceBridgeSource(description.getBaseUri(), description.getIdentifier(),
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

		serializer.start();

	}

	@Override
	public void consume(Binding binding) throws InterruptedException {
		serializer.consume(binding);
	}

	@Override
	public void consume(Collection<Binding> bindings) throws InterruptedException {
		serializer.consume(bindings);
	}

	@Override
	public void close() throws IOException {

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
