package net.fekepp.ldfu.server.formats;

import java.util.Set;

public interface Format {

	// Name

	public String getName();

	// Format Group

	public FormatGroup getFormatGroup();

	// public void setFormatGroup(FormatGroup formatGroup);

	// public void delFormatGroup();

	// Media Types

	public String getDefaultMediaType();

	// public void setDefaultMediaType(String mediaType);

	public Set<String> getMediaTypes();

	// public void addMediaType(String mediaType);

	// public void remMediaType(String mediaType);

	// File Extensions

	public String getDefaultFileExtension();

	// public void setDefaultFileExtension(String fileExtension);

	public Set<String> getFileExtensions();

	// public void addFileExtension(String fileExtension);

	// public void remFileExtension(String fileExtension);

}