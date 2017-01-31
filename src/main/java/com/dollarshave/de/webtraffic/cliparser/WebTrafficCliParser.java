package com.dollarshave.de.webtraffic.cliparser;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.cli.ParseException;

/**
 * Created by vijay on 1/28/17.
 * parses the cli arguments
 */
public class WebTrafficCliParser {

    public static final Logger LOGGER = LoggerFactory.getLogger(WebTrafficCliParser.class);

    private final Options options = new Options();

    private final HelpFormatter formatter = new HelpFormatter();

    private CommandLine cmd;

    public WebTrafficCliParser(){
        defineOptions();

    }

    private void defineOptions(){
        options.addOption("ip", "input-path", true, "input path for csv file");
        options.addOption("op", "output-path", true, "output path for csv file");
        options.addOption("m", "mode", true, "local or master");
        options.addOption("?", "help", false, "help message");
    }

    private String[] requiredOptions() {
        return new String[] {"input-path", "output-path"};
    }

    private void printHelp(Options options) {
        formatter.printHelp(this.getClass().getName(), options);

    }

    public CommandLine parseCommandLine(String... args) {
        CommandLineParser parser = new GnuParser();

        try {

            cmd = parser.parse(options, args);
            LOGGER.info(cmd.toString());

            if (cmd.hasOption('?')) {
                printHelp(options);
                System.exit(0);
            }

            for (String optionName : ArrayUtils.addAll(requiredOptions())) {
                if (!cmd.hasOption(optionName)) {
                    formatter.printHelp("optionName", options);
                    System.exit(1);
                }
            }


        } catch (ParseException e) {
            printHelp(options);
            LOGGER.error("Unexpected Exception: {}", e.getMessage());
            System.exit(1);
        }
        return cmd;
    }


}
