package net.fekepp.ldp;

public interface ModelConverterListener {

	public FormatConverterListenerDelegate getFormatConverterListenerDelegate();

	public void setFormatConverterListenerDelegate(FormatConverterListenerDelegate delegate);

	public Model getModel();

	public boolean isListeningOnFormatConverter(FormatConverter formatConverter);

}
