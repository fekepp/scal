package net.fekepp.scal.run;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.namespace.RDF;
import org.semanticweb.yars.nx.parser.Callback;

public class CallbackRdfClassPropertyReader extends Callback {

	private Set<Node> classes = new HashSet<Node>();
	private Set<Node> properties = new HashSet<Node>();

	private Map<Node, Set<Node>> classSubjectsByTypes = new HashMap<Node, Set<Node>>();
	private Map<Node, Map<Node, Set<Node>>> propertyObjectsByTypesAndSubjects = new HashMap<Node, Map<Node, Set<Node>>>();

	@Override
	protected void startDocumentInternal() {

	}

	@Override
	protected void processStatementInternal(Node[] node) {

		// Classes
		if (node[1].equals(RDF.TYPE)) {
			if (classes.contains(node[2])) {
				Set<Node> classSubjectByType = classSubjectsByTypes.get(node[2]);
				if (classSubjectByType == null) {
					classSubjectByType = new HashSet<Node>();
					classSubjectsByTypes.put((Node) node[2], classSubjectByType);
				}
				classSubjectByType.add((Node) node[0]);
			}
		}

		// Properties
		if (properties.contains(node[1])) {
			Map<Node, Set<Node>> propertyObjectsByTypeAndSubjects = propertyObjectsByTypesAndSubjects.get(node[1]);
			if (propertyObjectsByTypeAndSubjects == null) {
				propertyObjectsByTypeAndSubjects = new HashMap<Node, Set<Node>>();
				propertyObjectsByTypesAndSubjects.put(node[1], propertyObjectsByTypeAndSubjects);
			}
			Set<Node> propertyObjectsByTypeAndSubject = propertyObjectsByTypeAndSubjects.get(node[0]);
			if (propertyObjectsByTypeAndSubject == null) {
				propertyObjectsByTypeAndSubject = new HashSet<Node>();
				propertyObjectsByTypeAndSubjects.put((Node) node[0], propertyObjectsByTypeAndSubject);
			}
			propertyObjectsByTypeAndSubject.add((Node) node[2]);
		}

	}

	@Override
	protected void endDocumentInternal() {

	}

	public Set<Node> getClasses() {
		return classes;
	}

	public Set<Node> getProperties() {
		return properties;
	}

	public Set<Node> getClassSubjects(Node type) {
		return classSubjectsByTypes.get(type);
	}

	public Set<Node> getPropertyObjects(Node type, Node subject) {
		Map<Node, Set<Node>> propertiesBySubjects = propertyObjectsByTypesAndSubjects.get(type);
		if (propertiesBySubjects != null) {
			return propertiesBySubjects.get(subject);
		}
		return null;
	}

}
