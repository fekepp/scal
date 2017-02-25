package net.fekepp.ldfu.server.formats;

import java.util.Collections;
import java.util.Set;

import jersey.repackaged.com.google.common.collect.Sets;

public class RdfXmlFormat implements Format {

	private static String NAME = "RDF/XML";

	private static FormatGroup FORMAT_GROUP = RdfFormatGroup.getInstance();

	private static String DEFAULT_MEDIA_TYPE = "application/rdf+xml";

	private static Set<String> MEDIA_TYPES = Sets.newHashSet(DEFAULT_MEDIA_TYPE);

	private static String DEFAULT_FILE_EXTENSION = ".rdf";

	private static Set<String> FILE_EXTENSIONS = Sets.newHashSet(DEFAULT_FILE_EXTENSION);

	private static class InstanceHolder {
		static final RdfXmlFormat INSTANCE = new RdfXmlFormat();
	}

	private RdfXmlFormat() {
	}

	public static RdfXmlFormat getInstance() {
		return InstanceHolder.INSTANCE;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public FormatGroup getFormatGroup() {
		return FORMAT_GROUP;
	}

	@Override
	public String getDefaultMediaType() {
		return DEFAULT_MEDIA_TYPE;
	}

	@Override
	public Set<String> getMediaTypes() {
		return Collections.unmodifiableSet(MEDIA_TYPES);
	}

	@Override
	public String getDefaultFileExtension() {
		return DEFAULT_FILE_EXTENSION;
	}

	@Override
	public Set<String> getFileExtensions() {
		return Collections.unmodifiableSet(FILE_EXTENSIONS);
	}

}