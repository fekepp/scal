package net.fekepp.ldps;

import java.net.URI;

public interface Description {

	public URI getBase();

	public void setBase(URI base);

	public String getIdentifier();

	public void setIdentifier(String identifier);

	public Format getFormat();

	public void setFormat(Format format);

}