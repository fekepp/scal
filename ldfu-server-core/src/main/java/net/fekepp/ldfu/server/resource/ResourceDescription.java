package net.fekepp.ldfu.server.resource;

import java.net.URI;

import net.fekepp.ldfu.server.data.Format;

public class ResourceDescription implements Description {

	private URI baseUri;
	private String identifier;
	private Format format;

	public ResourceDescription(URI baseUri, String identifier) {
		this(baseUri, identifier, null);
	}

	public ResourceDescription(URI baseUri, String identifier, Format format) {
		this.baseUri = baseUri;
		this.identifier = identifier;
		this.format = format;
	}

	@Override
	public URI getBaseUri() {
		return baseUri;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public Format getFormat() {
		return format;
	}

}