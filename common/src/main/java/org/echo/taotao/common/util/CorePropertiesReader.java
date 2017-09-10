package org.echo.taotao.common.util;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.event.ConfigurationEvent;
import org.apache.commons.configuration.event.ConfigurationListener;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * @author Administrator on 9/9/2017.
 */
public class CorePropertiesReader {

    private static final Logger logger = LoggerFactory.getLogger(CorePropertiesReader.class);

    private static final String CORE_PROPERTY_PATH = "META-INF/core.properties";

    private static final String STAR = "*";
    private static final String COMMA = "*";
    private static final String OVERRIDE_FILE_PREFIX = "META-INF/overrides";
    private static final String OVERRIDE_FILE_NAME = "overrides.properties";
    private static final String FILE_SEPARATOR = "/";
    private static final String OVERRIDE_PROPERTY_PARAM = "core.overrides.file";
    private static final String SYS_PARAM_ARTIFACTS_LIST = "artifacts.list";
    private static final String PROTOCOL_FILE = "file";
    private static final String PROTOCOL_JAR = "jar";
    private static final String ARTIFACT_JAR_ENDING_SUFFIX = ".jar";
    private static final String SLASH_STRING = "/";
    private static final String PARAM_COMMON_ARTIFACT_VERSION = "common.artifact.version";
    private static final String DEFAULT_POM_VERSION = "1.0-SNAPSHOT";

    private static final Map<String, Properties> PROPERTIES_MAPS = initPropertiesByEnv();
    private static final Set<String> CORE_PROPERTIES_ARTIFACTS_SET = getValidArtifactsList();
    private static final Set<String> COMMON_POM_VERSION_SET = getCommonPomVersionSet();


    private CorePropertiesReader() {
    }

    public static PropertiesConfiguration findConfiguration() {
        PropertiesConfiguration propertiesConfiguration = findAndCheckConfig(CORE_PROPERTY_PATH);
        return propertiesConfiguration;
    }

    private static PropertiesConfiguration findAndCheckConfig(String resourceName) {
        Set<UrlPropertiesConfiguration> propertiesSet = getPropertiesSet(resourceName);
        PropertiesConfiguration propertiesConfiguration = mergeProperties(propertiesSet);
        validateUnResolvedPlaceHolder(propertiesConfiguration);
        return propertiesConfiguration;

    }

    private static void validateUnResolvedPlaceHolder(PropertiesConfiguration propertiesConfiguration) {
        Iterator<String> iterator = propertiesConfiguration.getKeys();
        Set<String> unresolvedKeys = Sets.newHashSet();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String valueForKey = getValueForKey(key, propertiesConfiguration);
            if (valueForKey != null && valueForKey.indexOf("${") != -1) {
                unresolvedKeys.add(key);
            }
        }

        if (CollectionUtils.isNotEmpty(unresolvedKeys)) {
            throw new RuntimeException("have unresolved keys.");
        }
    }

    private static String getValueForKey(String key, PropertiesConfiguration propertiesConfiguration) {
        String[] values = propertiesConfiguration.getStringArray(key);
        boolean allowDuplicateProperty = false;
        if (allowDuplicateProperty) {
            Set<String> valueSet = Sets.newHashSet();
            return Joiner.on(COMMA).join(valueSet);
        } else {
            return Joiner.on(COMMA).join(values);
        }
    }

    private static PropertiesConfiguration mergeProperties(Set<UrlPropertiesConfiguration> propertiesSet) {
        UrlPropertiesConfiguration urlPropertiesConfiguration = new UrlPropertiesConfiguration();
        urlPropertiesConfiguration.addConfigurationListener(getConfigurationListener());
        UrlConfigurationListener urlConfigurationListener = new UrlConfigurationListener();
        urlPropertiesConfiguration.addConfigurationListener(urlConfigurationListener);

        for (UrlPropertiesConfiguration propertiesConfiguration : propertiesSet) {
            urlConfigurationListener.appendedConfiguration = propertiesConfiguration;
            urlPropertiesConfiguration.append(propertiesConfiguration);
        }
        return urlPropertiesConfiguration;
    }

    private static class UrlConfigurationListener implements ConfigurationListener {
        private UrlPropertiesConfiguration appendedConfiguration = null;

        @Override
        public void configurationChanged(ConfigurationEvent configurationEvent) {
            if (!configurationEvent.isBeforeUpdate()) {
                ((UrlPropertiesConfiguration) configurationEvent.getSource()).getUrlPropertiesConfiguration()
                        .setProperty(configurationEvent.getPropertyName(), appendedConfiguration);

                String valueUrl = (String) appendedConfiguration.getValueUrlPropertiesConfiguration()
                        .getProperty(configurationEvent.getPropertyName());
                ((UrlPropertiesConfiguration) configurationEvent.getSource()).getValueUrlPropertiesConfiguration()
                        .setProperty(configurationEvent.getPropertyName(), valueUrl);
            }
        }
    }

    private static Set<UrlPropertiesConfiguration> getPropertiesSet(String resourceName) {
        Enumeration<URL> urls = getUrls(resourceName); // get all property files from class loader.
        Set<UrlPropertiesConfiguration> propSets = Sets.newHashSet();
        Map<String, Set<String>> existingPlaceHolderMapByUrl = Maps.newHashMap();
        try {
            StringBuilder stringBuilder = new StringBuilder("load properties from: ");
            while (urls.hasMoreElements()) {
                UrlPropertiesConfiguration urlPropertiesConfiguration = new UrlPropertiesConfiguration();
                URL url = urls.nextElement();
                urlPropertiesConfiguration.addConfigurationListener(getConfigurationListener());
                ResolvedValueUrlConfigurationListener resolvedValueUrlConfigurationListener = new ResolvedValueUrlConfigurationListener();
                resolvedValueUrlConfigurationListener.setResolvedValueUrl(url.toString());
                urlPropertiesConfiguration.addConfigurationListener(resolvedValueUrlConfigurationListener);
                if (CollectionUtils.isNotEmpty(CORE_PROPERTIES_ARTIFACTS_SET)) { // always is null
                    if (!isValidPathAgainstArtifacts(url)) {
                        continue;
                    }
                }
                stringBuilder.append("\n\t").append(url);
                Set<String> existingPlaceHolderSet = existingPlaceHolderMapByUrl.get(url.toString());
                if (CollectionUtils.isEmpty(existingPlaceHolderSet)) {
                    existingPlaceHolderSet = Sets.newHashSet();
                    existingPlaceHolderMapByUrl.put(url.toString(), existingPlaceHolderSet);
                }

                URLConnection con = url.openConnection();
                ResourceUtils.useCachesIfNecessary(con);
                InputStream is = con.getInputStream();
                loadCurrentUrl(urlPropertiesConfiguration, url, is);
                urlPropertiesConfiguration.clearConfigurationListeners();
                propSets.add(urlPropertiesConfiguration);
            }
//            Set<String> allPlaceHoderNames = getAllPlaceHoders(existingPlaceHolderMapByUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return propSets;

    }

    /*private static Set<String> getAllPlaceHoders(Map<String, Set<String>> existingPlaceHolderMapByUrl) {
        Set<String> placeholders = Sets.newHashSet();
        Collection<Set<String>> existingPlaceHoders = existingPlaceHolderMapByUrl.values();
        for (Set<String> existingPlaceHoder : existingPlaceHoders) {
            placeholders.addAll(existingPlaceHoder);
        }
        return placeholders;
    }*/

    private static void loadCurrentUrl(UrlPropertiesConfiguration urlPropertiesConfiguration, URL url, InputStream is) {
        try {
            urlPropertiesConfiguration.load(is);
            urlPropertiesConfiguration.setURL(url);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean isValidPathAgainstArtifacts(URL url) {
        String protocol = url.getProtocol();
        String path = url.getPath();
        if (StringUtils.equals(PROTOCOL_FILE, protocol)) {
            return true;
        } else if (StringUtils.equals(PROTOCOL_JAR, protocol)) {
            String artifactId = getArtifactIdFromJarPath(path);
            return isValidArtifactId(artifactId);
        }
        return false;
    }

    private static boolean isValidArtifactId(String artifactId) {
        if (CollectionUtils.isNotEmpty(CORE_PROPERTIES_ARTIFACTS_SET)) {
            return CORE_PROPERTIES_ARTIFACTS_SET.contains(artifactId);
        } else {
            return true;
        }
    }

    private static String getArtifactIdFromJarPath(String path) {
        String artifactId = StringUtils.EMPTY;
        for (String pomVersion : COMMON_POM_VERSION_SET) {
            int endingJarSuffix = path.indexOf(ARTIFACT_JAR_ENDING_SUFFIX);
            int startingSlashIdx = path.lastIndexOf(SLASH_STRING, endingJarSuffix);
            String artifactIdContinsPomVersion = path.substring(startingSlashIdx + 1, endingJarSuffix);

            int pomVersionIdx = artifactIdContinsPomVersion.indexOf(pomVersion);
            if (pomVersionIdx != -1 && (pomVersionIdx - 1) > 0) {
                return artifactIdContinsPomVersion.substring(0, pomVersionIdx - 1);
            }
        }
        return artifactId;
    }

    private static ConfigurationListener getConfigurationListener() {
        return new ConfigurationListener() {
            private Set<String> propKeys = Sets.newHashSet();

            @Override
            public void configurationChanged(ConfigurationEvent configurationEvent) {
                if (!configurationEvent.isBeforeUpdate()) {
                    String propertyName = configurationEvent.getPropertyName();
                    if (propKeys.contains(propertyName)) {
                        boolean allowDuplicateProperty = false;
                        if (allowDuplicateProperty) {
                            propKeys.remove(propertyName);
                        } else {
                            throw new RuntimeException("duplicated key define for " + propertyName);
                        }
                    }
                    propKeys.add(propertyName);
                }
            }
        };
    }


    private static Map<String, Properties> initPropertiesByEnv() {
        String filesFromCommand = System.getProperty(OVERRIDE_PROPERTY_PARAM, null);
        if (StringUtils.isNotBlank(filesFromCommand)) {
            String[] split = StringUtils.split(filesFromCommand, COMMA);
            return loadExternalProperties(split);
        } else {
            String[] resourceNames = getEnvSpecificFileName();
            return loadInternalProperties(resourceNames);
        }
    }

    private static Map<String, Properties> loadInternalProperties(String[] resourceNames) {
        Map<String, Properties> map = Maps.newLinkedHashMap();
        if (ArrayUtils.isNotEmpty(resourceNames)) {
            List<URL> urlToLoad = Lists.newArrayList();
            for (String name : resourceNames) {
                List<URL> urlForResource = getAllUrlForResource(name);
                urlToLoad.addAll(urlForResource);
            }

            List<URL> sortedUrlByPackage = sortUrlByPackage(urlToLoad);
            for (URL url : sortedUrlByPackage) {
                Properties property = loadPropertiesFileFromUrl(url);
                map.put(url.toString(), property);
            }
        }

        return map;
    }

    private static Properties loadPropertiesFileFromUrl(URL url) {
        Properties prop = new Properties();
        try {
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();
            loadProperties(prop, is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }

    private static void loadProperties(Properties prop, InputStream is) {
        try {
            prop.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static List<URL> sortUrlByPackage(List<URL> urlToLoad) {
        Map<String, List<URL>> urlPackageMap = Maps.newLinkedHashMap();
        if (CollectionUtils.isNotEmpty(urlToLoad)) {
            for (URL url : urlToLoad) {
                String location = url.toString().substring(0, url.toString().lastIndexOf(OVERRIDE_FILE_PREFIX));
                List<URL> urlListByPackage = urlPackageMap.get(location);
                if (CollectionUtils.isEmpty(urlListByPackage)) {
                    urlListByPackage = Lists.newArrayList();
                    urlPackageMap.put(location, urlListByPackage);
                }
                urlListByPackage.add(url);
            }
        }
        List<URL> sortByPackage = Lists.newArrayList();
        Collection<List<URL>> urls = urlPackageMap.values();
        for (List<URL> url : urls) {
            sortByPackage.addAll(url);
        }
        return sortByPackage;
    }

    private static List<URL> getAllUrlForResource(String resourceName) {
        List<URL> urlLocations = Lists.newArrayList();
        if (StringUtils.isNotBlank(resourceName)) {
            Enumeration<URL> urls = getUrls(resourceName);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                urlLocations.add(url);
            }
        }
        return urlLocations;
    }

    private static Enumeration<URL> getUrls(String resourceName) {
        ClassLoader clToUse = ClassUtils.getDefaultClassLoader();
        Enumeration<URL> urls = null;
        try {
            urls = clToUse.getResources(resourceName);
            logger.info(String.format("It will load resource %s with urls %s"), resourceName, urls.toString());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return urls;
    }


    private static String[] getEnvSpecificFileName() {
        List<String> sourceNames = Lists.newArrayList();
        // if there is more than one environment, enable below.
        /*String lifeCycle = "uat";
        String cluster = "primary";
        String instance = "crcr";

        boolean isInstanceSet = !StringUtils.equals(instance, STAR);
        boolean isClusterSet = StringUtils.isNotBlank(cluster);
        boolean isLefeCycleSet = StringUtils.isNotBlank(lifeCycle);

        if (isLefeCycleSet && isClusterSet && isInstanceSet) {
            String joinName = Joiner.on(FILE_SEPARATOR).join(OVERRIDE_FILE_PREFIX, lifeCycle, cluster, instance, OVERRIDE_FILE_NAME);
            sourceNames.add(joinName);
        }
        if (isLefeCycleSet && isClusterSet) {
            String joinName = Joiner.on(FILE_SEPARATOR).join(OVERRIDE_FILE_PREFIX, lifeCycle, cluster, OVERRIDE_FILE_NAME);
            sourceNames.add(joinName);
        }
        if (isLefeCycleSet ) {
            String joinName = Joiner.on(FILE_SEPARATOR).join(OVERRIDE_FILE_PREFIX, lifeCycle, OVERRIDE_FILE_NAME);
            sourceNames.add(joinName);
        }*/
        sourceNames.add(Joiner.on(FILE_SEPARATOR).join(OVERRIDE_FILE_PREFIX, OVERRIDE_FILE_NAME));

        return sourceNames.toArray(new String[sourceNames.size()]);
    }

    private static Map<String, Properties> loadExternalProperties(String[] resourceNames) {
        Map<String, Properties> map = Maps.newLinkedHashMap();
        if (ArrayUtils.isNotEmpty(resourceNames)) {
            for (String name : resourceNames) {
                map.putAll(loadPropertiesFileByFile(name));
            }
        }
        return map;
    }

    private static Map<? extends String, ? extends Properties> loadPropertiesFileByFile(String name) {
        Map<String, Properties> map = Maps.newLinkedHashMap();
        InputStream is = null;
        try {
            is = loadPropertiesResource(name, map);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return map;
    }

    private static InputStream loadPropertiesResource(String resourceName, Map<String, Properties> map) throws FileNotFoundException {
        InputStream is;
        Properties prop = new Properties();
        is = new FileInputStream(resourceName);
        try {
            prop.load(is);
            map.put(resourceName, prop);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return is;
    }

    private static Set<String> getValidArtifactsList() {
        Set<String> artifactsSet = Sets.newLinkedHashSet();
        String artLists = System.getProperty(SYS_PARAM_ARTIFACTS_LIST);
        if (StringUtils.isNotBlank(artLists)) {
            artifactsSet = splitAndTrimToSet(artLists);
        }
        if (CollectionUtils.isEmpty(artifactsSet)) {
            Set<Map.Entry<String, Properties>> entrySet = PROPERTIES_MAPS.entrySet();
            for (Map.Entry<String, Properties> entry : entrySet) {
                Properties properties = entry.getValue();
                if (properties.containsKey(SYS_PARAM_ARTIFACTS_LIST)) {
                    String PropFromOverrides = properties.getProperty(SYS_PARAM_ARTIFACTS_LIST);
                    artifactsSet = splitAndTrimToSet(PropFromOverrides);
                    if (CollectionUtils.isNotEmpty(artifactsSet)) break;
                }
            }
        }
        return artifactsSet;
    }

    private static Set<String> splitAndTrimToSet(String tokens) {
        Set<String> rtnSet = Sets.newLinkedHashSet();
        if (StringUtils.isNotBlank(tokens)) {
            String[] tokenArray = StringUtils.splitPreserveAllTokens(tokens, COMMA);
            if (ArrayUtils.isNotEmpty(tokenArray)) {
                for (String aTokenArray : tokenArray) {
                    String trimedToken = StringUtils.trimToNull(aTokenArray);
                    if (StringUtils.isNotBlank(trimedToken)) {
                        rtnSet.add(trimedToken);
                    }
                }
            }
        }
        return rtnSet;
    }

    private static Set<String> getCommonPomVersionSet() {
        Set<String> pomVersion = Sets.newHashSet();
        String pomVersionFromCommand = System.getProperty(PARAM_COMMON_ARTIFACT_VERSION, null);
        if (StringUtils.isNotBlank(pomVersionFromCommand)) {
            Set<String> pomVersionFromSystem = splitAndTrimToSet(pomVersionFromCommand);
            if (CollectionUtils.isNotEmpty(pomVersionFromSystem)) {
                pomVersion.addAll(pomVersionFromSystem);
            }
        }

        Set<Map.Entry<String, Properties>> entrySet = PROPERTIES_MAPS.entrySet();
        for (Map.Entry<String, Properties> entry : entrySet) {
            Properties properties = entry.getValue();
            if (properties.containsKey(PARAM_COMMON_ARTIFACT_VERSION)) {
                String propFromOverrides = properties.getProperty(PARAM_COMMON_ARTIFACT_VERSION);
                Set<String> artifactsSet = splitAndTrimToSet(propFromOverrides);
                if (CollectionUtils.isNotEmpty(artifactsSet)) {
                    pomVersion.addAll(artifactsSet);
                    break;
                }
            }
        }
        pomVersion.add(DEFAULT_POM_VERSION);
        return pomVersion;
    }


}

class ResolvedValueUrlConfigurationListener implements ConfigurationListener {
    private String resolvedValueUrl = null;

    @Override
    public void configurationChanged(ConfigurationEvent configurationEvent) {
        if (!configurationEvent.isBeforeUpdate()) {
            ((UrlPropertiesConfiguration) configurationEvent.getSource())
                    .getValueUrlPropertiesConfiguration().setProperty(
                    configurationEvent.getPropertyName(), resolvedValueUrl
            );
        }
    }

    public void setResolvedValueUrl(String resolvedValueUrl) {
        this.resolvedValueUrl = resolvedValueUrl;
    }
}


class UrlPropertiesConfiguration extends PropertiesConfiguration {
    private PropertiesConfiguration urlPropertiesConfiguration = new PropertiesConfiguration();
    private PropertiesConfiguration valueUrlPropertiesConfiguration = new PropertiesConfiguration();

    public PropertiesConfiguration getUrlPropertiesConfiguration() {
        return urlPropertiesConfiguration;
    }

    public PropertiesConfiguration getValueUrlPropertiesConfiguration() {
        return valueUrlPropertiesConfiguration;
    }
}