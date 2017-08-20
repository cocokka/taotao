package org.echo.taotao.common.util;

import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.SystemConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author Administrator
 * @Date 8/17/2017
 * @Description
 * @Version
 */
public class Configuration {

    private static final Logger LOGGER = LoggerFactory.getLogger(Configuration.class);

    private Configuration() {
    }

    private static final String DEFAULT_CONFIGURATION_FILE_PATH = "META-INF/core.properties";

    // TODO there should be get from cache first.
    public static CompositeConfiguration getCompositeConfiguration() {
        CompositeConfiguration compositeConfiguration = new CompositeConfiguration();
        compositeConfiguration.addConfiguration(new SystemConfiguration());
        org.apache.commons.configuration2.Configuration configuration = getConfiguration();
        if (configuration != null) {
            compositeConfiguration.addConfiguration(getConfiguration());
        }
        return compositeConfiguration;
    }

    private static org.apache.commons.configuration2.Configuration getConfiguration() {
        Parameters parameters = new Parameters();
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder;
        builder = new FileBasedConfigurationBuilder(PropertiesConfiguration.class)
                .configure(parameters.properties().setFileName(DEFAULT_CONFIGURATION_FILE_PATH));
        try {
            return builder.getConfiguration();
        } catch (ConfigurationException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }
}
