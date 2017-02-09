package net.fekepp.ldfu.server.mediatype;

import java.util.Collections;
import java.util.Set;

import jersey.repackaged.com.google.common.collect.Sets;

public class Notation3Format implements Format {

	private static String NAME = "Notation3";

	private static FormatGroup FORMAT_GROUP = Notation3FormatGroup.getInstance();

	private static String DEFAULT_MEDIA_TYPE = "text/n3";// text/n3;charset=utf-8

	private static Set<String> MEDIA_TYPES = Sets.newHashSet(DEFAULT_MEDIA_TYPE);

	private static String DEFAULT_FILE_EXTENSION = ".n3";

	private static Set<String> FILE_EXTENSIONS = Sets.newHashSet(DEFAULT_FILE_EXTENSION);

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
	public FormatGroup getFormatGroup() {
		return FORMAT_GROUP;
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

}