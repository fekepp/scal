package net.fekepp.ldfu.server.formats;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jersey.repackaged.com.google.common.collect.Sets;

public class RdfFormatGroup implements FormatGroup {

	private static String NAME = "RDF";

	private static Format DEFAULT_FORMAT = TurtleFormat.getInstance();

	private static Set<Format> FORMATS = Sets.newHashSet(DEFAULT_FORMAT, NtriplesFormat.getInstance(),
			RdfXmlFormat.getInstance());

	private static class InstanceHolder {
		static final RdfFormatGroup INSTANCE = new RdfFormatGroup();
	}

	private RdfFormatGroup() {
	}

	public static RdfFormatGroup getInstance() {
		return InstanceHolder.INSTANCE;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Format getDefaultFormat() {
		return DEFAULT_FORMAT;
	}

	@Override
	public Set<Format> getFormats() {
		return Collections.unmodifiableSet(FORMATS);
	}

	@Override
	public Map<String, Format> getMediaTypesMap() {
		Map<String, Format> contentTypesMap = new HashMap<String, Format>();
		for (Format format : FORMATS) {
			for (String mediaType : format.getMediaTypes()) {
				contentTypesMap.put(mediaType, format);
			}
		}
		return Collections.unmodifiableMap(contentTypesMap);
	}

	@Override
	public Map<String, Format> getFileExtensionsMap() {
		Map<String, Format> fileExtensionsMap = new HashMap<String, Format>();
		for (Format format : FORMATS) {
			for (String fileExtensions : format.getFileExtensions()) {
				fileExtensionsMap.put(fileExtensions, format);
			}
		}
		return Collections.unmodifiableMap(fileExtensionsMap);
	}

}