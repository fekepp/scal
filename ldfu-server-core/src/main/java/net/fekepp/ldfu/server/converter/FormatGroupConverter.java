package net.fekepp.ldfu.server.converter;

import net.fekepp.ldfu.server.formats.FormatGroup;

public interface FormatGroupConverter {

	public void convert();

	public FormatGroup getSourceFormatGroup();

	public FormatGroup getSinkFormatGroup();

}