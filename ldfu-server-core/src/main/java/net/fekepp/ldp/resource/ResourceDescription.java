package net.fekepp.ldp.resource;

import java.net.URI;

import net.fekepp.ldp.Description;
import net.fekepp.ldp.Format;

public class ResourceDescription implements Description {

	private URI base;
	private String identifier;
	private Format format;

	public ResourceDescription(URI base, String identifier) {
		this(base, identifier, null);
	}

	public ResourceDescription(URI base, String identifier, Format format) {
		this.base = base;
		this.identifier = identifier;
		this.format = format;
	}

	@Override
	public URI getBase() {
		return base;
	}

	@Override
	public void setBase(URI base) {
		this.base = base;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public Format getFormat() {
		return format;
	}

	@Override
	public void setFormat(Format format) {
		this.format = format;
	}

}