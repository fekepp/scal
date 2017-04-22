package net.fekepp.ldfu.server.data.listeners;

import java.util.Set;

import net.fekepp.ldfu.server.data.FormatConverter;
import net.fekepp.ldfu.server.data.FormatConverterListener;
import net.fekepp.ldfu.server.data.ModelConverter;
import net.fekepp.ldfu.server.data.ModelConverterListener;

public class ResourceListener extends AbstractListener {

	public ResourceListener(ListenerDelegate listenerDelegate) {
		super(listenerDelegate);
	}

	@Override
	public Set<ModelConverterListener> buildStorageModelConverterListener(ModelConverter modelConverter) {
		return null;
	}

	@Override
	public Set<FormatConverterListener> buildStorageFormatConverterListener(FormatConverter formatConverter) {
		return null;
	}

	@Override
	public Set<ModelConverterListener> buildInputModelConverterListener(ModelConverter modelConverter) {
		return null;
	}

	@Override
	public Set<FormatConverterListener> buildInputFormatConverterListener(FormatConverter formatConverter) {
		return null;
	}

	@Override
	public Set<ModelConverterListener> buildOutputModelConverterListener(ModelConverter modelConverter) {
		return null;
	}

	@Override
	public Set<FormatConverterListener> buildOutputFormatConverterListener(FormatConverter formatConverter) {
		return null;
	}

}
