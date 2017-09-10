package org.echo.taotao.common.util;

import org.apache.commons.configuration.Configuration;
import org.echo.taotao.common.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Administrator on 8/17/2017
 * @version
 */
public class ConfigurationTest extends BaseTest {

    @Autowired
    private Configuration configuration;


    @Test
    public void getConfiguration() throws Exception {
        String key = "project.name";
        String value = "taotao";
        System.setProperty(key, value);
//        Configuration configuration = DefaultConfiguration.getConfiguration();
        assertNull(configuration.getString("name"));
        assertEquals(value, configuration.getProperty(key));
        System.clearProperty(key);
        assertEquals("lgp", configuration.getString("ftp.username"));
    }

}