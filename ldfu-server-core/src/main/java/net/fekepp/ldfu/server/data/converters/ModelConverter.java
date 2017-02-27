package net.fekepp.ldfu.server.data.converters;

import net.fekepp.ldfu.server.data.models.Model;

public interface ModelConverter {

	public void convert();

	public Model getSourceFormatGroup();

	public Model getSinkFormatGroup();

}