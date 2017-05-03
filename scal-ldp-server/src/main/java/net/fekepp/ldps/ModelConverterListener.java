package net.fekepp.ldps;

public interface ModelConverterListener {

	public FormatConverterListenerDelegate getFormatConverterListenerDelegate();

	public void setFormatConverterListenerDelegate(FormatConverterListenerDelegate delegate);

	public Model getModel();

	public boolean isListeningOnFormatConverter(FormatConverter formatConverter);

}
