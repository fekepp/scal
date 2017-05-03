package net.fekepp.ldps.listener;

import java.util.Set;

import net.fekepp.ldps.FormatConverter;
import net.fekepp.ldps.FormatConverterListener;
import net.fekepp.ldps.ModelConverter;
import net.fekepp.ldps.ModelConverterListener;
import net.fekepp.ldps.ResourceListenerDelegate;

public class DefaultResourceListener extends AbstractResourceListener {

	public DefaultResourceListener(ResourceListenerDelegate listenerDelegate) {
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
