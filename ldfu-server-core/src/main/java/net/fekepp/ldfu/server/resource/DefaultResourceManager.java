package net.fekepp.ldfu.server.resource;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fekepp.ldfu.server.data.Format;
import net.fekepp.ldfu.server.data.FormatConverter;
import net.fekepp.ldfu.server.data.formats.TurtleFormat;
import net.fekepp.ldfu.server.exceptions.ContainerIdentifierExpectedException;
import net.fekepp.ldfu.server.exceptions.ParentNotFoundException;
import net.fekepp.ldfu.server.exceptions.ParseException;
import net.fekepp.ldfu.server.exceptions.ParserException;
import net.fekepp.ldfu.server.exceptions.ResourceIdentifierExpectedException;
import net.fekepp.ldfu.server.exceptions.ResourceNotFoundException;
import net.fekepp.ldfu.server.storage.StorageManager;

public class DefaultResourceManager implements ResourceManager {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private StorageManager storageManager;
	private StorageManager storageManagerTemporary;

	public DefaultResourceManager(StorageManager storageManager, StorageManager storageManagerTemporary) {
		this.storageManager = storageManager;
		this.storageManagerTemporary = storageManagerTemporary;
	}

	@Override
	public Source getResource(Description description) throws ResourceNotFoundException,
			ContainerIdentifierExpectedException, ResourceIdentifierExpectedException, IOException {

		// Get the source
		Source source = storageManager.getResource(description);

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
						.buildFormatConverter(sourceFormat.getModel().getDefaultFormat());

				// If converter is available
				if (formatConverter != null) {

					// Return data serialized with the default format of the
					// source format group
					return new ResourceSource(description.getBaseUri(), description.getIdentifier(),
							sourceFormat.getModel().getDefaultFormat(), source.getInputStream(), formatConverter);

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
				Description sink = storageManager.getResource(source);

				// Get the sink format
				Format sinkFormat = sink.getFormat();

				// If sink format is available
				if (sinkFormat != null) {

					// Get converter from source format to sink format
					FormatConverter formatConverter = sourceFormat.buildFormatConverter(sinkFormat);

					// If converter is available
					if (formatConverter != null) {

						// Set sink to data serialized in format of sink
						storageManager.setResource(new ResourceSource(source.getBaseUri(), source.getIdentifier(),
								sinkFormat, source.getInputStream(), formatConverter));

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
					.buildFormatConverter(sourceFormat.getModel().getDefaultFormat());

			// If converter is available
			if (formatConverter != null) {

				// Set sink to data serialized in default format of source
				// format group
				storageManager.setResource(new ResourceSource(source.getBaseUri(), source.getIdentifier(),
						sourceFormat.getModel().getDefaultFormat(), source.getInputStream(), formatConverter));

				return;

			}

		}

		// If source format is not available

		// Set sink to binary data
		storageManager.setResource(source);

		return;

	}

	@Override
	public void delResource(Description description) throws ResourceNotFoundException,
			ResourceIdentifierExpectedException, ContainerIdentifierExpectedException, IOException {

		// Delete the resource from storage
		storageManager.delResource(description);

	}

	@Override
	public Source proResource(Source input)
			throws ResourceNotFoundException, ContainerIdentifierExpectedException, ResourceIdentifierExpectedException,
			ParentNotFoundException, ParseException, ParserException, InterruptedException, IOException {

		// public Source proResource(Identifier identifier, Source input, Format
		// outputFormat)

		// Should probably be read from output description in
		// "proResource(Source input, Description output)"
		// or
		// "proResource(Source input, Format outputFormat)"
		// to trigger listeners for specific output formats/models
		Format outputFormat = TurtleFormat.getInstance();

		// Throws not found
		Source storage = storageManager.getResource(input);

		// if (storage != null) {

		Source storageTemporary = null;

		// Description to be generated
		Description descriptionTemporary = null;

		do {

			// Generate a unique identifier
			UUID uuid = UUID.randomUUID();

			// Create a description based on the identifier string
			descriptionTemporary = new ResourceDescription(null, uuid.toString());

			try {
				storageTemporary = storageManagerTemporary.getResource(descriptionTemporary);
			}

			catch (ResourceNotFoundException e) {
				// Ignore
			}

		}

		// Repeat if a temporary resource with already exists
		// We ignore that until the identifier may get created until it is
		// actually used below
		while (storageTemporary != null);

		Format inputFormat = input.getFormat();

		FormatConverter inputFormatConverter = inputFormat == null ? null
				: inputFormat.buildFormatConverter(inputFormat.getModel().getDefaultFormat());

		logger.info(
				"storageManagerTemporary.setResource() >\nbaseUri={}\nidentifier={}\nformat={}\ninputStream={}\nformatConverter={}",
				input.getBaseUri(), descriptionTemporary.getIdentifier(), inputFormat.getModel().getDefaultFormat(),
				input.getInputStream(), inputFormatConverter);

		storageManagerTemporary.setResource(new ResourceSource(input.getBaseUri(), descriptionTemporary.getIdentifier(),
				inputFormat.getModel().getDefaultFormat(), input.getInputStream(), inputFormatConverter));

		return storageManagerTemporary.getResource(descriptionTemporary);

	}

}