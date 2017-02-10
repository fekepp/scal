package net.fekepp.ldfu.server.storage.filesystem;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Nodes;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.namespace.LDP;
import org.semanticweb.yars.nx.namespace.RDF;

import net.fekepp.ldfu.server.mediatype.Format;
import net.fekepp.ldfu.server.mediatype.RdfFormatGroup;
import net.fekepp.ldfu.server.storage.StorageResource;

public class FilesystemStorageContainerResource implements StorageResource {

	private String identifier;

	private Path path;

	public FilesystemStorageContainerResource(String identifier, Path path) {
		this.identifier = identifier;
		this.path = path;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public Format getFormat() {
		return RdfFormatGroup.getInstance().getDefaultFormat();
	}

	@Override
	public InputStream getData() {

		// Create the representation to be returned
		Set<Node[]> representation = new HashSet<Node[]>();

		// Create a base URI for the requested resource
		Resource identifierResource = new Resource(identifier);

		// Let the resource be a LDP Container
		representation.add(new Node[] { identifierResource, RDF.TYPE, LDP.CONTAINER });

		// Let the resource be a LDP Basic Container
		representation.add(new Node[] { identifierResource, RDF.TYPE, LDP.BASIC_CONTAINER });

		// Add containment triples for all contained resources
		// Java7
		for (final File file : path.toFile().listFiles()) {
			String name = file.getName();
			int lastDotIndex = name.lastIndexOf(".");
			if (lastDotIndex >= 0) {
				name = name.substring(0, lastDotIndex);
				if (Files.exists(path.resolve(name))) {
					continue;
				}
			}
			representation.add(new Node[] { identifierResource, LDP.CONTAINS,
					new Resource(name + (file.isDirectory() ? "/" : "")) });
		}

		// Java8
		// try (Stream<Path> paths = Files.list(path)) {
		// paths.forEach(path -> {
		// representation.add(new Node[] { identifierResource, LDP.CONTAINS,
		// new Resource(identifier + path.getFileName() +
		// (Files.isDirectory(path) ? "/" : "")) });
		// });
		// }
		//
		// catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// TODO Add triples from RDF file that contains additional triple

		StringBuffer stringBuffer = new StringBuffer();
		for (Node[] node : representation) {
			stringBuffer.append(Nodes.toString(node));
			stringBuffer.append(System.getProperty("line.separator"));
		}

		return new ByteArrayInputStream(stringBuffer.toString().getBytes());

	}

	@Override
	public boolean isRdfResource() {
		return true;
	}

	@Override
	public boolean isContainerResource() {
		return true;
	}

}
