package net.fekepp.ldfu.server.data.formats;

import java.util.Collections;
import java.util.Set;

import jersey.repackaged.com.google.common.collect.Sets;
import net.fekepp.ldfu.server.data.Format;
import net.fekepp.ldfu.server.data.FormatConverter;
import net.fekepp.ldfu.server.data.Model;
import net.fekepp.ldfu.server.data.converters.RdfConverter;
import net.fekepp.ldfu.server.data.models.RdfModel;

public class NtriplesFormat implements Format {

	private static String NAME = "N-Triples";

	private static Model MODEL = RdfModel.getInstance();

	private static String DEFAULT_MEDIA_TYPE = "application/n-triples";

	private static Set<String> MEDIA_TYPES = Sets.newHashSet(DEFAULT_MEDIA_TYPE);

	private static String DEFAULT_FILE_EXTENSION = ".nt";

	private static Set<String> FILE_EXTENSIONS = Sets.newHashSet(DEFAULT_FILE_EXTENSION, ".ntriples");

	private static class InstanceHolder {
		static final NtriplesFormat INSTANCE = new NtriplesFormat();
	}

	private NtriplesFormat() {
	}

	public static NtriplesFormat getInstance() {
		return InstanceHolder.INSTANCE;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Model getModel() {
		return MODEL;
	}

	@Override
	public String getDefaultMediaType() {
		return DEFAULT_MEDIA_TYPE;
	}

	@Override
	public Set<String> getMediaTypes() {
		return Collections.unmodifiableSet(MEDIA_TYPES);
	}

	@Override
	public String getDefaultFileExtension() {
		return DEFAULT_FILE_EXTENSION;
	}

	@Override
	public Set<String> getFileExtensions() {
		return Collections.unmodifiableSet(FILE_EXTENSIONS);
	}

	@Override
	public FormatConverter buildFormatConverter(Format sinkFormat) {
		if (getModel().getFormats().contains(sinkFormat)) {
			return new RdfConverter(getInstance(), sinkFormat);
		}
		// TODO Add support for conversion between format groups
		return null;
	}

}