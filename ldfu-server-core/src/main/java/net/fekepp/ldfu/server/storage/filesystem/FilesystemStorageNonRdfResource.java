package net.fekepp.ldfu.server.storage.filesystem;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;

import net.fekepp.ldfu.server.formats.Format;
import net.fekepp.ldfu.server.storage.StorageResource;

public class FilesystemStorageNonRdfResource implements StorageResource {

	private String identifier;

	private Path path;

	private Format format;

	public FilesystemStorageNonRdfResource(String identifier, Path path, Format format) {
		this.identifier = identifier;
		this.path = path;
		this.format = format;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public Format getFormat() {
		return format;
	}

	@Override
	public InputStream getData() {
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(path.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileInputStream;
	}

	@Override
	public boolean isRdfResource() {
		return false;
	}

	@Override
	public boolean isContainerResource() {
		return false;
	}

}
