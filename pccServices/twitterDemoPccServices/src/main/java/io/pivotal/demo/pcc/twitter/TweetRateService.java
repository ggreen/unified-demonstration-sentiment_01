package io.pivotal.demo.pcc.twitter;

import java.io.IOException;
import java.util.Queue;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.pdx.JSONFormatter;
import org.apache.geode.pdx.PdxInstance;
import org.json.JSONException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO: centralize Access-Control-Allow-Origin 
 * @author Gregory Green
 *
 */
@RestController
public class TweetRateService
{
	//TODO: calculate this data from PCC
	//private Random r = new Random(System.currentTimeMillis());

	
	@CrossOrigin	
	@RequestMapping(value="/tweet_rate")
	@ResponseBody
	public void tweet_rate(String time_interval, HttpServletResponse response)
	throws IOException
	{
	
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/event-stream");
		response.setHeader("Cache-Control","no-cache");
		response.setCharacterEncoding("UTF-8");
		
		//response.getWriter().println("data: {\"tweetRate\":\""+tweetRate+"\", \"avgPolarity\": \""+avgPolarity+"\"}\r\n");
		
		PdxInstance pdx = this.tweet_rates.get("tweet_rates");
		
		for(int i =0; i< 20 ; i++)
	    {
			
			response.getWriter().println("data: {\"tweetRate\":\""+pdx.getField("tweetRate")+"\", \"avgPolarity\": \""+pdx.getField("avgPolarity")+"\"}\r\n");
	    }
		
		//for(int i =0; i< 20 ; i++)
		//{
			
			//response.getWriter().println("data: {\"tweetRate\":\""+r.nextInt(23)+"\", \"avgPolarity\": \""+r.nextInt(5)+"\"}\r\n");			
		//}
	}//------------------------------------------------

	@CrossOrigin
	@RequestMapping(value="/num_tweets")
	@ResponseBody
	public String num_tweets(HttpServletResponse respone)
	{
		//TODO: calculate this data from PCC
		//Random r = new Random(System.currentTimeMillis());
		respone.setHeader("Access-Control-Allow-Origin", "*");
		
		return String.valueOf(this.liveTweetsQueue.size());
	}//------------------------------------------------
	
	/**
	 *  Example Data
	 *   	
	 * data: {"tweet": "RT @maggieNYT: The whole \"nationalist vs New York wing\" fight was smart branding for ppl trying to rally troops.. Not accurate, but clever\u2026", "polarity": "0.87"}

			data: {"tweet": "RT @edko426: @4AllSoulKind @03Ava @cgm807 @zackwack123 @Barbarajean117 @WalkerkillR @nfraizi @GTBighair1 @dynamex @MiceeMouse\u2026 ", "polarity": "0.98"}
			
			data: {"tweet": "RT @ChelseaFC: Superb header by Gary Cahill to put us back in front, tremendous bravery by the skipper and wonderful execution. #CHESOU", "polarity": "0.87"}
			
			data: {"tweet": "RT @BNightengale: The #Marlins sale of course won't be finalized until #MLB approval. The next owners meeting is next month in New York", "polarity": "0.56"}
			
			data: {"tweet": "#3RMXi v0.1.2 had just been released!\nThe Language component State is decoupled from the UI now!\nhttps://t.co/y3D4tIurAf\n#react #javascript", "polarity": "0.67"}

	 * @param response
	 * @throws IOException
	 */
	@CrossOrigin
	@RequestMapping(value="/live_tweets")
	@ResponseBody
	public void live_tweets(HttpServletResponse response)
	throws IOException,  JSONException
	{
		//TODO: calculate this data from PCC
		//Ex: "{"tweet": "Congratulations!!@Jeannettehyde https://t.co/vOkcxwmBft", "polarity": "0.95"}"
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/event-stream");
		response.setHeader("Cache-Control","no-cache");
		response.setCharacterEncoding("UTF-8");
				
		PdxInstance tweetPdx = null;
		
		int tweetRate = liveTweetsQueue.size();
		double avgPolarity = 0;
		int cnt = 0;
		
		double polarity = 0;
		while((tweetPdx = liveTweetsQueue.poll()) != null)
		{
			polarity = Double.parseDouble(String.valueOf(tweetPdx.getField("polarity")));
			response.getWriter().println(String.format("data: {\"tweet\": \"%s\", \"polarity\": \"%s\"}\r\n", tweetPdx.getField("tweet"), polarity));
			
			avgPolarity += polarity;
			cnt++;
			
		}
		
		if(cnt >  0)
		{
			avgPolarity = (avgPolarity)/(cnt*1.0);		
		}
		

		String jsonString = String.format("{\"tweetRate\":\"%s\" , \"avgPolarity\": \"%s\"}",tweetRate,avgPolarity);
	    this.tweet_rates.put("tweet_rates",JSONFormatter.fromJSON(jsonString));	
		
	}//------------------------------------------------
	/*@CrossOrigin
	@RequestMapping(value="/gen_tweet_stats",headers ={"Access-Control-Allow-Origin","*"})
	@ResponseBody
	public String gen_tweet_stats(String key, String value)
	{

		Object old = n_tweets.put(key, value);

		return String.valueOf(old);
	}*/

	@Resource
	private ClientCache gemfireCache;

	@Resource
	private Region<Object, Object> tweets;
	
	@Resource
	private Region<String, PdxInstance> tweet_rates;
	
	@Resource 
	private Queue<PdxInstance> liveTweetsQueue;
}
