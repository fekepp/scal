package net.fekepp.ldfu.server.data;

public interface ModelConverterListener {

	public FormatConverterListenerDelegate getFormatConverterListenerDelegate();

	public void setFormatConverterListenerDelegate(FormatConverterListenerDelegate delegate);

	public Model getModel();

	public boolean isListeningOnFormatConverter(FormatConverter formatConverter);

}
