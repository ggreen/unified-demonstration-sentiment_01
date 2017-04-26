package io.pivotal.demo.pcc.twitter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@RestController
@EnableAutoConfiguration
@ComponentScan()
//@PropertySource("classpath:application.yml")
//@PropertySource("application.yml")
//@ImportResource(locations = {"classpath:cache-config.xml"})
@ConfigurationProperties
@EnableWebMvc
//@EnableGemfireRepositories(basePackageClasses= {RecipientDAORepository.class})
@Configuration
public class TwitterDemoPccServicesApplication {

	 @RequestMapping("/")
	    @ResponseBody
	public String home() {
	        return "Hello World!";
	    }
	 
	public static void main(String[] args) {
		SpringApplication.run(TwitterDemoPccServicesApplication.class, args);
	}
}
