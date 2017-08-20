package org.echo.taotao.common.util;

import org.apache.commons.configuration2.CompositeConfiguration;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @Author Administrator
 * @Date 8/17/2017
 * @Description
 * @Version
 */
public class ConfigurationTest {
    @Test
    public void getCompositeConfiguration() throws Exception {
        String key = "project.name";
        String value = "taotao";
        System.setProperty(key, value);
        CompositeConfiguration configuration = Configuration.getCompositeConfiguration();
        assertEquals("LGP", configuration.getString("name"));
        assertEquals(value, configuration.getProperty(key));
        System.clearProperty(key);
    }

}