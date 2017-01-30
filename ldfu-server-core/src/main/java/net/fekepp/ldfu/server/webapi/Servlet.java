package net.fekepp.ldfu.server.webapi;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.namespace.LDP;
import org.semanticweb.yars.nx.namespace.RDF;

/**
 * @author "Felix Leif Keppmann"
 */
@Path("/")
public class Servlet {

	private static ServerController controller;

	public static ServerController getController() {
		return controller;
	}

	public static void setController(ServerController controller) {
		Servlet.controller = controller;
	}

	private final Logger logger = Logger.getLogger(getClass().getName());

	@Context
	UriInfo uriInfo;

	@GET
	public Response getInstanceContainer() {

		// Get the request URI
		URI uri = Helper.getContainerUriOrRedirect(uriInfo);

		// Create the representation to be returned
		Set<Node[]> representation = new HashSet<Node[]>();

		// Create a base URI for the requested resource
		Resource identifierResource = new Resource(uri.toString());

		// Let the resource be a LDP Container
		representation.add(new Node[] { identifierResource, RDF.TYPE, LDP.CONTAINER });

		// Let the resource be a LDP Basic Container
		representation.add(new Node[] { identifierResource, RDF.TYPE, LDP.BASIC_CONTAINER });

		// Log
		logger.log(Level.INFO, "GET > " + uri);

		// Return the representation with HTTP 200 OK
		return Response.ok(new GenericEntity<Iterable<Node[]>>(representation) {
		}).build();

	}

}