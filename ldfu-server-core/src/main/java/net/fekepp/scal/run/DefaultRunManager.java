package net.fekepp.scal.run;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.yars.nx.namespace.RDF;

import net.fekepp.ldp.FormatConverter;
import net.fekepp.ldp.FormatConverterListener;
import net.fekepp.ldp.Method;
import net.fekepp.ldp.ResourceListener;
import net.fekepp.ldp.ResourceListenerDelegate;
import net.fekepp.ldp.ResourceManager;
import net.fekepp.ldp.Source;
import net.fekepp.ldp.converter.RdfConverter;
import net.fekepp.ldp.converter.RdfConverterTripleListener;
import net.fekepp.ldp.exception.ContainerIdentifierExpectedException;
import net.fekepp.ldp.exception.ConverterException;
import net.fekepp.ldp.exception.ParentNotFoundException;
import net.fekepp.ldp.exception.ParseException;
import net.fekepp.ldp.exception.ParserException;
import net.fekepp.ldp.exception.ResourceIdentifierExpectedException;
import net.fekepp.ldp.listener.DefaultResourceListener;
import net.fekepp.scal.RunManager;
import net.fekepp.scal.namespace.SCAL;

public class DefaultRunManager implements RunManager, ResourceListenerDelegate {

	private ResourceManager resourceManager;

	public DefaultRunManager(ResourceManager resourceManager) {

		// Process arguments
		this.resourceManager = resourceManager;

		// Initialize instance
		registerResourceListeners();

	}

	@Override
	public Source process(Source storage, Source input)
			throws ContainerIdentifierExpectedException, ResourceIdentifierExpectedException, ParentNotFoundException,
			ParseException, ParserException, ConverterException, InterruptedException, IOException {

		return null;

	}

	private void registerResourceListeners() {

		Set<ResourceListener> resourceListeners = resourceManager.getResourceListeners();

		DefaultResourceListener resourceListener = new DefaultResourceListener(this) {

			@Override
			public Set<FormatConverterListener> buildStorageFormatConverterListener(FormatConverter formatConverter) {

				Set<FormatConverterListener> listeners = new HashSet<FormatConverterListener>();

				if (formatConverter instanceof RdfConverter) {

					RdfConverterTripleListener rdfConverterTripleListener = new RdfConverterTripleListener();
					rdfConverterTripleListener.setPredicate(RDF.TYPE);
					rdfConverterTripleListener.setObject(SCAL.Run);

					((RdfConverter) formatConverter).getFormatConverterListeners().add(rdfConverterTripleListener);

					listeners.add(rdfConverterTripleListener);

				}

				return listeners;

			}

			@Override
			public Set<FormatConverterListener> buildInputFormatConverterListener(FormatConverter formatConverter) {
				return null;
			}

		};

		resourceListener.getMethods().add(Method.PRO);

		resourceListeners.add(resourceListener);

	}

}
