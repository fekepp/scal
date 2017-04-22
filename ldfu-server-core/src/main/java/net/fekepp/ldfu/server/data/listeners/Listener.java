package net.fekepp.ldfu.server.data.listeners;

import java.util.Set;

import net.fekepp.ldfu.server.data.Format;
import net.fekepp.ldfu.server.data.FormatConverter;
import net.fekepp.ldfu.server.data.FormatConverterListener;
import net.fekepp.ldfu.server.data.Model;
import net.fekepp.ldfu.server.data.ModelConverter;
import net.fekepp.ldfu.server.data.ModelConverterListener;

public interface Listener {

	// Delegate
	public ListenerDelegate getListenerDelegate();

	public void setListenerDelegate(ListenerDelegate listenerDelegate);

	// Processing
	// public Source process(Source storage, Source input);

	// Identifier
	public boolean isListeningOnIdentifier(String identifier);

	// Method
	public boolean isListeningOnMethod(String method);

	// Storage
	public boolean isListeningOnStorage(Model model);

	public boolean isListeningOnStorage(Format format);

	// Input
	public boolean isListeningOnInput(Model model);

	public boolean isListeningOnInput(Format format);

	// Output
	public boolean isListeningOnOutput(Model model);

	public boolean isListeningOnOutput(Format format);

	// TODO
	// public boolean isListeningOnModelConverter(ModelConverter
	// modelConverter);
	//
	// public boolean isListeningOnFormatConverter(FormatConverter
	// formatConverter);

	// Storage Converter Listeners
	public Set<ModelConverterListener> buildStorageModelConverterListener(ModelConverter modelConverter);

	public Set<FormatConverterListener> buildStorageFormatConverterListener(FormatConverter formatConverter);

	// Input Converter Listeners
	public Set<ModelConverterListener> buildInputModelConverterListener(ModelConverter modelConverter);

	public Set<FormatConverterListener> buildInputFormatConverterListener(FormatConverter formatConverter);

	// Output Converter Listeners
	public Set<ModelConverterListener> buildOutputModelConverterListener(ModelConverter modelConverter);

	public Set<FormatConverterListener> buildOutputFormatConverterListener(FormatConverter formatConverter);

}
