package net.fekepp.ldp.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputStreamProcessingTemplate {

	private static final Logger logger = LoggerFactory.getLogger(InputStreamProcessingTemplate.class);

	public static void process(String fileName, InputStreamProcessor processor) {
		IOException processException = null;
		InputStream input = null;
		try {
			input = new FileInputStream(fileName);
			processor.process(input);
		} catch (IOException e) {
			processException = e;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					if (processException != null) {
						// throw new
						logger.error("Error while processing and closing input stream for file {} | {} | {}", fileName,
								processException, e);
					} else {
						// throw new
						logger.error("Error closing input stream for file {} | {} ", fileName, e);
					}
				}
			}
			if (processException != null) {
				// throw new
				logger.error("Error processing InputStream for file {} | {} ", fileName, processException);
			}
		}
	}
}
