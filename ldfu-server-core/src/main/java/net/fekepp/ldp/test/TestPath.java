package net.fekepp.ldp.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.semanticweb.yars.nx.parser.ParseException;

import edu.kit.aifb.datafu.io.mediatypes.MediaTypes;

public class TestPath {

	public static void main(String[] args)
			throws IOException, URISyntaxException, InterruptedException, ParseException {

		String directoryIdentifier = "example-application-02";
		String fileIdentifier = "example-application-01-run.nt";

		Path rootPathRelative = Paths.get("../doc/../doc/example");
		Path rootPathRelativeNormalized = rootPathRelative.normalize();
		Path rootPathAbsolute = rootPathRelative.toAbsolutePath();
		Path rootPathAbsoluteNormalized = rootPathAbsolute.normalize();

		System.out.println("rootPathRelative=" + rootPathRelative);
		System.out.println("rootPathRelativeNormalized=" + rootPathRelativeNormalized);
		System.out.println("rootPathAbsolute=" + rootPathAbsolute);
		System.out.println("rootPathAbsoluteNormalized=" + rootPathAbsoluteNormalized);
		System.out.println();

		Path directoryPathRelative = rootPathRelative.resolve(directoryIdentifier);
		Path directoryPathRelativeNormalized = directoryPathRelative.normalize();
		Path directoryPathAbsolute = directoryPathRelative.toAbsolutePath();
		Path directoryPathAbsoluteNormalized = directoryPathAbsolute.normalize();
		System.out.println("directoryPathRelative=" + directoryPathRelative);
		System.out.println("directoryPathRelativeNormalized=" + directoryPathRelativeNormalized);
		System.out.println("directoryPathAbsolute=" + directoryPathAbsolute);
		System.out.println("directoryPathAbsoluteNormalized=" + directoryPathAbsoluteNormalized);
		System.out.println();

		Path filePathRelative = rootPathRelative.resolve(fileIdentifier);
		Path filePathRelativeNormalized = filePathRelative.normalize();
		Path filePathAbsolute = filePathRelative.toAbsolutePath();
		Path filePathAbsoluteNormalized = filePathAbsolute.normalize();
		System.out.println("filePathRelative=" + filePathRelative);
		System.out.println("filePathRelativeNormalized=" + filePathRelativeNormalized);
		System.out.println("filePathAbsolute=" + filePathAbsolute);
		System.out.println("filePathAbsoluteNormalized=" + filePathAbsoluteNormalized);
		System.out.println();

		System.out.println("Files.isDirectory(directoryPathRelativeNormalized)="
				+ Files.isDirectory(directoryPathRelativeNormalized));
		System.out.println("Files.isDirectory(directoryPathAbsoluteNormalized)="
				+ Files.isDirectory(directoryPathAbsoluteNormalized));
		System.out.println(
				"Files.isRegularFile(filePathRelativeNormalized)=" + Files.isRegularFile(filePathRelativeNormalized));
		System.out.println(
				"Files.isRegularFile(filePathAbsoluteNormalized)=" + Files.isRegularFile(filePathAbsoluteNormalized));

		// String test = MediaType.APPLICATION_JSON;
		edu.kit.aifb.datafu.io.mediatypes.MediaType notation3MediaType = MediaTypes.Notation3;
		for (String fileExtension : notation3MediaType.getFileExtensions()) {
			System.out.println("notation3MediaType.getFileExtensions() > " + fileExtension);
		}
		for (String name : notation3MediaType.getNames()) {
			System.out.println("notation3MediaType.getNames() > " + name);
		}

	}

}
