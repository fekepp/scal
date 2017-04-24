package net.fekepp.ldp;

import java.util.Map;
import java.util.Set;

public interface Model {

	public String getName();

	public Format getDefaultFormat();

	public Set<Format> getFormats();

	public Map<String, Format> getMediaTypesMap();

	public Map<String, Format> getFileExtensionsMap();

	public ModelConverter buildModelConverter(Model sinkModel);

	public FormatConverter buildFormatConverter(Format sourceFormat, Format sinkFormat);

	// public ContentListener buildContentListener();

}