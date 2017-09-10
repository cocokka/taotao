package org.echo.taotao.common.util;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

/**
 * @author Administrator on 8/17/2017
 * @version
 */
@org.springframework.context.annotation.Configuration
@Scope("singleton")
public class DefaultConfiguration {

    @Bean("configuration")
    public Configuration getApacheConfig() {
        return ApacheConfigurationHolder.apacheConfig;
    }

    public static Configuration getConfiguration() {
        return ApacheConfigurationHolder.apacheConfig;
    }

    private static class ApacheConfigurationHolder {
        private ApacheConfigurationHolder() {
        }

        private static Configuration apacheConfig = new CoreConfiguration().getConfiguration();
    }

    private static class CoreConfiguration {
        public Configuration getConfiguration() {
            CompositeConfiguration configuration = new CompositeConfiguration();
            configuration.addConfiguration(new SystemConfiguration());
            PropertiesConfiguration propertiesConfiguration = CorePropertiesReader.findConfiguration();
            if (propertiesConfiguration != null) {
                configuration.addConfiguration(propertiesConfiguration);
            }
            return configuration;
        }
    }
}
