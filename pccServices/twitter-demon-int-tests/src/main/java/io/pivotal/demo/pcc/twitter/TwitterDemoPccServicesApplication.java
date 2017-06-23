package io.pivotal.demo.pcc.twitter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableAutoConfiguration
//@ComponentScan()
//@PropertySource("classpath:application.yml")
//@ImportResource(locations = {"classpath:cache-config.xml"})
//@ConfigurationProperties
//@EnableWebMvc
//@EnableGemfireRepositories(basePackageClasses= {RecipientDAORepository.class})
@SpringBootApplication
public class TwitterDemoPccServicesApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(TwitterDemoPccServicesApplication.class, args);
	}
}
