package net.fekepp.ldfu.server.webapi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fekepp.ldfu.server.storage.ContainerIdentifierExpectedException;
import net.fekepp.ldfu.server.storage.ParentNotFoundException;
import net.fekepp.ldfu.server.storage.ProcessingNotSupportedException;
import net.fekepp.ldfu.server.storage.ResourceIdentifierExpectedException;
import net.fekepp.ldfu.server.storage.ResourceNotFoundException;
import net.fekepp.ldfu.server.storage.Storage;
import net.fekepp.ldfu.server.storage.StorageResource;

/**
 * @author "Felix Leif Keppmann"
 */
// @Path("/")
@Path("{path:.*}")
public class Servlet {

	private static ServerController controller;

	private static Storage storage;

	public static ServerController getController() {
		return controller;
	}

	public static void setController(ServerController controller) {
		Servlet.controller = controller;
	}

	public static Storage getStorage() {
		return storage;
	}

	public static void setStorage(Storage storage) {
		Servlet.storage = storage;
	}

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Context
	UriInfo uriInfo;

	@Context
	HttpHeaders httpHeaders;

	@GET
	public Response getResource(@PathParam("path") String path) {

		// Log the request
		logger.info("getResource(String path) > path={}", path);

		// Get the request URI
		URI uri = uriInfo.getRequestUri();

		// Log the request URI
		logger.info("uri.getPath() > {}", uriInfo.getRequestUri().getPath());

		// Get the resource for the path
		final StorageResource resource = storage.getResource(path);

		// If the resource exists
		if (resource != null) {

			// If the resource is a RDF resource
			if (resource.isRdfResource()) {

				// If the resource is a container resource
				if (resource.isContainerResource()) {

					// If the resource identifier does not end with a slash
					if (!uri.getPath().endsWith("/")) {

						// Response with HTTP 301 Moved Permanentaly to the
						// correct identifier with slash
						throw new RedirectionException(Status.MOVED_PERMANENTLY, URI.create(uri.toString() + "/"));

					}

				}

				// Else if the resource is not a container resource
				else {

					// If the resource identifier ends with a slash
					if (uri.getPath().endsWith("/")) {

						// Response with HTTP 301 Moved Permanentaly to the
						// correct identifier without a slash
						throw new RedirectionException(Status.MOVED_PERMANENTLY,
								URI.create(uri.toString().substring(0, uri.toString().length() - 1)));

					}

				}

				// Response with HTTP 200 and the RDF representation of the
				// resource as payload
				return Response.ok(new GenericEntity<Iterable<Node[]>>(resource.getRdfRepresentation()) {
				}).build();

			}

			// Else if the resource is not a RDF resource
			else {

				// Return with HTTP 200 and the binary representation of the
				// resource as payload
				return Response.ok(new StreamingOutput() {

					@Override
					public void write(OutputStream outputStream) throws IOException, WebApplicationException {
						IOUtils.copy(resource.getBinaryRepresentation(), outputStream);
					}

				}).build();

			}

		}

		// Else if the resource does not exists
		else {

			// Response with HTTP 404
			throw new NotFoundException();

		}

	}

	@PUT
	public Response putResource(@PathParam("path") String path, InputStream inputStream) {

		// Log the request
		logger.info("putResource(String path, InputStream inputStream) > path={}", path);

		// Get the request URI
		URI uri = uriInfo.getRequestUri();

		// Log the request URI
		logger.info("uri.getPath() > {}", uriInfo.getRequestUri().getPath());

		// Get the media type of the request
		MediaType mediaType = httpHeaders.getMediaType();

		// Log the media type
		logger.info("httpHeaders.getMediaType() > {}", mediaType);

		// Get the resource for the path
		StorageResource resource = storage.getResource(path);

		// If the resource exists
		if (resource != null) {

			// If the resource is a container resource
			if (resource.isContainerResource()) {

				// If the resource identifier does not end with a slash
				if (!uri.getPath().endsWith("/")) {

					// Response with HTTP 301 Moved Permanentaly to the
					// correct identifier with slash
					throw new RedirectionException(Status.MOVED_PERMANENTLY, URI.create(uri.toString() + "/"));

				}

			}

			// Else if the resource is not a container resource
			else {

				// If the resource identifier ends with a slash
				if (uri.getPath().endsWith("/")) {

					// Response with HTTP 301 Moved Permanentaly to the
					// correct identifier without a slash
					throw new RedirectionException(Status.MOVED_PERMANENTLY,
							URI.create(uri.toString().substring(0, uri.toString().length() - 1)));

				}

			}

		}

		// StorageResource resource;
		try {

			resource = storage.setResource(path, mediaType, inputStream);

			// If the resource exists
			if (resource != null) {

				// If the resource is a RDF resource
				if (resource.isRdfResource()) {

					// Response with HTTP 200 and the RDF representation of the
					// resource as payload
					return Response.ok(new GenericEntity<Iterable<Node[]>>(resource.getRdfRepresentation()) {
					}).build();

				}

				// Else if the resource is not a RDF resource
				else {

					// Response with HTTP 200
					return Response.ok().build();

				}

			}

			// Else if the resource does not exists
			// This should not happen because common failures are covered by the
			// exceptions
			else {

				// Response with HTTP 500
				throw new InternalServerErrorException("Failed to create/update resource in the storage");

			}

		}

		catch (ContainerIdentifierExpectedException e) {

			// Response with HTTP 301 Moved Permanentaly to the
			// correct identifier with slash
			throw new RedirectionException(Status.MOVED_PERMANENTLY, URI.create(uri.toString() + "/"));

		}

		catch (ResourceIdentifierExpectedException e) {

			// Response with HTTP 301 Moved Permanentaly to the
			// correct identifier without a slash
			throw new RedirectionException(Status.MOVED_PERMANENTLY,
					URI.create(uri.toString().substring(0, uri.toString().length() - 1)));

		}

		catch (ParentNotFoundException e) {

			// TODO Align with specified LDP behaviour
			throw new BadRequestException("Parnet not found");

		}

		catch (ParseException e) {

			// TODO Align with specified LDP behaviour
			throw new BadRequestException("Could not parse the RDF representation");

		}

		catch (IOException e) {

			// TODO Align with specified LDP behaviour
			throw new BadRequestException("Could not create the resource");

		}

	}

	@POST
	public Response postResource(@PathParam("path") String path, InputStream inputStream) {

		// Log the request
		logger.info("postResource(String path, InputStream inputStream) > path={}", path);

		// Get the request URI
		URI uri = uriInfo.getRequestUri();

		// Log the request URI
		logger.info("uri.getPath() > {}", uriInfo.getRequestUri().getPath());

		// Get the media type of the request
		MediaType mediaType = httpHeaders.getMediaType();

		// Log the media type
		logger.info("httpHeaders.getMediaType() > {}", mediaType);

		try {

			// Process the resource with given input
			StorageResource resource = storage.processResource(path, mediaType, inputStream);

			// If the resource exists
			if (resource != null) {

				// If the resource is a RDF resource
				if (resource.isRdfResource()) {

					// Response with HTTP 200 and the RDF representation of the
					// resource as payload
					return Response.ok(new GenericEntity<Iterable<Node[]>>(resource.getRdfRepresentation()) {
					}).build();

				}

				// Else if the resource is not a RDF resource
				else {

					// Response with HTTP 200
					return Response.ok().build();

				}

			}

			// Else if the resource does not exists
			// This should not happen because common failures are covered by the
			// exceptions
			else {

				// Response with HTTP 500
				throw new InternalServerErrorException("Failed to create/update resource in the storage");

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
					URI.create(uri.toString().substring(0, uri.toString().length() - 1)));

		}

		catch (ContainerIdentifierExpectedException e) {

			// Response with HTTP 301 Moved Permanentaly to the
			// correct identifier with slash
			throw new RedirectionException(Status.MOVED_PERMANENTLY, URI.create(uri.toString() + "/"));

		}

		catch (ProcessingNotSupportedException e) {

			// TODO Align with specified LDP behaviour
			throw new BadRequestException("Processing not supported by this resource");

		}

		catch (ParseException e) {

			// TODO Align with specified LDP behaviour
			throw new BadRequestException("Could not parse the RDF representation");

		}

		catch (IOException e) {

			// TODO Align with specified LDP behaviour
			throw new InternalServerErrorException("Could not create the resource");

		}

	}

	@DELETE
	public Response deleteResource(@PathParam("path") String path) {

		// Log the request
		logger.info("deleteResource(String path) > path={}", path);

		// Get the request URI
		URI uri = uriInfo.getRequestUri();

		// Log the request URI
		logger.info("uri.getPath() > {}", uriInfo.getRequestUri().getPath());

		// Get the resource for the path
		StorageResource resource = storage.getResource(path);

		// If the resource exists
		if (resource != null) {

			// If the resource is a container resource
			if (resource.isContainerResource()) {

				// If the resource identifier does not end with a slash
				if (!uri.getPath().endsWith("/")) {

					// Response with HTTP 301 Moved Permanentaly to the
					// correct identifier with slash
					throw new RedirectionException(Status.MOVED_PERMANENTLY, URI.create(uri.toString() + "/"));

				}

			}

			// Else if the resource is not a container resource
			else {

				// If the resource identifier ends with a slash
				if (uri.getPath().endsWith("/")) {

					// Response with HTTP 301 Moved Permanentaly to the
					// correct identifier without a slash
					throw new RedirectionException(Status.MOVED_PERMANENTLY,
							URI.create(uri.toString().substring(0, uri.toString().length() - 1)));

				}

			}

			// Delete the resource for the path
			storage.deleteResource(path);

			// Response with HTTP 200
			return Response.ok().build();

		}

		// Else if the resource does not exists
		else {

			// Response with HTTP 404
			throw new NotFoundException();

		}

	}

}