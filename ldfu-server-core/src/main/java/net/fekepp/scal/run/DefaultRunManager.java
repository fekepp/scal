package net.fekepp.scal.run;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.namespace.RDF;
import org.semanticweb.yars.nx.parser.InternalParserError;
import org.semanticweb.yars.nx.parser.NxParser;
import org.semanticweb.yars.nx.parser.RdfParser;
import org.semanticweb.yars.rdfxml.RdfXmlParser;
import org.semanticweb.yars.turtle.TurtleParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import net.fekepp.ldp.format.NtriplesFormat;
import net.fekepp.ldp.format.RdfXmlFormat;
import net.fekepp.ldp.format.TurtleFormat;
import net.fekepp.ldp.listener.DefaultResourceListener;
import net.fekepp.scal.RunManager;
import net.fekepp.scal.namespace.SCAL;

public class DefaultRunManager implements RunManager, ResourceListenerDelegate {

	private final Logger logger = LoggerFactory.getLogger(getClass());

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

		logger.info("PROCESS");

		logger.info("storage.getBaseUri() > {}", storage.getBaseUri());
		logger.info("input.getBaseUri() > {}", input.getBaseUri());

		RdfParser parser = null;

		if (storage.getFormat().equals(NtriplesFormat.getInstance())) {
			parser = new NxParser(storage.getInputStream());
		} else if (storage.getFormat().equals(TurtleFormat.getInstance())) {
			parser = new TurtleParser(storage.getInputStream(), input.getBaseUri());
		} else if (storage.getFormat().equals(RdfXmlFormat.getInstance())) {
			parser = new RdfXmlParser(storage.getInputStream(), input.getBaseUri());
		}

		if (parser != null) {
			logger.info("Parser found > inputFormat={} | parser={}", storage.getFormat(), parser);
		} else {
			logger.error("Parser not found > Abort conversion > inputFormat={}", storage.getFormat());
			throw new ConverterException();
		}

		CallbackRdfClassPropertyReader callback = new CallbackRdfClassPropertyReader();
		callback.getClasses().addAll(SCAL.getClasses());
		callback.getProperties().addAll(SCAL.getProperties());

		try {
			parser.parse(callback);
		}

		catch (org.semanticweb.yars.nx.parser.ParseException e) {
			throw new ParseException();
		}

		catch (InternalParserError e) {
			throw new ParserException();
		}

		// Runs
		Set<Node> runs = callback.getClassSubjects(SCAL.Run);
		if (runs != null) {
			for (Node run : runs) {

				// TODO Create run
				logger.info("Create Run > {}", run);

				// Programs
				Set<Node> programs = callback.getPropertyObjects(SCAL.program, run);
				if (programs != null) {
					for (Node program : programs) {

						// TODO Create program
						logger.info("Create program > {}", program);

						// Program Declarations
						Set<Node> declarations = callback.getPropertyObjects(SCAL.declaration, program);
						if (declarations != null) {

							// TODO Add declaration to program
							logger.info("Add program declaration");

						}

						else {

							// TODO Handle missing program declaration
							logger.info("Handle missing program declaration");

						}

						// TODO Add program to run
						logger.info("Add program to run");

					}
				}

				// Queries
				Set<Node> queries = callback.getPropertyObjects(SCAL.query, run);
				if (queries != null) {
					for (Node query : queries) {

						// TODO Create query
						logger.info("Create query > {}", query);

						// Query Declarations
						Set<Node> declarations = callback.getPropertyObjects(SCAL.declaration, query);
						if (declarations != null) {

							// TODO Add declaration to query
							logger.info("Add declaration to query");

						}

						else {

							// TODO Handle missing query declaration
							logger.info("Handle missing query declaration");

						}

						// Query Sink Resource
						Set<Node> sinks = callback.getPropertyObjects(SCAL.sink, query);
						if (sinks != null) {

							// TODO Add sink to query
							logger.info("Add sink resource to query");

						}

						else {

							// TODO Handle missing query sink
							logger.info("Handle missing query sink");

						}

						// TODO Add query to run
						logger.info("Add query to run");

					}
				}

			}
		}

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
