package net.fekepp.ldps;

import java.util.Set;

public interface Format {

	//
	// Name
	//

	public String getName();

	//
	// Model
	//

	public Model getModel();

	// public void setModel(Model model);

	// public void delModel();

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