package net.fekepp.ldp.listener;

import java.util.Set;

import net.fekepp.ldp.FormatConverter;
import net.fekepp.ldp.FormatConverterListener;
import net.fekepp.ldp.ResourceListenerDelegate;
import net.fekepp.ldp.ModelConverter;
import net.fekepp.ldp.ModelConverterListener;

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
