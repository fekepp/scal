package net.fekepp.ldfu.server.mediatype;

import java.util.Map;
import java.util.Set;

public interface FormatGroup {

	public String getName();

	public Format getDefaultFormat();

	public Set<Format> getFormats();

	public Map<String, Format> getMediaTypesMap();

	public Map<String, Format> getFileExtensionsMap();

}