package net.fekepp.ldfu.server.webapi;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Command line interface for the LD-Fu Web API.
 * 
 * @author "Felix Leif Keppmann"
 */
public class CommandLine {

	public static final Option OPTION_PORT = Option.builder("p").longOpt("port").desc("port to be used by the server")
			.hasArg().type(Number.class).required().build();

	public static final Options getOptions() {
		Options options = new Options();
		options.addOption(OPTION_PORT);
		return options;
	}

	public static void main(String[] args) {

		// Create a default command line parser
		CommandLineParser parser = new DefaultParser();

		// Create a command line to be parsed
		org.apache.commons.cli.CommandLine commandLine = null;

		// Create a help formatter used in case of trouble
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.setLeftPadding(4);

		// Try to parse options and start the controller
		try {

			// Parse the command line options
			commandLine = parser.parse(getOptions(), args);

			// Create a Web API controller
			ServerController controller = new ServerController();

			// Configure port based required port option
			Object portOptionParsedValue = commandLine.getParsedOptionValue(OPTION_PORT.getOpt());
			if (portOptionParsedValue instanceof Long && (Long) portOptionParsedValue > 0
					&& (Long) portOptionParsedValue <= 65535) {
				controller.setPort(((Long) portOptionParsedValue).intValue());
			} else {
				throw new ParseException("Bad input string: " + portOptionParsedValue);
			}

			// Start the Web API controller
			controller.startBlocking();

		}

		// Handle parse exceptions thrown in case of wrong options
		catch (ParseException e) {

			// Print the failure message generated during parsing
			System.out.println(e.getMessage().replace("For input string", "Bad input string") + "\n");

			// Print helpful information about options
			helpFormatter.printHelp("ldfu-server", getOptions());

			// Return with exit code != 0 to indicate that something went wrong
			System.exit(1);

		}

	}

}