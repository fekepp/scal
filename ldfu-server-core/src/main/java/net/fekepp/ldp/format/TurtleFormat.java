package net.fekepp.ldp.format;

import java.util.Collections;
import java.util.Set;

import jersey.repackaged.com.google.common.collect.Sets;
import net.fekepp.ldp.Format;
import net.fekepp.ldp.FormatConverter;
import net.fekepp.ldp.Model;
import net.fekepp.ldp.model.RdfModel;

public class TurtleFormat implements Format {

	private static String NAME = "Turtle";

	private static Model MODEL = RdfModel.getInstance();

	private static String DEFAULT_MEDIA_TYPE = "text/turtle";

	private static Set<String> MEDIA_TYPES = Sets.newHashSet(DEFAULT_MEDIA_TYPE, "application/x-turtle",
			"application/turtle");

	private static String DEFAULT_FILE_EXTENSION = ".ttl";

	private static Set<String> FILE_EXTENSIONS = Sets.newHashSet(DEFAULT_FILE_EXTENSION, ".turtle");

	private static class InstanceHolder {
		static final TurtleFormat INSTANCE = new TurtleFormat();
	}

	private TurtleFormat() {
	}

	public static TurtleFormat getInstance() {
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