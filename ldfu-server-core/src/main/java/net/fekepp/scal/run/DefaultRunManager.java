package net.fekepp.scal.run;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Nodes;
import org.semanticweb.yars.nx.namespace.RDF;
import org.semanticweb.yars.nx.parser.InternalParserError;
import org.semanticweb.yars.nx.parser.NxParser;
import org.semanticweb.yars.nx.parser.RdfParser;
import org.semanticweb.yars.rdfxml.RdfXmlParser;
import org.semanticweb.yars.turtle.TurtleParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.aifb.datafu.ConstructQuery;
import edu.kit.aifb.datafu.Program;
import edu.kit.aifb.datafu.Query;
import edu.kit.aifb.datafu.SelectQuery;
import edu.kit.aifb.datafu.io.origins.InputOrigin;
import edu.kit.aifb.datafu.io.origins.RequestOrigin;
import edu.kit.aifb.datafu.parser.ProgramConsumerImpl;
import edu.kit.aifb.datafu.parser.QueryConsumerImpl;
import edu.kit.aifb.datafu.parser.notation3.Notation3Parser;
import edu.kit.aifb.datafu.parser.sparql.SparqlParser;
import net.fekepp.controllers.ControllerDelegate;
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
import net.fekepp.ldp.exception.ResourceNotFoundException;
import net.fekepp.ldp.format.NtriplesFormat;
import net.fekepp.ldp.format.RdfXmlFormat;
import net.fekepp.ldp.format.TurtleFormat;
import net.fekepp.ldp.listener.DefaultResourceListener;
import net.fekepp.ldp.model.RdfModel;
import net.fekepp.ldp.resource.ResourceDescription;
import net.fekepp.scal.RunManager;
import net.fekepp.scal.namespace.SCAL;

public class DefaultRunManager
		implements RunManager, ResourceListenerDelegate, RunControllerDelegate, ControllerDelegate {

	private static Map<String, RunController> runControllers = new ConcurrentHashMap<String, RunController>();

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

		logger.info("storage.getBaseUri() > {}", storage.getBase());
		logger.info("input.getBaseUri() > {}", input.getBase());

		String identifier = storage.getIdentifier();
		logger.info("Identifier > {}", identifier);

		URI base = input.getBase().resolve(identifier);
		logger.info("Base > {}", base);

		RdfParser parser = null;

		if (storage.getFormat().equals(NtriplesFormat.getInstance())) {
			parser = new NxParser(storage.getInputStream());
		} else if (storage.getFormat().equals(TurtleFormat.getInstance())) {
			parser = new TurtleParser(storage.getInputStream(), base);
		} else if (storage.getFormat().equals(RdfXmlFormat.getInstance())) {
			parser = new RdfXmlParser(storage.getInputStream(), base);
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
		if (runs != null && runs.size() == 1) {
			for (Node run : runs) {

				// TODO Create run
				logger.info("Create Run > {}", run.getLabel());

				RunController runController = new RunController();

				// Programs
				Set<Node> programs = callback.getPropertyObjects(SCAL.program, run);
				if (programs != null) {
					for (Node program : programs) {

						// TODO Create program
						logger.info("Create program > {}", program);

						// Program Declarations
						Set<Node> declarations = callback.getPropertyObjects(SCAL.declaration, program);
						if (declarations != null && declarations.size() == 1) {

							for (Node declaration : declarations) {

								logger.info("Add program declaration > URI > {}", declaration.getLabel());

								String programIdentifier = URI.create(declaration.getLabel()).getPath().substring(1);

								logger.info("Add program declaration > Identifier > {}", programIdentifier);

								Source programResource = null;

								try {
									programResource = resourceManager
											.getResource(new ResourceDescription(base, programIdentifier));
								}

								catch (ResourceNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								// Create origin based on request URI and method
								InputOrigin origin = new RequestOrigin(base.resolve(identifier),
										edu.kit.aifb.datafu.Request.Method.POST);

								Program programDeclaration = null;
								try {
									programDeclaration = parseProgram(origin, programResource.getInputStream());
								}

								catch (edu.kit.aifb.datafu.parser.notation3.ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								logger.info("Parsed program > {}:\n{}", programIdentifier, programDeclaration);

								runController.getPrograms().put(programIdentifier, programDeclaration);

							}

						}

						else {

							// TODO Handle missing or multiple program
							// declaration
							logger.info("Handle multiple program declaration > declarations={}",
									declarations != null ? declarations.size() : "null");

						}

						// TODO Add program to run
						logger.info("Add program to run");

					}
				}

				// Queries
				Set<Node> queries = callback.getPropertyObjects(SCAL.query, run);
				if (queries != null) {
					for (Node query : queries) {

						logger.info("Create query > {}", query);

						// Query Declarations
						Set<Node> declarations = callback.getPropertyObjects(SCAL.declaration, query);

						// Query Sink Resource
						Set<Node> sinks = callback.getPropertyObjects(SCAL.sink, query);

						if (declarations != null && declarations.size() == 1 && sinks != null && sinks.size() == 1) {

							logger.info("Add declaration with sink to query");

							for (Node declaration : declarations) {

								logger.info("Add query declaration > URI > {}", declaration.getLabel());

								String queryIdentifier = URI.create(declaration.getLabel()).getPath().substring(1);

								logger.info("Add query declaration > Identifier > {}", queryIdentifier);

								Source queryResource = null;

								try {
									queryResource = resourceManager
											.getResource(new ResourceDescription(base, queryIdentifier));
								}

								catch (ResourceNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								// Create origin based on request URI and method
								InputOrigin origin = new RequestOrigin(base.resolve(identifier),
										edu.kit.aifb.datafu.Request.Method.POST);

								Set<Query> queryDeclarations = null;
								try {
									queryDeclarations = parseQueries(origin, queryResource.getInputStream());
								}

								catch (edu.kit.aifb.datafu.parser.sparql.ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								// Split construct and select queries
								Set<ConstructQuery> constructQueryDeclarations = new HashSet<ConstructQuery>();
								Set<SelectQuery> selectQueryDeclarations = new HashSet<SelectQuery>();

								for (Query queryDeclaration : queryDeclarations) {

									if (queryDeclaration instanceof ConstructQuery) {
										logger.info("Parsed CONSTRUCT query > {}:\n{}", queryIdentifier,
												queryDeclaration);
										constructQueryDeclarations.add((ConstructQuery) queryDeclaration);
									}

									else if (queryDeclaration instanceof SelectQuery) {
										logger.info("Parsed SELECT query > {}:\n{}", queryIdentifier, queryDeclaration);
										selectQueryDeclarations.add((SelectQuery) queryDeclaration);
									}

								}

								// Add construct queries to run
								if (constructQueryDeclarations.size() > 0 && selectQueryDeclarations.size() == 0) {
									logger.warn("Added CONSTRUCT query");
									runController.getConstructQueryCollections().put(queryIdentifier,
											constructQueryDeclarations);
								}

								// Add select queries to run
								else if (constructQueryDeclarations.size() == 0 && selectQueryDeclarations.size() > 0) {
									logger.warn("Added SELECT query");
									runController.getSelectQueryCollections().put(queryIdentifier,
											selectQueryDeclarations);
								}

								else if (constructQueryDeclarations.size() == 0
										&& selectQueryDeclarations.size() == 0) {
									// TODO Error handling for CONSTRUCT and
									// SELECT queries
									logger.warn("Found CONSTRUCT and SELECT queries");
								}

								else {
									// TODO Error handling for no query
									logger.warn("Found no CONSTRUCT and SELECT queries");
								}

								for (Node sink : sinks) {

									logger.info("Add query sink > URI > {}", sink.getLabel());

									String sinkIdentifier = URI.create(sink.getLabel()).getPath().substring(1);

									logger.info("Add query sink > Identifier > {}", sinkIdentifier);

									QuerySinkResourceBridge querySink = new QuerySinkResourceBridge(resourceManager,
											new ResourceDescription(base, sinkIdentifier,
													RdfModel.getInstance().getDefaultFormat()));

									runController.getSinks().put(queryIdentifier, querySink);

								}

							}

						}

						else {

							// TODO Handle missing or multiple query declaration
							// and query sink
							logger.info(
									"Handle missing or multiple query declaration and query sink > declarations={} | sinks={}",
									declarations != null ? declarations.size() : "null",
									sinks != null ? sinks.size() : "null");

						}

						// logger.info("Add query to run");

					}
				}

				Set<Node> delayTrigger = callback.getClassSubjects(SCAL.DelayTrigger);
				Set<Node> resourceRequestedTriggers = callback.getClassSubjects(SCAL.ResourceRequestedTrigger);

				// Triggers
				Set<Node> triggers = callback.getPropertyObjects(SCAL.trigger, run);
				if (triggers != null && triggers.size() == 1) {
					for (Node trigger : triggers) {

						logger.info("Create trigger > {}", trigger);

						// Delay Triggers
						if (delayTrigger != null && delayTrigger.contains(trigger)) {

							logger.info("Trigger is DelayTrigger");

							// Delay
							Set<Node> delays = callback.getPropertyObjects(SCAL.delay, trigger);

							if (delays != null && delays.size() == 1) {

								for (Node delay : delays) {

									logger.info("Add delay trigger delay > {}", delay.getLabel());

									int delayValue = Integer.valueOf(delay.getLabel());

									runController.setDelay(delayValue);

								}
							}

							else {

								// TODO Handle missing or multiple delays
								logger.info("Handle missing or multiple delays");

							}

						}

						// Resource Requested Triggers
						if (resourceRequestedTriggers != null && resourceRequestedTriggers.contains(trigger)) {

							logger.info("Trigger is ResourceRequestedTrigger");

							// URIs
							Set<Node> uris = callback.getPropertyObjects(SCAL.uri, trigger);

							// Methods
							Set<Node> methods = callback.getPropertyObjects(SCAL.method, trigger);

							if (uris != null && uris.size() == 1 && methods.size() > 0) {

								for (Node uri : uris) {

									logger.info("Add resource requested trigger uri > {}", uri.getLabel());

									for (Node method : methods) {

										logger.info("Add resource requested trigger method > {}", method.getLabel());

									}

								}
							}

							else {

								// TODO Handle missing or multiple uris or
								// missing methods
								logger.info("Handle missing or multiple uris or missing methods");

							}
						}

					}
				}

				else {

					// TODO Handle missing or multiple triggers
					logger.info("Handle missing or multiple triggers");

				}

				runControllers.put(identifier, runController);

				runController.setEvaluationControllerDelegate(this);

				runController.setDelegate(this);

				logger.info("STARTING");
				runController.start();

			}

		}

		else if (runs != null)

		{

			// TODO Handle multiple runs
			logger.info("Handle multiple runs");

		}

		else {

			// TODO Handle missing run
			logger.info("Handle missing run");

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

	public static Program parseProgram(InputOrigin origin, InputStream inputStream)
			throws edu.kit.aifb.datafu.parser.notation3.ParseException, IOException {

		// Create a program consumer for the program to be returned
		ProgramConsumerImpl programConsumer = new ProgramConsumerImpl(origin);

		// Try to parse the input stream
		try {
			Notation3Parser parser = new Notation3Parser(inputStream);
			parser.parse(programConsumer, origin);
		}

		// Always close the input stream
		finally {
			inputStream.close();
		}

		// Return the program
		return programConsumer.getProgram(origin);

	}

	public static Set<Query> parseQueries(InputOrigin origin, InputStream inputStream)
			throws edu.kit.aifb.datafu.parser.sparql.ParseException, IOException {

		// Create a set of queries to be returned
		Set<Query> queries = new HashSet<Query>();

		// Create a query consumer for queries to be returned
		QueryConsumerImpl queryConsumer = new QueryConsumerImpl(origin);

		// Try to parse the input stream
		try {
			SparqlParser parser = new SparqlParser(inputStream);
			parser.parse(queryConsumer, origin);
		}

		// Always close the input stream
		finally {
			inputStream.close();
		}

		// Add possible contained select query to returned queries
		queries.addAll(queryConsumer.getSelectQueries());

		// Add possible contained construct queries to returned queries
		queries.addAll(queryConsumer.getConstructQueries());

		// Return the queries
		return queries;

	}

	@Override
	public Set<Nodes> getNodes() {

		logger.info("Controller > Get nodes");

		Set<Nodes> nodesSet = new HashSet<Nodes>();

		// nodesSet.add(
		// new Nodes(new Resource("http://test/"), new
		// Resource("http://scenario#alarm"), new Literal("true")));
		//
		// nodesSet.add(
		// new Nodes(new Resource("http://asdf/"), new Resource("http://asdf/"),
		// new Literal("asdf")));

		return nodesSet;

	}

	@Override
	public void onControllerStarted() {
		logger.info("Controller > Started");
	}

	@Override
	public void onControllerExecuted() {
		logger.info("Controller > Executed");
	}

	@Override
	public void onControllerStopped() {
		logger.info("Controller > Stopped");
	}

	@Override
	public void onControllerStartupException(Exception e) {
		logger.error("Controller > Startup exception", e);
	}

	@Override
	public void onControllerExecutionException(Exception e) {
		logger.error("Controller > Execution exception", e);
	}

	@Override
	public void onControllerShutdownException(Exception e) {
		logger.error("Controller > Shutdown exception", e);
	}

}
