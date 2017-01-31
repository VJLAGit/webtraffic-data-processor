package com.dollarshave.de.webtraffic.main;

import com.dollarshave.de.webtraffic.cliparser.WebTrafficCliParser;
import com.dollarshave.de.webtraffic.processor.WebTrafficBatchProcessor;
import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Vijay on 1/27/17.
 * Main class to kickstart batch processing
 */
public class Starter {

    public static final Logger LOGGER = LoggerFactory.getLogger(Starter.class);

    public static void main(String[] args){

        WebTrafficCliParser parser = new WebTrafficCliParser();

        CommandLine cmd = parser.parseCommandLine(args);

        WebTrafficBatchProcessor job = new WebTrafficBatchProcessor(cmd);
        job.process();

    }
}

