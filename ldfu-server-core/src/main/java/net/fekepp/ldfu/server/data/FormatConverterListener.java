package net.fekepp.ldfu.server.data;

public interface FormatConverterListener {

	public FormatConverterListenerDelegate getFormatConverterListenerDelegate();

	public void setFormatConverterListenerDelegate(FormatConverterListenerDelegate delegate);

	// public Model getModel();

	// public boolean isListeningOnFormatConverter(FormatConverter
	// formatConverter);

}
