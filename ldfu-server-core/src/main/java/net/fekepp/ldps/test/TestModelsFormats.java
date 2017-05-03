package net.fekepp.ldps.test;

import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fekepp.ldps.Format;
import net.fekepp.ldps.Model;
import net.fekepp.ldps.model.RdfModel;

public class TestModelsFormats {

	private static final Logger logger = LoggerFactory.getLogger(TestModelsFormats.class.getName());

	public static void main(String[] args) {

		Model rdfFormatGroupSingleton = RdfModel.getInstance();
		logger.info("formatGroupName={}", rdfFormatGroupSingleton.getName());
		for (Entry<String, Format> mediaTypeMapEntry : rdfFormatGroupSingleton.getMediaTypesMap().entrySet()) {
			logger.info("\tmediaType={}", mediaTypeMapEntry.getKey());
			logger.info("\t\tformatName={}", mediaTypeMapEntry.getValue().getName());
			for (String fileExtension : mediaTypeMapEntry.getValue().getFileExtensions()) {
				logger.info("\t\tfileExtension={}", fileExtension);
			}
		}

		// TurtleFormat turtleFormat = new TurtleFormat();

		// logger.info("TurtleFormat.NAME > {}", TurtleFormat.NAME);
		// logger.info("TurtleFormat.DEFAULT_MEDIA_TYPE > {}",
		// TurtleFormat.DEFAULT_MEDIA_TYPE);
		// logger.info("TurtleFormat.DEFAULT_FILE_EXTENSION > {}",
		// TurtleFormat.DEFAULT_FILE_EXTENSION);
		//
		// for (String mediaType : TurtleFormat.MEDIA_TYPES) {
		// logger.info("TurtleFormat.MEDIA_TYPES > {}", mediaType);
		// }
		//
		// for (String fileExtension : TurtleFormat.FILE_EXTENSIONS) {
		// logger.info("TurtleFormat.FILE_EXTENSIONS > {}", fileExtension);
		// }

		// logger.info("RdfFormatGroup.NAME > {}", RdfFormatGroup.NAME);

	}

}