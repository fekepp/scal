package net.fekepp.ldp;

import java.net.URI;

public interface Description {

	public URI getBaseUri();

	public String getIdentifier();

	public Format getFormat();

}