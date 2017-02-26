package net.fekepp.ldfu.server.resource;

import java.io.IOException;

import net.fekepp.ldfu.server.converter.FormatConverter;
import net.fekepp.ldfu.server.exceptions.ContainerIdentifierExpectedException;
import net.fekepp.ldfu.server.exceptions.ParentNotFoundException;
import net.fekepp.ldfu.server.exceptions.ResourceIdentifierExpectedException;
import net.fekepp.ldfu.server.exceptions.ResourceNotFoundException;
import net.fekepp.ldfu.server.formats.Format;
import net.fekepp.ldfu.server.storage.Storage;
import net.fekepp.ldfu.server.storage.StorageResource;

public class DefaultResourceManager implements ResourceManager {

	private Storage storage;

	public DefaultResourceManager(Storage storage) {
		this.storage = storage;
	}

	@Override
	public Source getResource(Description description) throws ResourceNotFoundException,
			ContainerIdentifierExpectedException, ResourceIdentifierExpectedException {

		// Get the source
		StorageResource source = storage.getResource(description.getIdentifier());

		// If source is available
		if (source != null) {

			// Get the source format
			Format sourceFormat = source.getFormat();

			// If source format is available
			if (sourceFormat != null) {

				// Get the sink format
				Format sinkFormat = description.getFormat();

				// If sink format is available
				if (sinkFormat != null) {

					// Get converter from source format to sink format
					FormatConverter formatConverter = sourceFormat.buildFormatConverter(sinkFormat);

					// If converter is available
					if (formatConverter != null) {

						// Return data serialized with the sink format
						return new ResourceSource(description.getBaseUri(), description.getIdentifier(), sinkFormat,
								source.getInputStream(), formatConverter);

					}

				}

				// If sink format is not available or sink format handling
				// failed

				// Get converter from source format to the default format of the
				// source format group
				FormatConverter formatConverter = sourceFormat
						.buildFormatConverter(sourceFormat.getFormatGroup().getDefaultFormat());

				// If converter is available
				if (formatConverter != null) {

					// Return data serialized with the default format of the
					// source format group
					return new ResourceSource(description.getBaseUri(), description.getIdentifier(),
							sourceFormat.getFormatGroup().getDefaultFormat(), source.getInputStream(), formatConverter);

				}

			}

			// If source format is not available or source format handling
			// failed

			// Return binary data
			return new ResourceSource(description.getBaseUri(), description.getIdentifier(), null,
					source.getInputStream());

		}

		// If source is not available

		// Return null
		return null;

	}

	@Override
	public void setResource(Source source) throws ContainerIdentifierExpectedException,
			ResourceIdentifierExpectedException, ParentNotFoundException, IOException {

		// Get the source format
		Format sourceFormat = source.getFormat();

		// If source format is available
		if (sourceFormat != null) {

			try {

				// Get the sink resource
				StorageResource sink = storage.getResource(source.getIdentifier());

				// Get the sink format
				Format sinkFormat = sink.getFormat();

				// If sink format is available
				if (sinkFormat != null) {

					// Get converter from source format to sink format
					FormatConverter formatConverter = sourceFormat.buildFormatConverter(sinkFormat);

					// If converter is available
					if (formatConverter != null) {

						// TODO TMP
						ResourceSource test = new ResourceSource(source.getBaseUri(), source.getIdentifier(),
								sinkFormat, source.getInputStream(), formatConverter);

						// Set sink to data serialized in format of sink
						storage.setResource(source.getIdentifier(), null);

						return;

					}

				}

			}

			// If source is not existing
			catch (ResourceNotFoundException e) {

				// Ignore because set sink will be set to data serialized in
				// default format of source

			}

			// If sink is not existing or sink handling failed

			// Get converter from source format to the default format of the
			// source format group
			FormatConverter formatConverter = sourceFormat
					.buildFormatConverter(sourceFormat.getFormatGroup().getDefaultFormat());

			// If converter is available
			if (formatConverter != null) {

				ResourceSource test = new ResourceSource(source.getBaseUri(), source.getIdentifier(),
						sourceFormat.getFormatGroup().getDefaultFormat(), source.getInputStream(), formatConverter);

				// Set sink to data serialized in default format of source
				// format group
				storage.setResource(source.getIdentifier(), null);

				return;

			}

		}

		// If source format is not available

		// Set sink to binary data
		storage.setResource(source.getIdentifier(), null);

		return;

	}

	@Override
	public void delResource(Description description) throws ResourceNotFoundException,
			ResourceIdentifierExpectedException, ContainerIdentifierExpectedException, IOException {

		// Delete the resource from storage
		storage.delResource(description.getIdentifier());

	}

	@Override
	public Source proResource(Source source) {
		return null;
	}

	// public SourceResourceOLD getResource(String identifier, String mediaType,
	// URI base)
	// throws ResourceNotFoundException, ContainerIdentifierExpectedException,
	// ResourceIdentifierExpectedException {
	//
	// // Get the source
	// StorageResource source = storage.getResource(identifier);
	//
	// // Get the source format
	// Format sourceFormat = source.getFormat();
	//
	// // If source format is available
	// if (sourceFormat != null) {
	//
	// // Get the sink format
	// Format sinkFormat = mediaTypeToFormatMap.get(mediaType);
	//
	// // If sink format is available
	// if (sinkFormat != null) {
	//
	// // Get converter from source format to sink format
	// FormatConverter formatConverter =
	// sourceFormat.buildFormatConverter(sinkFormat);
	//
	// // If converter is available
	// if (formatConverter != null) {
	//
	// Res res = new Res(base, identifier, source.getInputStream(), sinkFormat,
	// formatConverter);
	//
	// return new SourceResourceOLD(identifier, formatConverter);
	//
	// }
	//
	// }
	//
	// // If sink format is available
	// // And if source format group equals sink format group
	// if (sinkFormat != null &&
	// sourceFormat.getFormatGroup().equals(sinkFormat.getFormatGroup())) {
	//
	// // Return data serialized with the media type of the
	// // sink format
	// // TODO Finalize implementation
	//
	// // Parser parser = sourceFormat.getParser();
	//
	// // Serializer serializer = sinkFormat.getSerializer();
	//
	// // FormatConverter converter =
	// // sourceFormat.getFormatGroup().getConverter(sinkFormat);
	//
	// // SourceOLD source = new SourceOLD(base, sinkFormat,
	// // converter);
	//
	// // converter.setSourceFormat(sourceFormat);
	// // converter.setSinkFormat(sinkFormat);
	// // converter.setSourceInputStream(source.getData());
	//
	// // testSource.setInputStream(source.getData());
	//
	// return null;
	//
	// }
	//
	// // If sink format is not available
	// // Or if source format group not equals sink format group
	// else {
	//
	// // Return data serialized with default media type of the
	// // source format group
	// // TODO Finalize implementation
	//
	// FormatConverter formatConverter = sourceFormat
	// .buildFormatConverter(sourceFormat.getFormatGroup().getDefaultFormat());
	//
	// Res res = new Res(base, identifier, source.getInputStream(),
	// sourceFormat.getFormatGroup().getDefaultFormat(), formatConverter);
	//
	// return null;
	//
	// }
	//
	// }
	//
	// // If source format is not available
	// else {
	//
	// // Return binary data
	// // TODO Finalize implementation
	//
	// Res res = new Res(base, identifier, source.getInputStream());
	//
	// return null;
	//
	// }
	//
	// }

	// public void setResource(String identifier, String mediaType, InputStream
	// data, URI base)
	// throws ContainerIdentifierExpectedException,
	// ResourceIdentifierExpectedException, ParentNotFoundException,
	// ParseException, IOException {
	//
	// // Get the source format
	// Format sourceFormat = mediaTypeToFormatMap.get(mediaType);
	//
	// // If source format is available
	// if (sourceFormat != null) {
	//
	// try {
	//
	// // Get the sink resource
	// StorageResource sink = storage.getResource(identifier);
	//
	// // Get the sink format
	// Format sinkFormat = sink.getFormat();
	//
	// // If sink format is available
	// // And if source format group equals sink format group
	// if (sinkFormat != null &&
	// sourceFormat.getFormatGroup().equals(sinkFormat.getFormatGroup())) {
	//
	// // Set sink to data serialized in format of sink
	// // TODO Finalize implementation
	// storage.setResource(identifier, null);
	// return;
	//
	// }
	//
	// }
	//
	// // If source is not existing
	// catch (ResourceNotFoundException e) {
	//
	// // Ignore because set sink will be set to data serialized in
	// // default format of source
	//
	// }
	//
	// // If sink is not existing
	// // Or if sink format is not available
	// // Or if source format group not equals sink format group
	//
	// // Set sink to data serialized in default format of source
	// // format group
	// // TODO Finalize implementation
	// storage.setResource(identifier, null);
	// return;
	//
	// }
	//
	// // If source format is not available
	// else {
	//
	// // Set sink to binary data
	// // TODO Finalize implementation
	// storage.setResource(identifier, null);
	// return;
	//
	// }
	//
	// }

	// public void delResource(String identifier) throws
	// ResourceNotFoundException, ResourceIdentifierExpectedException,
	// ContainerIdentifierExpectedException, IOException {
	//
	// // Delete the resource from storage
	// storage.delResource(identifier);
	//
	// }

	// public OutputStream proResource(String identifier, String mediaType,
	// InputStream data, URI base)
	// throws ResourceNotFoundException, ResourceIdentifierExpectedException,
	// ContainerIdentifierExpectedException {
	// return null;
	// }

}