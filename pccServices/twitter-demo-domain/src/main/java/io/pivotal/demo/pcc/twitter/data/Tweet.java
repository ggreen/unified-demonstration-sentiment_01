package io.pivotal.demo.pcc.twitter.data;

/**
 * "tweet": "#3RMXi v0.1.2 had just been released!\nThe Language component State is decoupled from the UI now!\nhttps://t.co/y3D4tIurAf\n#react #javascript", "polarity": "0.67"
 * @author Gregory Green
 *
 */
public class Tweet
{	
	/**
	 * @return the polarity
	 */
	public double getPolarity()
	{
		return polarity;
	}
	/**
	 * @return the tweet
	 */
	public String getTweet()
	{
		return tweet;
	}
	/**
	 * @param polarity the polarity to set
	 */
	public void setPolarity(double polarity)
	{
		this.polarity = polarity;
	}
	/**
	 * @param tweet the tweet to set
	 */
	public void setTweet(String tweet)
	{
		this.tweet = tweet;
	}
	private double polarity;
	private String tweet;

}
