package net.fekepp.ldp.resource;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fekepp.ldp.Description;
import net.fekepp.ldp.Format;
import net.fekepp.ldp.FormatConverter;
import net.fekepp.ldp.FormatConverterListener;
import net.fekepp.ldp.FormatConverterListenerDelegate;
import net.fekepp.ldp.ResourceListener;
import net.fekepp.ldp.ResourceListenerDelegate;
import net.fekepp.ldp.Model;
import net.fekepp.ldp.ResourceManager;
import net.fekepp.ldp.Source;
import net.fekepp.ldp.StorageManager;
import net.fekepp.ldp.exception.ContainerIdentifierExpectedException;
import net.fekepp.ldp.exception.ConverterException;
import net.fekepp.ldp.exception.ParentNotFoundException;
import net.fekepp.ldp.exception.ParseException;
import net.fekepp.ldp.exception.ParserException;
import net.fekepp.ldp.exception.ResourceIdentifierExpectedException;
import net.fekepp.ldp.exception.ResourceNotFoundException;
import net.fekepp.ldp.listener.ContainerResourceListener;

public class DefaultResourceManager implements ResourceManager, ResourceListenerDelegate {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private StorageManager storageManager;
	private StorageManager storageManagerTemporary;

	private Set<ResourceListener> listeners = new HashSet<ResourceListener>();

	public DefaultResourceManager(StorageManager storageManager, StorageManager storageManagerTemporary) {

		this.storageManager = storageManager;
		this.storageManagerTemporary = storageManagerTemporary;

		ContainerResourceListener containerResourceListener = new ContainerResourceListener(this);
		containerResourceListener.getMethods().add("pro");
		listeners.add(containerResourceListener);

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
			ParseException, ParserException, ConverterException, InterruptedException, IOException {

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

			// TODO Maybe try before falling back to binary to save file in the
			// given source format if converter to default format is not
			// available

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
	public Source proResource(Source input, Format outputFormat) throws ResourceNotFoundException,
			ContainerIdentifierExpectedException, ResourceIdentifierExpectedException, ParentNotFoundException,
			ParseException, ParserException, ConverterException, InterruptedException, IOException {

		logger.info("PRO > START");

		// Get storage
		Source storage = storageManager.getResource(input);

		// If storage is available
		if (storage != null) {

			/*
			 * Derive base, identifier, models, formats, default formats, and
			 * format converters
			 */

			// Derive the base
			URI base = input.getBaseUri();

			// Derive the identifier
			String identifier = input.getIdentifier();

			// Derive the storage model, format, default format, and format
			// converter
			Model storageModel = null;
			Format storageFormat = storage.getFormat();
			Format storageFormatDefault = null;
			FormatConverter storageFormatConverter = null;
			if (storageFormat != null) {
				storageModel = storageFormat.getModel();
				storageFormatDefault = storageFormat.getModel().getDefaultFormat();
				storageFormatConverter = storageFormat.buildFormatConverter(storageFormatDefault);
				storageFormatConverter.setBaseUri(base);
			}

			// Derive the input model, format, default format, and format
			// converter
			Model inputModel = null;
			Format inputFormat = input.getFormat();
			Format inputFormatDefault = null;
			FormatConverter inputFormatConverter = null;
			if (inputFormat != null) {
				inputModel = inputFormat.getModel();
				inputFormatDefault = inputFormat.getModel().getDefaultFormat();
				inputFormatConverter = inputFormat.buildFormatConverter(inputFormatDefault);
				inputFormatConverter.setBaseUri(base);
			}

			// Derive the output model and default format
			Model outputModel = null;
			// Format outputFormatDefault = null;
			if (outputFormat != null) {
				outputModel = outputFormat.getModel();
				// outputFormatDefault = outputModel.getDefaultFormat();
			}

			logger.info("inputFormat={} | storageFormat={} | outputFormat={}",
					inputFormat == null ? "null" : inputFormat.getName(),
					storageFormat == null ? "null" : storageFormat.getName(),
					outputFormat == null ? "null" : outputFormat.getName());

			// Create list of listeners that are still interested after metadata
			Set<ResourceListener> listenersInterested = new HashSet<ResourceListener>();

			final Set<ResourceListener> listenersNotInterestedInStorage = new HashSet<ResourceListener>();
			final Set<ResourceListener> listenersNotInterestedInInput = new HashSet<ResourceListener>();

			/*
			 * Select listeners
			 */

			logger.info("PRO > SELECT LISTENERS");

			// Fill list with interested listeners based on metadata
			for (final ResourceListener listener : listeners) {

				logger.info("Check interest of listener > {}", listener);

				if (!listener.isListeningOnIdentifier(identifier))
					continue;

				if (!listener.isListeningOnMethod("pro"))
					continue;

				if (!listener.isListeningOnInput(inputModel))
					continue;

				if (!listener.isListeningOnInput(inputFormat))
					continue;

				if (!listener.isListeningOnStorage(storageModel))
					continue;

				if (!listener.isListeningOnStorage(storageFormat))
					continue;

				if (!listener.isListeningOnOutput(outputModel))
					continue;

				if (!listener.isListeningOnOutput(outputFormat))
					continue;

				// Listener still is interested after metadata checks
				logger.info("Listener still is interested after metadata checks");

				// Add listener to set of interested listeners
				listenersInterested.add(listener);

				// Build converter listeners for storage, input, and output
				final Set<FormatConverterListener> storageFormatConverterListeners = listener
						.buildStorageFormatConverterListener(storageFormatConverter);
				final Set<FormatConverterListener> inputFormatConverterListeners = listener
						.buildInputFormatConverterListener(inputFormatConverter);

				// Continue if listener is not interested in any converters
				if (storageFormatConverterListeners == null && inputFormatConverterListeners == null)
					continue;

				if (storageFormatConverterListeners != null) {
					logger.info("Listener is interested in storage payload");
					listenersNotInterestedInStorage.add(listener);
					for (FormatConverterListener storageFormatConverterListener : storageFormatConverterListeners) {
						storageFormatConverterListener
								.setFormatConverterListenerDelegate(new FormatConverterListenerDelegate() {
									@Override
									public void process() {
										logger.info(
												"Removing listener from list of not interested storage payload listeners");
										listenersNotInterestedInStorage.remove(listener);
									}
								});
					}
				}

				if (inputFormatConverterListeners != null) {
					logger.info("Listener is interested in input payload");
					listenersNotInterestedInInput.add(listener);
					for (FormatConverterListener inputFormatConverterListener : inputFormatConverterListeners) {
						inputFormatConverterListener
								.setFormatConverterListenerDelegate(new FormatConverterListenerDelegate() {

									@Override
									public void process() {
										logger.info(
												"Removing listener from list of not interested input payload listeners");
										listenersNotInterestedInInput.remove(listener);
									}
								});
					}
				}

			}

			/*
			 * Stream storage to null
			 */

			logger.info("PRO > STREAM STORAGE");

			// TODO Take care of stream closing
			if (!listenersNotInterestedInStorage.isEmpty()) {

				logger.info("Stream storage to null to evaluate storage format converter listeners");

				storageFormatConverter.setInputStream(storage.getInputStream());
				storageFormatConverter.setOutputStream(new OutputStream() {

					@Override
					public void write(int arg0) throws IOException {
						// Ignore
					}

				});

				storageFormatConverter.convert();

				logger.info("Close storage input and output streams");
				storageFormatConverter.getInputStream().close();
				storageFormatConverter.getOutputStream().close();

			} else

			{

				logger.info("Close storage because no storage format converter listeners");
				storage.getInputStream().close();

			}

			// Remove all listeners that are not interested in the storage
			listenersInterested.removeAll(listenersNotInterestedInStorage);

			/*
			 * Stream input to temporary storage
			 */

			logger.info("PRO > STREAM INPUT");

			// Generate description for temporary storage. Repeat if a temporary
			// resource with already exists. We ignore that until the identifier
			// may get created until it is actually used below
			Source storageTemporary = null;
			Description descriptionTemporary = null;
			do {
				descriptionTemporary = new ResourceDescription(null, UUID.randomUUID().toString());
				try {
					storageTemporary = storageManagerTemporary.getResource(descriptionTemporary);
				} catch (ResourceNotFoundException e) {
					// Close input stream and ignore
					// TODO Correctly close the input stream
					storage.getInputStream().close();
				}
			} while (storageTemporary != null);

			// Write input to temporary storage
			logger.info(
					"storageManagerTemporary.setResource() > baseUri={} | identifier={} | format={} | inputStream={} | formatConverter={}",
					base, descriptionTemporary.getIdentifier(), inputFormatDefault, input.getInputStream(),
					inputFormatConverter);

			// TODO Converter not necessary available
			storageManagerTemporary.setResource(new ResourceSource(base, descriptionTemporary.getIdentifier(),
					inputFormatDefault, input.getInputStream(), inputFormatConverter));

			// Remove all listeners that are not interested in the input
			listenersInterested.removeAll(listenersNotInterestedInInput);

			/*
			 * Process storage and input to output by interested listeners
			 */

			logger.info("PRO > PROCESS STORAGE WITH INPUT");

			// Let interested listeners process the storage and input
			Set<Source> processOutputs = new HashSet<Source>();
			for (ResourceListener listener : listeners) {
				if (listener.getListenerDelegate() != null) {

					Source processStorage = storageManager.getResource(storage);
					Source processInput = storageManagerTemporary.getResource(descriptionTemporary);
					Source processOutput = listener.getListenerDelegate().process(processStorage,
							new ResourceSource(base, processInput.getIdentifier(), processInput.getFormat(),
									processInput.getInputStream()));

					processOutputs.add(processOutput);

				}
			}

			Source processOutput = null;
			boolean firstOutput = false;
			for (Source currentProcessOutput : processOutputs) {
				if (currentProcessOutput != null && firstOutput) {
					processOutput = currentProcessOutput;
					firstOutput = false;
				} else if (currentProcessOutput != null) {
					logger.warn("Multiple listener with outputs > Discarded non-first output");
					processOutput.getInputStream().close();
				}
			}

			// If process output is available
			if (processOutput != null) {

				// Get the process output format
				Format processOutputFormat = processOutput.getFormat();

				// If process output format is available
				if (processOutputFormat != null) {

					// If output format is available
					if (outputFormat != null) {

						// Get converter from process output format to output
						// format
						FormatConverter formatConverter = processOutputFormat.buildFormatConverter(outputFormat);

						// If converter is available
						if (formatConverter != null) {

							// Return data serialized with the output format
							return new ResourceSource(input.getBaseUri(), input.getIdentifier(), outputFormat,
									processOutput.getInputStream(), formatConverter);

						}

					}

					// If output format is not available or output format
					// handling
					// failed

					// Get converter from process output format to the default
					// format of the process output format group
					FormatConverter formatConverter = processOutputFormat
							.buildFormatConverter(processOutputFormat.getModel().getDefaultFormat());

					// If converter is available
					if (formatConverter != null) {

						// Return data serialized with the default format of the
						// process output format group
						return new ResourceSource(input.getBaseUri(), input.getIdentifier(),
								processOutputFormat.getModel().getDefaultFormat(), processOutput.getInputStream(),
								formatConverter);

					}

				}

				// If process output format is not available or process output
				// format handling failed

				// Return binary data
				return new ResourceSource(input.getBaseUri(), input.getIdentifier(), null,
						processOutput.getInputStream());

				// TODO Implement a mechanism to delete temporary resource once
				// they
				// are completely processed

			}

		}

		// If storage is not or processing output is not available

		// Return null
		return null;

	}

	@Override
	public Source process(Source storage, Source input)
			throws ContainerIdentifierExpectedException, ResourceIdentifierExpectedException, ParentNotFoundException,
			ParseException, ParserException, ConverterException, InterruptedException, IOException {

		logger.info("public Source process(...) > storage={} | input={}", storage.getIdentifier(),
				input.getIdentifier());

		logger.info("setResource() > baseUri={} | identifier={} | format={} | inputStream={}", input.getBaseUri(),
				storage.getIdentifier() + input.getIdentifier(), input.getFormat(), input.getInputStream());

		setResource(new ResourceSource(input.getBaseUri(), storage.getIdentifier() + input.getIdentifier(),
				input.getFormat(), input.getInputStream()));

		return null;

	}

}