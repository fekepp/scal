package net.fekepp.ldfu.server.resource;

import java.io.IOException;

import net.fekepp.ldfu.server.data.Format;
import net.fekepp.ldfu.server.data.FormatConverter;
import net.fekepp.ldfu.server.exceptions.ContainerIdentifierExpectedException;
import net.fekepp.ldfu.server.exceptions.ParentNotFoundException;
import net.fekepp.ldfu.server.exceptions.ParseException;
import net.fekepp.ldfu.server.exceptions.ParserException;
import net.fekepp.ldfu.server.exceptions.ResourceIdentifierExpectedException;
import net.fekepp.ldfu.server.exceptions.ResourceNotFoundException;
import net.fekepp.ldfu.server.storage.StorageManager;

public class DefaultResourceManager implements ResourceManager {

	private StorageManager storage;

	public DefaultResourceManager(StorageManager storage) {
		this.storage = storage;
	}

	@Override
	public Source getResource(Description description) throws ResourceNotFoundException,
			ContainerIdentifierExpectedException, ResourceIdentifierExpectedException, IOException {

		// Get the source
		Source source = storage.getResource(description);

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
	public void setResource(Source source)
			throws ContainerIdentifierExpectedException, ResourceIdentifierExpectedException, ParentNotFoundException,
			ParseException, ParserException, InterruptedException, IOException {

		// Get the source format
		Format sourceFormat = source.getFormat();

		// If source format is available
		if (sourceFormat != null) {

			try {

				// Get the sink resource
				Description sink = storage.getResource(source);

				// Get the sink format
				Format sinkFormat = sink.getFormat();

				// If sink format is available
				if (sinkFormat != null) {

					// Get converter from source format to sink format
					FormatConverter formatConverter = sourceFormat.buildFormatConverter(sinkFormat);

					// If converter is available
					if (formatConverter != null) {

						// Set sink to data serialized in format of sink
						storage.setResource(new ResourceSource(source.getBaseUri(), source.getIdentifier(), sinkFormat,
								source.getInputStream(), formatConverter));

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

				// Set sink to data serialized in default format of source
				// format group
				storage.setResource(new ResourceSource(source.getBaseUri(), source.getIdentifier(),
						sourceFormat.getFormatGroup().getDefaultFormat(), source.getInputStream(), formatConverter));

				return;

			}

		}

		// If source format is not available

		// Set sink to binary data
		storage.setResource(source);

		return;

	}

	@Override
	public void delResource(Description description) throws ResourceNotFoundException,
			ResourceIdentifierExpectedException, ContainerIdentifierExpectedException, IOException {

		// Delete the resource from storage
		storage.delResource(description);

	}

	@Override
	public Source proResource(Source source) {
		return null;
	}

}