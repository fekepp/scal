package net.fekepp.ldp.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.RedirectionException;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.aifb.datafu.Program;
import edu.kit.aifb.datafu.Query;
import edu.kit.aifb.datafu.io.origins.InputOrigin;
import edu.kit.aifb.datafu.io.stream.CountingInputStream;
import edu.kit.aifb.datafu.parser.ProgramConsumerImpl;
import edu.kit.aifb.datafu.parser.QueryConsumerImpl;
import edu.kit.aifb.datafu.parser.notation3.Notation3Parser;
import edu.kit.aifb.datafu.parser.sparql.SparqlParser;

/**
 * @author "Felix Leif Keppmann"
 */
public class Helper {

	private static final Logger logger = LoggerFactory.getLogger(Helper.class.getName());

	public static URI getContainerUriOrRedirect(UriInfo uriInfo) {

		// Get the request URI
		URI uri = uriInfo.getRequestUri();

		// Redirect with HTTP 301 Moved Permanently to the container resource
		// URI with a trailing slash, if the request URI has no trailing slash
		if (!uri.getPath().endsWith("/")) {
			throw new RedirectionException(Status.MOVED_PERMANENTLY, URI.create(uri.toString() + "/"));
		}

		// Return the request URI
		return uri;

	}

	public static URI getResourceUriOrRedirect(UriInfo uriInfo) {

		// Get the request URI
		URI uri = uriInfo.getRequestUri();

		// Redirect with HTTP 301 Moved Permanently to the resource URI without
		// a trailing slash, if the request URI has a trailing slash
		if (uri.getPath().endsWith("/")) {
			throw new RedirectionException(Status.MOVED_PERMANENTLY,
					URI.create(uri.toString().substring(0, uri.toString().length() - 1)));
		}

		// Return the request URI
		return uri;

	}

	public static Program parseProgramOrReturnBadRequest(InputOrigin origin, InputStream inputStream) {

		// Create the program to be returned
		Program program;

		// Parse program from input stream
		try {
			program = parseProgram(origin, inputStream);
		}

		// Response with HTTP 400 Bad Request if an IO exception occurs during
		// parsing of program
		catch (IOException e) {
			logger.warn("Input stream handling failed", e);
			throw new BadRequestException("Input stream handling failed", e);
		}

		// Response with HTTP 400 Bad Request if a parse exception occurs during
		// parsing of program
		catch (edu.kit.aifb.datafu.parser.notation3.ParseException e) {
			logger.warn("Program parsing failed", e);
			throw new BadRequestException("Program parsing failed", e);
		}

		// Response with HTTP 400 Bad Request if program was not found
		if (program == null) {
			logger.warn("Program not found");
			throw new BadRequestException("Program not found");
		}

		// Return the parsed program
		return program;

	}

	public static Set<Query> parseQueriesOrReturnBadRequest(InputOrigin origin, InputStream inputStream) {

		// Create a set of queries to be returned
		Set<Query> queries = new HashSet<Query>();

		// Create a query consumer for queries to be returned
		QueryConsumerImpl queryConsumer = new QueryConsumerImpl(origin);

		// Try to parse queries from input stream
		try {

			try {
				SparqlParser parser = new SparqlParser(inputStream);
				parser.parse(queryConsumer, origin);
			}

			// Response with HTTP 400 Bad Request if a parse exception occurs
			// during parsing of program
			catch (edu.kit.aifb.datafu.parser.sparql.ParseException e) {
				logger.warn("Query parsing failed", e);
				throw new BadRequestException("Query parsing failed", e);
			}

			// Close the input stream
			finally {
				inputStream.close();
			}

		}

		// Response with HTTP 400 Bad Request if an IO exception occurs during
		// parsing of program
		catch (IOException e) {
			logger.warn("Input stream handling failed", e);
			throw new BadRequestException("Input stream handling failed", e);
		}

		// Response with HTTP 400 Bad Request if no queries have been found
		if (queryConsumer.getConstructQueries().size() == 0 && queryConsumer.getSelectQueries().size() == 0) {
			logger.warn("No queries found");
			throw new BadRequestException("No queries found");
		}

		// Response with HTTP 400 Bad Request if select and construct queries
		// have been found
		if (queryConsumer.getSelectQueries().size() > 0 && queryConsumer.getConstructQueries().size() > 0) {
			logger.warn("Either one select query or construct queries allowed");
			throw new BadRequestException("Either one select query or construct queries allowed");
		}

		// Response with HTTP 400 Bad Request if more than one select query has
		// been found
		if (queryConsumer.getSelectQueries().size() > 1) {
			logger.warn("Only one select query allowed");
			throw new BadRequestException("Only one select query allowed");
		}

		// Add possible contained select query to returned queries
		queries.addAll(queryConsumer.getSelectQueries());

		// Add possible contained construct queries to returned queries
		queries.addAll(queryConsumer.getConstructQueries());

		// Return the construct queries
		return queries;

	}

	public static Program parseProgram(InputOrigin origin, InputStream inputStream)
			throws IOException, edu.kit.aifb.datafu.parser.notation3.ParseException {

		CountingInputStream countingInputStream = new CountingInputStream(inputStream);

		ProgramConsumerImpl programConsumer = new ProgramConsumerImpl(origin);

		origin.beginRequest(Thread.currentThread().getName());

		try {
			Notation3Parser parser = new Notation3Parser(countingInputStream);
			parser.parse(programConsumer, origin);
		}

		finally {
			origin.endRequest();
			origin.setTriples(programConsumer.getFacts().size());
			origin.setBytes(countingInputStream.getBytesRead());
			inputStream.close();
		}

		return programConsumer.getProgram(origin);

	}

	public static void logUri(URI uri) {
		logger.info("uri.toString() > {} ", uri.toString());
		logger.info("scheme={} | user={} | host={} | port={} | path={} | query={} | fragment={}", uri.getScheme(),
				uri.getUserInfo(), uri.getHost(), uri.getPort(), uri.getPath(), uri.getQuery(), uri.getFragment());
		// logger.info("uri.getAuthority() > {} ", uri.getAuthority());
		// logger.info("uri.getFragment() > {} ", uri.getFragment());
		// logger.info("uri.getHost() > {} ", uri.getHost());
		// logger.info("uri.getQuery() > {} ", uri.getQuery());
		// logger.info("uri.getPath() > {} ", uri.getPath());
		// logger.info("uri.getPort() > {} ", uri.getPort());
		// logger.info("uri.getScheme() > {} ", uri.getScheme());
		// logger.info("uri.getUserInfo() > {} ", uri.getUserInfo());
	}

}