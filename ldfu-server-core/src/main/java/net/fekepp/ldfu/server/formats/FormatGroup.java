package net.fekepp.ldfu.server.formats;

import java.util.Map;
import java.util.Set;

import net.fekepp.ldfu.server.converter.FormatGroupConverter;

public interface FormatGroup {

	public String getName();

	public Format getDefaultFormat();

	public Set<Format> getFormats();

	public Map<String, Format> getMediaTypesMap();

	public Map<String, Format> getFileExtensionsMap();

	public FormatGroupConverter buildConverter(FormatGroup sinkFormatGroup);

}