package net.fekepp.ldps.format;

import java.util.Collections;
import java.util.Set;

import jersey.repackaged.com.google.common.collect.Sets;
import net.fekepp.ldps.Format;
import net.fekepp.ldps.FormatConverter;
import net.fekepp.ldps.Model;
import net.fekepp.ldps.model.Notation3Model;

public class Notation3Format implements Format {

	private static String NAME = "Notation3";

	private static Model MODEL = Notation3Model.getInstance();

	private static String DEFAULT_MEDIA_TYPE = "text/n3";// text/n3;charset=utf-8

	private static Set<String> MEDIA_TYPES = Sets.newHashSet(DEFAULT_MEDIA_TYPE, "text/rdf+n3", "application/rdf+n3");

	private static String DEFAULT_FILE_EXTENSION = ".n3";

	private static Set<String> FILE_EXTENSIONS = Sets.newHashSet(DEFAULT_FILE_EXTENSION, ".notation3");

	private static class InstanceHolder {
		static final Notation3Format INSTANCE = new Notation3Format();
	}

	private Notation3Format() {
	}

	public static Notation3Format getInstance() {
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
		return getModel().buildFormatConverter(this, sinkFormat);
	}

}