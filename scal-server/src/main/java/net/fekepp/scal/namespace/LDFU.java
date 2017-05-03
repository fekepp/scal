package net.fekepp.scal.namespace;

import org.semanticweb.yars.nx.Resource;

public final class LDFU {

	public static final String NS = "http://ldfu#";

	public static final Resource Instance = new Resource(NS + "Instance");

	public static final Resource hasConfiguration = new Resource(NS + "hasConfiguration");

	public static final Resource hasPrograms = new Resource(NS + "hasPrograms");

	public static final Resource hasResources = new Resource(NS + "hasResources");

	public static final Resource Configuration = new Resource(NS + "Configuration");

	public static final Resource hasDelay = new Resource(NS + "hasDelay");

}