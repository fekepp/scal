package net.fekepp.scal.run;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.semanticweb.yars.nx.Variable;

import edu.kit.aifb.datafu.Binding;
import edu.kit.aifb.datafu.Sink;
import edu.kit.aifb.datafu.consumer.impl.BindingConsumerCollection;
import edu.kit.aifb.datafu.io.mediatypes.MediaType;

/**
 * @author "Felix Leif Keppmann"
 */
public class BindingConsumerCollectionSink implements Sink {

	private final BindingConsumerCollection bindingConsumerCollection;

	public BindingConsumerCollectionSink(BindingConsumerCollection bindingConsumerCollection) {
		this.bindingConsumerCollection = bindingConsumerCollection;
	}

	@Override
	public void consume(Binding binding) throws InterruptedException {
		bindingConsumerCollection.consume(binding);
	}

	@Override
	public void consume(Collection<Binding> bindings) throws InterruptedException {
		bindingConsumerCollection.consume(bindings);
	}

	@Override
	public void setVariables(List<Variable> li) {
		// Ignore
	}

	@Override
	public void setMediaType(MediaType mt) {
		throw new UnsupportedOperationException();
	}

	@Override
	public MediaType getMediaType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void open() throws IOException {
		// Ignore
	}

	@Override
	public void close() throws IOException {
		// Ignore
	}

//	@Override
//	public void reset() {
//		// Ignore
//	}

	public void clear() {
		bindingConsumerCollection.getCollection().clear();
	}

}