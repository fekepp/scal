package net.fekepp.ldp;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jersey.repackaged.com.google.common.collect.Sets;
import net.fekepp.ldp.model.Notation3Model;
import net.fekepp.ldp.model.RdfModel;
import net.fekepp.ldp.model.SparqlQueryModel;

public final class Models {

	private static Set<Model> FORMAT_GROUPS = Sets.newHashSet(Notation3Model.getInstance(),
			RdfModel.getInstance(), SparqlQueryModel.getInstance());

	public static Set<Model> getFormatGroups() {
		return Collections.unmodifiableSet(FORMAT_GROUPS);
	}

	public static Map<String, Format> getMediaTypesMap() {
		Map<String, Format> contentTypesMap = new HashMap<String, Format>();
		for (Model formatGroup : FORMAT_GROUPS) {
			contentTypesMap.putAll(formatGroup.getMediaTypesMap());
		}
		return Collections.unmodifiableMap(contentTypesMap);
	}

	public static Map<String, Format> getFileExtensionsMap() {
		Map<String, Format> fileExtensionsMap = new HashMap<String, Format>();
		for (Model formatGroup : FORMAT_GROUPS) {
			fileExtensionsMap.putAll(formatGroup.getFileExtensionsMap());
		}
		return Collections.unmodifiableMap(fileExtensionsMap);
	}

}