package net.fekepp.scal.run;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.semanticweb.yars.nx.Nodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.aifb.datafu.ConstructQuery;
import edu.kit.aifb.datafu.Program;
import edu.kit.aifb.datafu.SelectQuery;
import edu.kit.aifb.datafu.Sink;
import edu.kit.aifb.datafu.engine.EvaluateProgram;
import edu.kit.aifb.datafu.io.origins.InternalOrigin;
import edu.kit.aifb.datafu.planning.EvaluateProgramConfig;
import edu.kit.aifb.datafu.planning.EvaluateProgramConfig.ThreadingModel;
import edu.kit.aifb.datafu.planning.EvaluateProgramGenerator;
import net.fekepp.controllers.AbstractController;

public class RunController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	protected RunControllerDelegate delegate;

	private Map<String, Program> programs = new ConcurrentHashMap<String, Program>();

	private Map<String, Collection<ConstructQuery>> constructQueryCollections = new ConcurrentHashMap<String, Collection<ConstructQuery>>();

	private Map<String, Collection<SelectQuery>> selectQueryCollections = new ConcurrentHashMap<String, Collection<SelectQuery>>();

	private Map<String, Sink> sinks = new ConcurrentHashMap<String, Sink>();

	private EvaluateProgramConfig configuration = new EvaluateProgramConfig();

	private EvaluateProgramGenerator engine;

	private EvaluateProgram evaluation;

	private int delay;

	public RunController() {

		// Currently, set threading model to serial to omit any side effects
		configuration.setThreadingModel(ThreadingModel.SERIAL);

	}

	public void executeStep() {

		// Manually evaluate
		// TODO Figure out if external triggering of the evaluation of a step
		// could be possible while time-based evaluation is running, i.e.,
		// external triggering must respect a currently evaluating step and be
		// executed afterwards or in parallel
		if (delay < 1) {
			evaluate();
		}

	}

	@Override
	protected void startup() throws Exception {

		// Create a program for queries
		Program program = new Program(new InternalOrigin("api"));

		// Handle construct queries collection
		for (Entry<String, Collection<ConstructQuery>> entry : constructQueryCollections.entrySet()) {

			Sink sink = sinks.get(entry.getKey());

			if (sink != null) {

				// Add all construct queries to the program
				for (ConstructQuery query : entry.getValue()) {
					program.registerConstructQuery(query, sink);
				}

			}

			else {
				// TODO Appropriate error handling or ignore
				throw new Exception();
			}

		}

		// Handle select queries collection
		for (Entry<String, Collection<SelectQuery>> entry : selectQueryCollections.entrySet()) {

			Sink sink = sinks.get(entry.getKey());

			if (sink != null) {

				// Add all construct queries to the program
				for (SelectQuery query : entry.getValue()) {
					// program.registerSelectQuery(query, bindingConsumerSink);
					program.registerSelectQuery(query, sink);
				}

			}

			else {
				// TODO Appropriate error handling or ignore
				throw new Exception();
			}

		}

		// Join programs and queries
		Set<Program> programsWithQueries = new HashSet<Program>(programs.values());
		programsWithQueries.add(program);

		// Create the evaluation generator
		engine = new EvaluateProgramGenerator(programsWithQueries, configuration);

		// Create a new evaluation
		evaluation = engine.getEvaluateProgram();

		// Start the evaluation
		evaluation.start();

	}

	@Override
	protected void execute() throws Exception {

		// Continuously evaluate if a delay is set until shutdown
		// TODO: Support frequencies, not just delays
		if (delay > 1) {
			logger.info("Execution with delay");
			while (execution && !shutdown) {
				evaluate();
			}
		}

		// Wait until shutdown (default execution)
		else {
			logger.info("Execution with trigger");
			super.execute();
		}

	}

	@Override
	protected void shutdown() throws Exception {

		// Check if current evaluation exists
		if (evaluation != null) {

			// Shutdown current evaluation
			try {
				evaluation.shutdown();
			}

			catch (InterruptedException e) {
				if (super.delegate != null) {
					super.delegate.onControllerShutdownException(e);
				}
			}

		}

		sinks.clear();
		// constructQueryConsumers.clear();

	}

	private void evaluate() {

		// Clear sinks
		// for (BindingConsumerCollectionSink sink : sinks) {
		// sink.clear();
		// }

		try {

			// Get input nodes from the delegate if it exists
			if (delegate != null) {

				Set<Nodes> nodesSet = delegate.getNodes();

				if (nodesSet != null) {
					for (Nodes nodes : nodesSet) {
						evaluation.getBaseConsumer().consume(engine.getBindingFactory().createBinding(nodes));
					}
				}

			}

			logger.info("Evaluation start with delay > {}", delay);

			// Evaluate with delay which may be zero in case of manual execution
			evaluation.awaitIdleAndReset(delay);

			logger.info("Evaluation end");

		}

		catch (InterruptedException e) {
			if (super.delegate != null) {
				super.delegate.onControllerExecutionException(e);
			}
		}

	}

	public RunControllerDelegate getEvaluationControllerDelegate() {
		return delegate;
	}

	public void setEvaluationControllerDelegate(RunControllerDelegate delegate) {
		this.delegate = delegate;
	}

	public Map<String, Program> getPrograms() {
		return programs;
	}

	public Map<String, Collection<ConstructQuery>> getConstructQueryCollections() {
		return constructQueryCollections;
	}

	public Map<String, Collection<SelectQuery>> getSelectQueryCollections() {
		return selectQueryCollections;
	}

	public Map<String, Sink> getSinks() {
		return sinks;
	}

	public EvaluateProgramConfig getConfiguration() {
		return configuration;
	}

	public void setConfiguration(EvaluateProgramConfig configuration) {
		this.configuration = configuration;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

}
