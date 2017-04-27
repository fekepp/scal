package net.fekepp.scal.run;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.semanticweb.yars.nx.Nodes;

import edu.kit.aifb.datafu.ConstructQuery;
import edu.kit.aifb.datafu.Program;
import edu.kit.aifb.datafu.SelectQuery;
import edu.kit.aifb.datafu.consumer.impl.BindingConsumerCollection;
import edu.kit.aifb.datafu.engine.EvaluateProgram;
import edu.kit.aifb.datafu.io.origins.InternalOrigin;
import edu.kit.aifb.datafu.planning.EvaluateProgramConfig;
import edu.kit.aifb.datafu.planning.EvaluateProgramConfig.ThreadingModel;
import edu.kit.aifb.datafu.planning.EvaluateProgramGenerator;
import net.fekepp.controllers.AbstractController;

public class RunController extends AbstractController {

	protected RunControllerDelegate delegate;

	private Map<String, Program> programs = new ConcurrentHashMap<String, Program>();

	private Map<String, Collection<ConstructQuery>> constructQueryCollections = new ConcurrentHashMap<String, Collection<ConstructQuery>>();

	private Map<String, Collection<SelectQuery>> selectQueryCollections = new ConcurrentHashMap<String, Collection<SelectQuery>>();

	private Map<String, BindingConsumerCollection> constructQueryConsumers = new ConcurrentHashMap<String, BindingConsumerCollection>();

	private Map<String, BindingConsumerCollection> selectQueryConsumers = new ConcurrentHashMap<String, BindingConsumerCollection>();

	private Set<BindingConsumerCollectionSink> sinks = new HashSet<BindingConsumerCollectionSink>();

	private EvaluateProgramConfig configuration = new EvaluateProgramConfig();

	private EvaluateProgramGenerator engine;

	private EvaluateProgram evaluation;

	private int delay;

	public RunController() {

		// Currently, set threading model to serial to omit any side effects
		configuration.setThreadingModel(ThreadingModel.SERIAL);

	}

	@Override
	protected void startup() throws Exception {

		// Create a program for queries
		Program program = new Program(new InternalOrigin("api"));

		// Handle construct queries collection
		for (Entry<String, Collection<ConstructQuery>> entry : constructQueryCollections.entrySet()) {

			// Create a binding consumer and sink each collection of queries
			BindingConsumerCollection bindingConsumerCollection = new BindingConsumerCollection();
			BindingConsumerCollectionSink bindingConsumerSink = new BindingConsumerCollectionSink(
					bindingConsumerCollection);
			sinks.add(bindingConsumerSink);

			// Add all construct queries to the program
			for (ConstructQuery query : entry.getValue()) {
				program.registerConstructQuery(query, bindingConsumerSink);
			}

			// Remember the binding consumer
			constructQueryConsumers.put(entry.getKey(), bindingConsumerCollection);

		}

		// Handle select queries collection
		for (Entry<String, Collection<SelectQuery>> entry : selectQueryCollections.entrySet()) {

			// Create a binding consumer and sink each collection of queries
			BindingConsumerCollection bindingConsumerCollection = new BindingConsumerCollection();
			BindingConsumerCollectionSink bindingConsumerSink = new BindingConsumerCollectionSink(
					bindingConsumerCollection);
			sinks.add(bindingConsumerSink);

			// Add all construct queries to the program
			for (SelectQuery query : entry.getValue()) {
				program.registerSelectQuery(query, bindingConsumerSink);
			}

			// Remember the binding consumer
			selectQueryConsumers.put(entry.getKey(), bindingConsumerCollection);

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

	protected void execute() {

		while (execution && !shutdown) {

			for (BindingConsumerCollectionSink sink : sinks) {
				sink.clear();
			}

			try {

				if (delegate != null) {

					Set<Nodes> nodesSet = delegate.getNodes();

					if (nodesSet != null) {
						for (Nodes nodes : nodesSet) {
							evaluation.getBaseConsumer().consume(engine.getBindingFactory().createBinding(nodes));
						}
					}

				}

				evaluation.awaitIdleAndReset(delay);

			}

			catch (InterruptedException e) {
				if (super.delegate != null) {
					super.delegate.onControllerExecutionException(e);
				}
			}

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
		constructQueryConsumers.clear();

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

	public Map<String, BindingConsumerCollection> getConstructQueryConsumers() {
		return constructQueryConsumers;
	}

	public Map<String, Collection<SelectQuery>> getSelectQueryCollections() {
		return selectQueryCollections;
	}

	public Map<String, BindingConsumerCollection> getSelectQueryConsumers() {
		return selectQueryConsumers;
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
