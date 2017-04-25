package io.pivotal.demo.pcc.twitter;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping(value = "/approvalReads", produces = MediaType.APPLICATION_JSON_VALUE)
@Component
public class TweetRateService
{
	 @RequestMapping("/tweet_rate")
	    @ResponseBody
	public String tweetRate() {
	        return "1000";
	    }
}
