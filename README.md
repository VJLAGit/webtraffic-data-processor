# webtraffic-data-processor
>

## Synopsis:
+ Transform the dataset of individual page view events into a collection of linkedlists, that represent the journeys that each user tookthrough the website.   

## Languages/Technology/Framework
+ Gradle
+ Java 8 
+ Spark 2.0.1
+ Docker.

## Install Dependencies: 
+ Java 8
+ Gradle
+ Docker

## Initialize application/Build Docker Image: 
+ Install Java 8, docker and Gradle
+ Navigate to webtraffic-data-processor. 
+ Execute  
    > gradle clean build shadowJar buildDocker . 
    
+ Builds shadowJar and builds a docker image with spark 2.0.1, java 8 and webtrafficjar . 
+ image tag is "com.dollarshave.de.webtraffic/webtraffic:1.0-SNAPSHOT"

## Steps to Execute Job
1. docker run --rm -it -p 4040:4040 -v $(host_csv_input_dir):/opt/input/  com.dollarshave.de.webtraffic/webtraffic:1.0-SNAPSHOT  bash
2. bin/spark-submit  /opt/webtraffic/webtraffic-data-processor.jar -m master -ip /opt/input/ -op /opt/output/
3. Navigate /opt/output to validate data.

## Steps to execute pre existing/uploaded image
+. Image uploaded to docker.io as "arumv001/webtraffic" .   
+. docker pull arumv001/webtraffic   
+. Execute the same steps(Steps to Execute Job) with the change in image .   
   
