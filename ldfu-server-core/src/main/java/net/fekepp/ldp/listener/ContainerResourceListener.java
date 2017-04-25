package net.fekepp.ldp.listener;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.yars.nx.namespace.LDP;
import org.semanticweb.yars.nx.namespace.RDF;

import net.fekepp.ldp.FormatConverter;
import net.fekepp.ldp.FormatConverterListener;
import net.fekepp.ldp.ResourceListenerDelegate;
import net.fekepp.ldp.converter.RdfConverter;
import net.fekepp.ldp.converter.RdfConverterTripleListener;

public class ContainerResourceListener extends DefaultResourceListener {

	public ContainerResourceListener(ResourceListenerDelegate delegate) {
		super(delegate);
	}

	@Override
	public Set<FormatConverterListener> buildStorageFormatConverterListener(FormatConverter formatConverter) {

		Set<FormatConverterListener> listeners = new HashSet<FormatConverterListener>();

		if (formatConverter instanceof RdfConverter) {

			RdfConverterTripleListener rdfConverterTripleListener = new RdfConverterTripleListener();
			rdfConverterTripleListener.setPredicate(RDF.TYPE);
			rdfConverterTripleListener.setObject(LDP.CONTAINER);

			((RdfConverter) formatConverter).getFormatConverterListeners().add(rdfConverterTripleListener);

			listeners.add(rdfConverterTripleListener);

		}

		return listeners;
	}

}
