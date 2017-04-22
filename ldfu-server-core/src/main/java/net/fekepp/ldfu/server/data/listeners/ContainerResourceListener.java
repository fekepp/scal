package net.fekepp.ldfu.server.data.listeners;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.yars.nx.Resource;

import net.fekepp.ldfu.server.data.FormatConverter;
import net.fekepp.ldfu.server.data.FormatConverterListener;
import net.fekepp.ldfu.server.data.ModelConverter;
import net.fekepp.ldfu.server.data.ModelConverterListener;
import net.fekepp.ldfu.server.data.converters.RdfConverterTripleListener;

public class ContainerResourceListener extends AbstractListener {

	public ContainerResourceListener(ListenerDelegate delegate) {
		super(delegate);
	}

	@Override
	public Set<ModelConverterListener> buildStorageModelConverterListener(ModelConverter modelConverter) {
		return null;
	}

	@Override
	public Set<FormatConverterListener> buildStorageFormatConverterListener(FormatConverter formatConverter) {

		Set<FormatConverterListener> listeners = new HashSet<FormatConverterListener>();

		RdfConverterTripleListener rdfConverterTripleListener = new RdfConverterTripleListener();
		rdfConverterTripleListener.setPredicate(new Resource("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"));
		rdfConverterTripleListener.setObject(new Resource("http://www.w3.org/ns/ldp#Container"));

		listeners.add(rdfConverterTripleListener);

		return listeners;
	}

	@Override
	public Set<ModelConverterListener> buildInputModelConverterListener(ModelConverter modelConverter) {
		return null;
	}

	@Override
	public Set<FormatConverterListener> buildInputFormatConverterListener(FormatConverter formatConverter) {
		return null;
	}

	@Override
	public Set<ModelConverterListener> buildOutputModelConverterListener(ModelConverter modelConverter) {
		return null;
	}

	@Override
	public Set<FormatConverterListener> buildOutputFormatConverterListener(FormatConverter formatConverter) {
		return null;
	}

}
