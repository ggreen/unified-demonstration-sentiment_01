package io.pivotal.demo.pcc.twitter.data;

/**
 * data: {\"tweetRate\":\""+r.nextInt(23)+"\", \"avgPolarity\": \""+r.nextInt(5)+"\"}
 * @author Gregory Green
 *
 */
public class TweetStats
{	
	/**
	 * @return the avgPolarity
	 */
	public int getAvgPolarity()
	{
		return avgPolarity;
	}
	/**
	 * @return the tweetRate
	 */
	public int getTweetRate()
	{
		return tweetRate;
	}
	/**
	 * @param avgPolarity the avgPolarity to set
	 */
	public void setAvgPolarity(int avgPolarity)
	{
		this.avgPolarity = avgPolarity;
	}
	/**
	 * @param tweetRate the tweetRate to set
	 */
	public void setTweetRate(int tweetRate)
	{
		this.tweetRate = tweetRate;
	}
	private int avgPolarity;
	private int tweetRate;
}
