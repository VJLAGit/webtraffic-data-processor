package com.dollarshave.de.webtraffic.processor;


import org.apache.commons.cli.CommandLine;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.*;
//import org.apache.spark.sql.hive.HiveContext;
import org.apache.spark.sql.expressions.WindowSpec;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.spark.sql.functions.*;
import org.apache.spark.sql.expressions.Window;






/**
 * Created by Vijay on 1/28/17.
 * Process web traffic logs and creates the linked list to connect to next event
 */
public class WebTrafficBatchProcessor {

    public static final Logger LOGGER = LoggerFactory.getLogger(WebTrafficBatchProcessor.class);

    private SparkConf sparkConf;
    private JavaSparkContext sparkContext;
    private CommandLine cmd;

    private SparkSession session;

    /**
     * initi method
     */
    private void init(){

        if(cmd.getOptionValue("mode").equals("local")){
            session = SparkSession
                    .builder().master("local").appName("Webtraffic data processor").config("spark.hadoop.validateOutputSpecs", "false").getOrCreate();
        }else{
            session = SparkSession
                    .builder().appName("Webtraffic data processor").getOrCreate();
        }


    }



    public WebTrafficBatchProcessor(CommandLine cmd){
        this.cmd = cmd;
        init();

    }

    /**
     *
     * @return
     */
    public boolean process(){

      String inputPath = cmd.getOptionValue("input-path");

        String outputPath = cmd.getOptionValue("output-path");
        LOGGER.info("inputPath is {}", inputPath);
        LOGGER.info("outputPath is {}", outputPath);

        //create structType for input csv data
        //SQLContext sqlContext = new SQLContext(sparkContext);
        StructType customSchema = new StructType(new StructField[]{
                new StructField("event_id", DataTypes.StringType, true, Metadata.empty()),
                new StructField("collector_tstamp", DataTypes.StringType, true, Metadata.empty()),
                new StructField("domain_userid", DataTypes.StringType, true, Metadata.empty()),
                new StructField("page_urlpath", DataTypes.StringType, true, Metadata.empty())

        });
        //read input csv content
        Dataset df = session.read()
                .format("com.databricks.spark.csv")
                .schema(customSchema)
                .option("inferSchema", "true")
                .option("header", "false")
                .load(inputPath);
        //df.coalesce(sparkContext.defaultMinPartitions());
        df.coalesce(session.sparkContext().defaultMinPartitions());
        df.printSchema();


        //create windows fuctions
        WindowSpec wSpec = Window.partitionBy("domain_userid").orderBy("collector_tstamp");

        //add column next_event_id, by using the windows spec, lead by 1 (next event for the user)
        Dataset modifiedDF = df.withColumn("next_event_id" , lead(df.col("event_id"), 1).over(wSpec));

        modifiedDF.printSchema();


        modifiedDF.coalesce(session.sparkContext().defaultMinPartitions()).write()
                .mode(SaveMode.Overwrite)
                .format("com.databricks.spark.csv")
                .option("header", "false")
                .save(outputPath);

      if(session != null)
          session.stop();
      return true;
    }


}
