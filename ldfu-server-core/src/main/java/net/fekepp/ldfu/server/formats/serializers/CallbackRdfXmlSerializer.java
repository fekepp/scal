package net.fekepp.ldfu.server.formats.serializers;

import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Nodes;
import org.semanticweb.yars.nx.parser.Callback;

import edu.kit.aifb.datafu.bindings.BindingSimple;
import edu.kit.aifb.datafu.io.serialisers.RdfXmlSerialiser;

public class CallbackRdfXmlSerializer extends Callback {

	private RdfXmlSerialiser serializer;
	private OutputStream outputStream;

	public CallbackRdfXmlSerializer(OutputStream outputStream) {
		this.outputStream = outputStream;
		serializer = new RdfXmlSerialiser(outputStream);
	}

	@Override
	protected void startDocumentInternal() {
		serializer.start();
	}

	@Override
	protected void processStatementInternal(Node[] node) {
		serializer.consume(new BindingSimple(new Nodes(node)));
	}

	@Override
	protected void endDocumentInternal() {
		serializer.end();
		IOUtils.closeQuietly(outputStream);
	}

}