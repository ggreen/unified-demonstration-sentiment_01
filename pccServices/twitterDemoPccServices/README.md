# Twitter Demo PCC Services

This web application is a Data Science based demonstration for Twitter Feeds.
It uses Pivotal Cloud Cache to store and aggregate tweets.
It calculates tweets per second using technologies such
as  PCC/Apache Geode continuous queries and HTTP Server Side Events.


Java version of the web UI application has been deployed to PWS.


[http://twitterdemopccservices.cfapps.pez.pivotal.io/](http://twitterdemopccservices.cfapps.pez.pivotal.io/)

The current version uses stubbed Java based Spring Boot based web 
services to simulate twitter feeds, popularity, rates, etc.
Also, the application has PCC/Gemfire enabled in embedded mode.

Once access to a PCC service is available, we can be integrate the connection to 
a centralized PCC based cluster.

Still need to work on the module to write twitter feeds data to PCC, that the web 
UI application would read/analysis.

## Building

	cd unified-demonstration-sentiment_01/pccServices/twitterDemoPccServices
	mvn clean package

##PWS Deployment 

	 cf login --sso 
	 
### Push application
	 
	cf push -p target/twitterDemoPccServices-0.0.1-SNAPSHOT.jar


# PCC

Create service key 

	cf create-service p-cloudcache extra-small unifed-pcc
	cf create-service-key unifed-pcc unifed-pcc-key
	cf service-key unifed-pcc unifed-pcc-key
	
	cf push -p target/gedi-geode-extensions-rest-0.0.1-SNAPSHOT.jar
	
	cf set-env gedi-geode-extensions-rest LOCATOR_HOST 192.168.40.87
	cf set-env gedi-geode-extensions-rest LOCATOR_PORT 55221
	cf set-env gedi-geode-extensions-rest name unified-gedi-geode-extensions-rest
	
	cf bind-service gedi-geode-extensions-rest unifed-pcc
	cf restage gedi-geode-extensions-rest
	
	
	cf bind-service twitterDemoPccServices  unifed-pcc


##Initialize Gfsh

	cf service-key unifed-pcc unifed-pcc-key
	
	gfsh
	
	gfsh>connect --use-http --url=http://gemfire-f0850d0a-bfe3-4165-9cfb-586127874250.run.pez.pivotal.io/gemfire/v1 --user=cluster_operator
	password: *********************



 
create region --name=tweets --type=PARTITION
create region --name=tweet_rates --type=REPLICATE
