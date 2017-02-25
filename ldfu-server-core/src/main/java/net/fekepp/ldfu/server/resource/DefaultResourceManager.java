package net.fekepp.ldfu.server.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;

import org.semanticweb.yars.nx.parser.ParseException;

import net.fekepp.ldfu.server.exceptions.ContainerIdentifierExpectedException;
import net.fekepp.ldfu.server.exceptions.ParentNotFoundException;
import net.fekepp.ldfu.server.exceptions.ResourceIdentifierExpectedException;
import net.fekepp.ldfu.server.exceptions.ResourceNotFoundException;
import net.fekepp.ldfu.server.mediatype.Format;
import net.fekepp.ldfu.server.mediatype.RdfFormatGroup;
import net.fekepp.ldfu.server.storage.Storage;
import net.fekepp.ldfu.server.storage.StorageResource;

public class DefaultResourceManager implements ResourceManager {

	private Storage storage;

	private Map<String, Format> mediaTypeToFormatMap = RdfFormatGroup.getInstance().getMediaTypesMap();

	public DefaultResourceManager(Storage storage) {
		this.storage = storage;
	}

	public InputStream getResource(String identifier, String mediaType, URI base) throws ResourceNotFoundException,
			ContainerIdentifierExpectedException, ResourceIdentifierExpectedException {

		// Get the source
		StorageResource source = storage.getResource(identifier);

		// Get the source format
		Format sourceFormat = source.getFormat();

		// If source format is available
		if (sourceFormat != null) {

			// Get the sink format
			Format sinkFormat = mediaTypeToFormatMap.get(mediaType);

			// If sink format is available
			// And if source format group equals sink format group
			if (sinkFormat != null && sourceFormat.getFormatGroup().equals(sinkFormat.getFormatGroup())) {

				// Return data serialized with the media type of the
				// request format
				// TODO Finalize implementation
				return source.getData();

			}

			// If sink format is not available
			// Or if source format group not equals sink format group
			else {

				// Return data serialized with default media type of the
				// source format group
				// TODO Finalize implementation
				return source.getData();

			}

		}

		// If source format is not available
		else {

			// Return binary data
			// TODO Finalize implementation
			return source.getData();

		}

	}

	public void setResource(String identifier, String mediaType, InputStream data, URI base)
			throws ContainerIdentifierExpectedException, ResourceIdentifierExpectedException, ParentNotFoundException,
			ParseException, IOException {

		// Get the source format
		Format sourceFormat = mediaTypeToFormatMap.get(mediaType);

		// If source format is available
		if (sourceFormat != null) {

			try {

				// Get the sink resource
				StorageResource sink = storage.getResource(identifier);

				// Get the sink format
				Format sinkFormat = sink.getFormat();

				// If sink format is available
				// And if source format group equals sink format group
				if (sinkFormat != null && sourceFormat.getFormatGroup().equals(sinkFormat.getFormatGroup())) {

					// Set sink to data serialized in format of sink
					// TODO Finalize implementation
					storage.setResource(identifier, null);
					return;

				}

			}

			// If source is not existing
			catch (ResourceNotFoundException e) {

				// Ignore because set sink will be set to data serialized in
				// default format of source

			}

			// If sink is not existing
			// Or if sink format is not available
			// Or if source format group not equals sink format group

			// Set sink to data serialized in default format of source
			// format group
			// TODO Finalize implementation
			storage.setResource(identifier, null);
			return;

		}

		// If source format is not available
		else {

			// Set sink to binary data
			// TODO Finalize implementation
			storage.setResource(identifier, null);
			return;

		}

	}

	public void delResource(String identifier) throws ResourceNotFoundException, ResourceIdentifierExpectedException,
			ContainerIdentifierExpectedException, IOException {

		// Delete the resource from storage
		storage.delResource(identifier);

	}

	public OutputStream proResource(String identifier, String mediaType, InputStream data, URI base)
			throws ResourceNotFoundException, ResourceIdentifierExpectedException,
			ContainerIdentifierExpectedException {
		return null;
	}

}