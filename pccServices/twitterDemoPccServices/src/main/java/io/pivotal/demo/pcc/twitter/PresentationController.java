package io.pivotal.demo.pcc.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PresentationController
{

	@Autowired
	Environment env;
	
	
	@RequestMapping("/")
    public String index(Model model) 
    {
    	/*
    	 * SENTIMENT_COMPUTE_URL}';
        var sentimentStatsURL = '${SENTIMENT_STATS_URL}';
    	 */
        model.addAttribute("SENTIMENT_COMPUTE_URL", env.getProperty("SENTIMENT_COMPUTE_UR", ""));
        model.addAttribute("SENTIMENT_STATS_URL", env.getProperty("SENTIMENT_STATS_URL", ""));
        return "main";
    }
    
}
