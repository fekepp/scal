package net.fekepp.ldfu.server.data.models;

import java.util.Map;
import java.util.Set;

import net.fekepp.ldfu.server.data.converters.ModelConverter;
import net.fekepp.ldfu.server.data.formats.Format;

public interface Model {

	public String getName();

	public Format getDefaultFormat();

	public Set<Format> getFormats();

	public Map<String, Format> getMediaTypesMap();

	public Map<String, Format> getFileExtensionsMap();

	public ModelConverter buildConverter(Model sinkFormatGroup);

}