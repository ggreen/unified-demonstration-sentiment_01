package io.pivotal.demo.pcc.twitter;

import java.util.Properties;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.EvictionAttributes;
import org.apache.geode.cache.ExpirationAttributes;
import org.apache.geode.cache.PartitionAttributes;
import org.apache.geode.cache.RegionAttributes;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.CacheFactoryBean;
import org.springframework.data.gemfire.PartitionAttributesFactoryBean;
import org.springframework.data.gemfire.PartitionedRegionFactoryBean;
import org.springframework.data.gemfire.RegionAttributesFactoryBean;
import org.springframework.data.gemfire.eviction.EvictionActionType;
import org.springframework.data.gemfire.eviction.EvictionAttributesFactoryBean;
import org.springframework.data.gemfire.eviction.EvictionPolicyType;
import org.springframework.data.gemfire.expiration.ExpirationActionType;
import org.springframework.data.gemfire.expiration.ExpirationAttributesFactoryBean;
import io.pivotal.spring.cloud.service.gemfire.GemfireServiceConnectorConfig;

@Configuration
public class DemoConfig
{
	 
	@Bean
	public Properties placeholderProperties() {
		Properties placeholders = new Properties();

		placeholders.setProperty("app.gemfire.region.eviction.action", "LOCAL_DESTROY");
		placeholders.setProperty("app.gemfire.region.eviction.policy-type", "MEMORY_SIZE");
		placeholders.setProperty("app.gemfire.region.eviction.threshold", "4096");
		placeholders.setProperty("app.gemfire.region.expiration.entry.tti.action", "INVALIDATE");
		placeholders.setProperty("app.gemfire.region.expiration.entry.tti.timeout", "300");
		placeholders.setProperty("app.gemfire.region.expiration.entry.ttl.action", "DESTROY");
		placeholders.setProperty("app.gemfire.region.expiration.entry.ttl.timeout", "600");
		placeholders.setProperty("app.gemfire.region.partition.local-max-memory", "16384");
		placeholders.setProperty("app.gemfire.region.partition.redundant-copies", "1");
		placeholders.setProperty("app.gemfire.region.partition.total-max-memory", "32768");

		return placeholders;
	}

	@Bean
	public PropertyPlaceholderConfigurer propertyPlaceholderConfigurer(
			@Qualifier("placeholderProperties") Properties placeholders) {
		PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
		propertyPlaceholderConfigurer.setProperties(placeholders);
		return propertyPlaceholderConfigurer;
	}

	@Bean
	public Properties gemfireProperties() {
		Properties gemfireProperties = new Properties();

		gemfireProperties.setProperty("name", "PccDemo");
		gemfireProperties.setProperty("mcast-port", "0");
		gemfireProperties.setProperty("log-level", "config");

		return gemfireProperties;
	}

	@Bean
	@Autowired
	public CacheFactoryBean gemfireCache(@Qualifier("gemfireProperties") Properties gemfireProperties) throws Exception {
		CacheFactoryBean cacheFactory = new CacheFactoryBean();
		cacheFactory.setProperties(gemfireProperties);
		return cacheFactory;
	}//------------------------------------------------
	@Bean(name = "n_tweets")
	@Autowired
	public PartitionedRegionFactoryBean<Object, Object> n_tweets(Cache gemfireCache,
			@Qualifier("partitionRegionAttributes") RegionAttributes<Object, Object> regionAttributes) throws Exception {

		PartitionedRegionFactoryBean<Object, Object> examplePartitionRegion =
			new PartitionedRegionFactoryBean<Object, Object>();

		examplePartitionRegion.setAttributes(regionAttributes);
		examplePartitionRegion.setCache(gemfireCache);
		examplePartitionRegion.setName("n_tweets");
		examplePartitionRegion.setPersistent(false);

		return examplePartitionRegion;
	}//------------------------------------------------
	@SuppressWarnings("deprecation")
	@Bean
	@Autowired
	public RegionAttributesFactoryBean partitionRegionAttributes(PartitionAttributes<?,?> partitionAttributes,
			EvictionAttributes evictionAttributes,
			@Qualifier("entryTtiExpirationAttributes") ExpirationAttributes entryTti,
			@Qualifier("entryTtlExpirationAttributes") ExpirationAttributes entryTtl) {

		RegionAttributesFactoryBean regionAttributes = new RegionAttributesFactoryBean();

		regionAttributes.setEvictionAttributes(evictionAttributes);
		regionAttributes.setEntryIdleTimeout(entryTti);
		regionAttributes.setEntryTimeToLive(entryTtl);
		regionAttributes.setPartitionAttributes(partitionAttributes);

		return regionAttributes;
	}//------------------------------------------------

	@Bean
	public EvictionAttributesFactoryBean defaultEvictionAttributes(
			@Value("${app.gemfire.region.eviction.action}") String action,
			@Value("${app.gemfire.region.eviction.policy-type}") String policyType,
			@Value("${app.gemfire.region.eviction.threshold}") int threshold) {

		EvictionAttributesFactoryBean evictionAttributes = new EvictionAttributesFactoryBean();

		evictionAttributes.setAction(EvictionActionType.valueOfIgnoreCase(action).getEvictionAction());
		evictionAttributes.setThreshold(threshold);
		evictionAttributes.setType(EvictionPolicyType.valueOfIgnoreCase(policyType));

		return evictionAttributes;
	}//------------------------------------------------

	@Bean
	public ExpirationAttributesFactoryBean entryTtiExpirationAttributes(
			@Value("${app.gemfire.region.expiration.entry.tti.action}") String action,
			@Value("${app.gemfire.region.expiration.entry.tti.timeout}") int timeout) {

		ExpirationAttributesFactoryBean expirationAttributes = new ExpirationAttributesFactoryBean();

		expirationAttributes.setAction(ExpirationActionType.valueOfIgnoreCase(action).getExpirationAction());
		expirationAttributes.setTimeout(timeout);

		return expirationAttributes;
	}

	@Bean
	public ExpirationAttributesFactoryBean entryTtlExpirationAttributes(
			@Value("${app.gemfire.region.expiration.entry.ttl.action}") String action,
			@Value("${app.gemfire.region.expiration.entry.ttl.timeout}") int timeout) {

		ExpirationAttributesFactoryBean expirationAttributes = new ExpirationAttributesFactoryBean();

		expirationAttributes.setAction(ExpirationActionType.valueOfIgnoreCase(action).getExpirationAction());
		expirationAttributes.setTimeout(timeout);

		return expirationAttributes;
	}

	@Bean
	public PartitionAttributesFactoryBean<?,?> defaultPartitionAttributes(
			@Value("${app.gemfire.region.partition.local-max-memory}") int localMaxMemory,
			@Value("${app.gemfire.region.partition.redundant-copies}") int redundantCopies,
			@Value("${app.gemfire.region.partition.total-max-memory}") int totalMaxMemory) {

		PartitionAttributesFactoryBean<?,?>  partitionAttributes = new PartitionAttributesFactoryBean<Object,Object>();

		partitionAttributes.setLocalMaxMemory(localMaxMemory);
		partitionAttributes.setRedundantCopies(redundantCopies);
		partitionAttributes.setTotalMaxMemory(totalMaxMemory);

		return partitionAttributes;
	}
	
	public ServiceConnectorConfig createGemfireConnectorConfig() {

        GemfireServiceConnectorConfig gemfireConfig = new GemfireServiceConnectorConfig();
        gemfireConfig.setPoolSubscriptionEnabled(true);
        gemfireConfig.setPdxSerializer(new ReflectionBasedAutoSerializer(".*"));
        gemfireConfig.setPdxReadSerialized(false);
        
        return gemfireConfig;
	}

	@Bean(name = "gemfireCache")
    public ClientCache getGemfireClientCache() throws Exception {		
		
		Cloud cloud = new CloudFactory().getCloud();
		ClientCache clientCache = cloud.getSingletonServiceConnector(ClientCache.class,  createGemfireConnectorConfig());

        return clientCache;
    }
}
