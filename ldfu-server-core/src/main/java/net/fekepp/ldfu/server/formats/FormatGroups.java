package net.fekepp.ldfu.server.formats;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jersey.repackaged.com.google.common.collect.Sets;

public final class FormatGroups {

	private static Set<FormatGroup> FORMAT_GROUPS = Sets.newHashSet(Notation3FormatGroup.getInstance(),
			RdfFormatGroup.getInstance(), SparqlQueryFormatGroup.getInstance());

	public static Set<FormatGroup> getFormatGroups() {
		return Collections.unmodifiableSet(FORMAT_GROUPS);
	}

	public static Map<String, Format> getMediaTypesMap() {
		Map<String, Format> contentTypesMap = new HashMap<String, Format>();
		for (FormatGroup formatGroup : FORMAT_GROUPS) {
			contentTypesMap.putAll(formatGroup.getMediaTypesMap());
		}
		return Collections.unmodifiableMap(contentTypesMap);
	}

	public static Map<String, Format> getFileExtensionsMap() {
		Map<String, Format> fileExtensionsMap = new HashMap<String, Format>();
		for (FormatGroup formatGroup : FORMAT_GROUPS) {
			fileExtensionsMap.putAll(formatGroup.getFileExtensionsMap());
		}
		return Collections.unmodifiableMap(fileExtensionsMap);
	}

}