package net.fekepp.scal.namespace;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.yars.nx.Resource;

public final class SCAL {

	// SCAL

	public static final String NS = "http://scal#";

	// Run

	public static final Resource Run = new Resource(NS + "Run");

	public static final Resource threadingModel = new Resource(NS + "threadingModel");

	public static final Resource program = new Resource(NS + "program");

	public static final Resource query = new Resource(NS + "query");

	public static final Resource resource = new Resource(NS + "resource");

	public static final Resource trigger = new Resource(NS + "trigger");

	// Program

	public static final Resource Program = new Resource(NS + "Program");

	// Query

	public static final Resource Query = new Resource(NS + "Query");

	// Resource

	public static final Resource Resource = new Resource(NS + "Resource");

	public static final Resource FileResource = new Resource(NS + "FileResource");

	public static final Resource HttpResource = new Resource(NS + "HttpResource");

	public static final Resource FtpResource = new Resource(NS + "FtpResource");

	public static final Resource SshResource = new Resource(NS + "SshResource");

	// Trigger

	public static final Resource Trigger = new Resource(NS + "Trigger");

	// Trigger > Time Trigger

	public static final Resource TimeTrigger = new Resource(NS + "TimeTrigger");

	// Trigger > Time Trigger > Frequency Trigger

	public static final Resource FrequencyTrigger = new Resource(NS + "FrequencyTrigger");

	public static final Resource frequency = new Resource(NS + "frequency");

	// Trigger > Time Trigger > Delay Trigger

	public static final Resource DelayTrigger = new Resource(NS + "DelayTrigger");

	public static final Resource delay = new Resource(NS + "delay");

	// Trigger > Event Trigger

	public static final Resource EventTrigger = new Resource(NS + "EventTrigger");

	// Trigger > Event Trigger > Resource Requested Trigger

	public static final Resource ResourceRequestedTrigger = new Resource(NS + "ResourceRequestedTrigger");

	public static final Resource ResourceChangedTrigger = new Resource(NS + "ResourceChangedTrigger");

	// Cross-concept

	public static final Resource declaration = new Resource(NS + "declaration");

	public static final Resource uri = new Resource(NS + "uri");

	public static final Resource method = new Resource(NS + "method");

	public static final Resource sink = new Resource(NS + "sink");

	public static final Resource state = new Resource(NS + "state");

	public static final Set<Resource> getResources() {
		HashSet<Resource> resources = new HashSet<Resource>();
		resources.addAll(getClasses());
		resources.addAll(getProperties());
		return resources;
	}

	public static final Set<Resource> getClasses() {
		HashSet<Resource> resources = new HashSet<Resource>();
		resources.add(Run);
		resources.add(Program);
		resources.add(Query);
		resources.add(Resource);
		resources.add(FileResource);
		resources.add(HttpResource);
		resources.add(FtpResource);
		resources.add(SshResource);
		resources.add(Trigger);
		resources.add(TimeTrigger);
		resources.add(EventTrigger);
		resources.add(FrequencyTrigger);
		resources.add(DelayTrigger);
		resources.add(ResourceRequestedTrigger);
		resources.add(ResourceChangedTrigger);
		return resources;
	}

	public static final Set<Resource> getProperties() {
		HashSet<Resource> resources = new HashSet<Resource>();
		resources.add(threadingModel);
		resources.add(program);
		resources.add(query);
		resources.add(resource);
		resources.add(trigger);
		resources.add(frequency);
		resources.add(delay);
		resources.add(declaration);
		resources.add(uri);
		resources.add(method);
		resources.add(sink);
		resources.add(state);
		return resources;
	}

}