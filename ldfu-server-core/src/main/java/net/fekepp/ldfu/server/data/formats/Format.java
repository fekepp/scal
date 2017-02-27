package net.fekepp.ldfu.server.data.formats;

import java.util.Set;

import net.fekepp.ldfu.server.data.converters.FormatConverter;
import net.fekepp.ldfu.server.data.models.Model;

public interface Format {

	//
	// Name
	//

	public String getName();

	//
	// Format Group
	//

	public Model getFormatGroup();

	// public void setFormatGroup(FormatGroup formatGroup);

	// public void delFormatGroup();

	//
	// Media Types
	//

	public String getDefaultMediaType();

	// public void setDefaultMediaType(String mediaType);

	public Set<String> getMediaTypes();

	// public void addMediaType(String mediaType);

	// public void remMediaType(String mediaType);

	//
	// File Extensions
	//

	public String getDefaultFileExtension();

	// public void setDefaultFileExtension(String fileExtension);

	public Set<String> getFileExtensions();

	// public void addFileExtension(String fileExtension);

	// public void remFileExtension(String fileExtension);

	//
	// Parser/Serializer/Converter
	//
	// public Parser getParser();

	// public Serializer getSerializer();

	public FormatConverter buildFormatConverter(Format format);

}