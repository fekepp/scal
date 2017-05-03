package net.fekepp.ldp.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.RedirectionException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fekepp.ldp.Format;
import net.fekepp.ldp.Models;
import net.fekepp.ldp.ResourceManager;
import net.fekepp.ldp.Source;
import net.fekepp.ldp.exception.ContainerIdentifierExpectedException;
import net.fekepp.ldp.exception.ConverterException;
import net.fekepp.ldp.exception.ParentNotFoundException;
import net.fekepp.ldp.exception.ParseException;
import net.fekepp.ldp.exception.ParserException;
import net.fekepp.ldp.exception.ResourceIdentifierExpectedException;
import net.fekepp.ldp.exception.ResourceNotFoundException;
import net.fekepp.ldp.resource.ResourceDescription;
import net.fekepp.ldp.resource.ResourceSource;

/**
 * @author "Felix Leif Keppmann"
 */
@Path("{path:.*}")
public class Servlet {

	private static ServerController controller;

	private static ResourceManager resourceManager;

	private static Map<String, Format> mediaTypeToFormatMap = Models.getMediaTypesMap();

	public static ServerController getController() {
		return controller;
	}

	public static void setController(ServerController controller) {
		Servlet.controller = controller;
	}

	public static ResourceManager getResourceManager() {
		return resourceManager;
	}

	public static void setResourceManager(ResourceManager resourceManager) {
		Servlet.resourceManager = resourceManager;
	}

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Context
	UriInfo uriInfo;

	@Context
	HttpHeaders httpHeaders;

	@GET
	public Response getResource(@PathParam("path") String path) {

		logger.info("getResource(String path) > {}", path);

		// Get the URI base
		URI base = uriInfo.getBaseUri();

		// Log the URI base
		logger.info("URI Base > {}", base);

		// Log the URI path
		logger.info("URI Path > {}", uriInfo.getRequestUri().getPath());

		// Log acceptable media types
		for (MediaType accept : httpHeaders.getAcceptableMediaTypes()) {
			logger.info("httpHeaders.getAcceptableMediaTypes() > {}", accept);
		}

		try {

			// Get the resource for the path
			// TODO Support more than one acceptable media type
			final Source resource = resourceManager.getResource(new ResourceDescription(base, path,
					mediaTypeToFormatMap.get(httpHeaders.getAcceptableMediaTypes().get(0).toString())));

			// If the resource exists
			if (resource != null) {

				// Return with HTTP 200 and the binary representation of the
				// resource as payload
				ResponseBuilder responseBuilder = Response.ok(new StreamingOutput() {

					@Override
					public void write(OutputStream outputStream) throws IOException, WebApplicationException {
						try {
							resource.streamTo(outputStream);
						} catch (ParseException | ParserException | ConverterException | InterruptedException e) {
							throw new InternalServerErrorException(e);
						}
					}

				});

				if (resource.getFormat() != null) {
					responseBuilder = responseBuilder.header("Content-Type",
							resource.getFormat().getDefaultMediaType());
				}

				return responseBuilder.build();

			}

			// Else if the resource does not exists
			// This should not happen because common failures are covered by the
			// exceptions
			else {

				// Response with HTTP 500
				throw new InternalServerErrorException("Failed to read the resource");

			}

		}

		catch (ResourceNotFoundException e) {

			// Response with HTTP 404
			throw new NotFoundException();

		}

		catch (ResourceIdentifierExpectedException e) {

			// Response with HTTP 301 Moved Permanentaly to the
			// correct identifier without a slash
			throw new RedirectionException(Status.MOVED_PERMANENTLY,
					URI.create(base.toString().substring(0, base.toString().length() - 1)));

		}

		catch (ContainerIdentifierExpectedException e) {

			// Response with HTTP 301 Moved Permanentaly to the
			// correct identifier with slash
			throw new RedirectionException(Status.MOVED_PERMANENTLY, URI.create(base.toString() + "/"));

		} catch (IOException e) {

			// TODO Align with specified LDP behaviour
			throw new BadRequestException("Could not create the resource");

		} catch (ParentNotFoundException e1) {
			// TODO Align with specified LDP behaviour
			throw new BadRequestException("Could not create the resource");
		} catch (ParseException e1) {
			// TODO Align with specified LDP behaviour
			throw new BadRequestException("Could not create the resource");
		} catch (ParserException e1) {
			// TODO Align with specified LDP behaviour
			throw new BadRequestException("Could not create the resource");
		} catch (ConverterException e1) {
			// TODO Align with specified LDP behaviour
			throw new BadRequestException("Could not create the resource");
		} catch (InterruptedException e1) {
			// TODO Align with specified LDP behaviour
			throw new BadRequestException("Could not create the resource");
		}

	}

	@PUT
	public Response putResource(@PathParam("path") String path, InputStream inputStream) {

		// Log the request
		logger.info("putResource(String path, InputStream inputStream) > path={}", path);

		// Get the URI base
		URI base = uriInfo.getBaseUri();

		// Log the URI base
		logger.info("URI Base > {}", base);

		// Log the URI path
		logger.info("URI Path > {}", uriInfo.getRequestUri().getPath());

		// Get the media type of the request
		MediaType mediaType = httpHeaders.getMediaType();

		// Format
		Format format = mediaTypeToFormatMap.get(httpHeaders.getMediaType().toString());

		// Log the media type
		logger.info("httpHeaders.getMediaType() > {}", mediaType);

		// Try to
		try {

			// Set the resource for the path
			// resourceManager.setResource(path, mediaType.toString(),
			// inputStream, uri);
			resourceManager.setResource(new ResourceSource(base, path, format, inputStream));

			// Response with HTTP 200
			return Response.ok().build();

		}

		catch (ContainerIdentifierExpectedException e) {

			// Response with HTTP 301 Moved Permanentaly to the correct
			// identifier with slash
			throw new RedirectionException(Status.MOVED_PERMANENTLY, URI.create(base.toString() + "/"));

		}

		catch (ResourceIdentifierExpectedException e) {

			// Response with HTTP 301 Moved Permanentaly to the correct
			// identifier without a slash
			throw new RedirectionException(Status.MOVED_PERMANENTLY,
					URI.create(base.toString().substring(0, base.toString().length() - 1)));

		}

		catch (ParentNotFoundException e) {

			// TODO Align with specified LDP behaviour
			throw new BadRequestException("Parnet not found");

		}

		catch (IOException e) {

			// TODO Align with specified LDP behaviour
			throw new BadRequestException("Could not create the resource");

		}

		catch (ParseException e) {

			// TODO Align with specified LDP behaviour
			throw new BadRequestException("Could not create the resource");

		}

		catch (ParserException e) {

			// TODO Align with specified LDP behaviour
			throw new BadRequestException("Could not create the resource");

		}

		catch (ConverterException e) {

			// TODO Align with specified LDP behaviour
			throw new BadRequestException("Could not create the resource");

		}

		catch (InterruptedException e) {

			// TODO Align with specified LDP behaviour
			throw new BadRequestException("Could not create the resource");

		}

	}

	@POST
	public Response proResource(@PathParam("path") String path, InputStream inputStream) {

		// Log the request
		logger.info("postResource(String path, InputStream inputStream) > path={}", path);

		// Get the URI base
		URI base = uriInfo.getBaseUri();

		// Log the URI base
		logger.info("URI Base > {}", base);

		// Log the URI path
		logger.info("URI Path > {}", uriInfo.getRequestUri().getPath());

		// Get the media type of the request
		MediaType mediaType = httpHeaders.getMediaType();

		// Format
		Format format = mediaTypeToFormatMap.get(httpHeaders.getMediaType().toString());

		// Log the media type
		logger.info("httpHeaders.getMediaType() > {}", mediaType);

		// Try to
		try {

			// Process the input with resource for the path
			// TODO Support more than one acceptable media type
			final Source resource = resourceManager.proResource(new ResourceSource(base, path, format, inputStream),
					mediaTypeToFormatMap.get(httpHeaders.getAcceptableMediaTypes().get(0).toString()));

			// If the processing of the input returns an output exists
			if (resource != null) {

				// Return with HTTP 200 and the binary representation of the
				// resource as payload
				ResponseBuilder responseBuilder = Response.ok(new StreamingOutput() {

					@Override
					public void write(OutputStream outputStream) throws IOException, WebApplicationException {
						try {
							resource.streamTo(outputStream);
						} catch (ParseException | ParserException | ConverterException | InterruptedException e) {
							throw new InternalServerErrorException(e);
						}
					}

				});

				if (resource.getFormat() != null) {
					responseBuilder = responseBuilder.header("Content-Type",
							resource.getFormat().getDefaultMediaType());
				}

				return responseBuilder.build();

			}

			// Else if the resource does not exists
			// This should not happen because common failures are covered by the
			// exceptions
			else {

				// Response with HTTP 500
				// throw new InternalServerErrorException("Failed to read the
				// resource");

				// Response with HTTP 200
				return Response.ok().build();

			}

		}

		catch (ResourceNotFoundException e) {

			// Response with HTTP 404
			throw new NotFoundException();

		}

		catch (ContainerIdentifierExpectedException e) {

			// Response with HTTP 301 Moved Permanentaly to the correct
			// identifier with slash
			throw new RedirectionException(Status.MOVED_PERMANENTLY, URI.create(base.toString() + "/"));

		}

		catch (ResourceIdentifierExpectedException e) {

			// Response with HTTP 301 Moved Permanentaly to the correct
			// identifier without a slash
			throw new RedirectionException(Status.MOVED_PERMANENTLY,
					URI.create(base.toString().substring(0, base.toString().length() - 1)));

		}

		catch (ParentNotFoundException e) {

			// TODO Align with specified LDP behaviour
			throw new BadRequestException("Parnet not found");

		}

		catch (IOException e) {

			// TODO Align with specified LDP behaviour
			throw new BadRequestException("Could not create the resource");

		}

		catch (ParseException e) {

			// TODO Align with specified LDP behaviour
			throw new BadRequestException("Could not create the resource");

		}

		catch (ParserException e) {

			// TODO Align with specified LDP behaviour
			throw new BadRequestException("Could not create the resource");

		}

		catch (ConverterException e) {

			// TODO Align with specified LDP behaviour
			throw new BadRequestException("Could not create the resource");

		}

		catch (InterruptedException e) {

			// TODO Align with specified LDP behaviour
			throw new BadRequestException("Could not create the resource");

		}

	}

	@DELETE
	public Response deleteResource(@PathParam("path") String path) {

		// Log the request
		logger.info("deleteResource(String path) > path={}", path);

		// Get the URI base
		URI base = uriInfo.getBaseUri();

		// Log the URI base
		logger.info("URI Base > {}", base);

		// Log the URI path
		logger.info("URI Path > {}", uriInfo.getRequestUri().getPath());

		try {

			// Delete the resource
			resourceManager.delResource(
					new ResourceDescription(base, path, mediaTypeToFormatMap.get(httpHeaders.getMediaType())));

			// Response with HTTP 200
			return Response.ok().build();

		}

		catch (ResourceNotFoundException e) {

			// Response with HTTP 404
			throw new NotFoundException();

		}

		catch (ResourceIdentifierExpectedException e) {

			// Response with HTTP 301 Moved Permanentaly to the
			// correct identifier without a slash
			throw new RedirectionException(Status.MOVED_PERMANENTLY,
					URI.create(base.toString().substring(0, base.toString().length() - 1)));

		}

		catch (ContainerIdentifierExpectedException e) {

			// Response with HTTP 301 Moved Permanentaly to the
			// correct identifier with slash
			throw new RedirectionException(Status.MOVED_PERMANENTLY, URI.create(base.toString() + "/"));

		}

		catch (IOException e) {

			// TODO Align with specified LDP behaviour
			throw new InternalServerErrorException("Could not delete the resource");

		}

	}

}