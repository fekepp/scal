package net.fekepp.ldps.converter;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fekepp.ldps.FormatConverterListener;
import net.fekepp.ldps.FormatConverterListenerDelegate;

public class RdfConverterTripleListener extends Callback implements FormatConverterListener {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private Node subject;

	private Node predicate;

	private Node object;

	private Callback callback;

	private FormatConverterListenerDelegate delegate;

	public RdfConverterTripleListener() {
		this(null);
	}

	public RdfConverterTripleListener(Callback callback) {
		this.callback = callback;
	}

	@Override
	protected void startDocumentInternal() {
		callback.startDocument();
	}

	@Override
	protected void processStatementInternal(Node[] node) {

		logger.info("Listener processing > {} | {} | {}", node[0], node[1], node[2]);

		if ((subject == null || node[0].equals(subject)) && (predicate == null || node[1].equals(predicate))
				&& (object == null || node[2].equals(object))) {
			logger.info("Listener triggered > {} | {} | {}", node[0], node[1], node[2]);
			if (delegate != null) {
				delegate.process();
			}
		}

		callback.processStatement(node);

	}

	@Override
	protected void endDocumentInternal() {
		callback.endDocument();
	}

	@Override
	public FormatConverterListenerDelegate getFormatConverterListenerDelegate() {
		return delegate;
	}

	@Override
	public void setFormatConverterListenerDelegate(FormatConverterListenerDelegate delegate) {
		this.delegate = delegate;
	}

	public Callback getCallback() {
		return callback;
	}

	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	public Node getSubject() {
		return subject;
	}

	public void setSubject(Node subject) {
		this.subject = subject;
	}

	public Node getPredicate() {
		return predicate;
	}

	public void setPredicate(Node predicate) {
		this.predicate = predicate;
	}

	public Node getObject() {
		return object;
	}

	public void setObject(Node object) {
		this.object = object;
	}

}
