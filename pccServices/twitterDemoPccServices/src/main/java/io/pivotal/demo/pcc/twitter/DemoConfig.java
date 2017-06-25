package io.pivotal.demo.pcc.twitter;

import java.util.Queue;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.pdx.PdxInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import gedi.solutions.geode.client.GeodeClient;

@Configuration
public class DemoConfig
{
	@Bean
	GeodeClient getGeodeClient()
	{

		return GeodeClient.connect();
	}//------------------------------------------------
	@Bean(name = "gemfireCache")
    public ClientCache getGemfireClientCache(@Autowired GeodeClient geodeClient) throws Exception {		
		
		 return geodeClient.getClientCache();
    }//------------------------------------------------
	@Bean(name = "tweets")
	public Region<String,PdxInstance> getTweets(@Autowired GeodeClient geodeClient)
	{
		return geodeClient.getRegion("tweets");
	}//------------------------------------------------
	@Bean(name = "tweet_rates")
	public Region<String,PdxInstance> getTweetRates(@Autowired GeodeClient geodeClient)
	{
		return geodeClient.getRegion("tweet_rates");
	}//------------------------------------------------	
	
	@Bean(name = "liveTweetsQueue")
	public Queue<PdxInstance> getTweetQueue(@Autowired GeodeClient geodeClient)
	{
		return geodeClient.registerCq("liveTweets", "select * from /tweets");
	}//------------------------------------------------
	
}
