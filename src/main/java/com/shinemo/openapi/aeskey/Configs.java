package com.shinemo.openapi.aeskey;

import com.shinemo.openapi.client.common.OpenApiException;
import com.shinemo.openapi.client.common.OpenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.Properties;

public final class Configs {

    private static Properties prop = new Properties();

    private static final Logger LOG = LoggerFactory.getLogger(Configs.class);

    static {
        loadPropertiesFile("/app.properties");
    }

    private static void loadPropertiesFile(String file) {
        InputStream stream = Configs.class.getResourceAsStream(file);
        try {
            prop.load(stream);
        } catch (Exception e) {
            LOG.error("Failed to load config from " + file + ", cause:" + e.getMessage(), e);
        } finally {
            OpenUtils.silentClose(stream);
        }
    }


    public static String getString(String key) {
        return prop.getProperty(key);
    }

    public static int getInt(String key) {
        String value = prop.getProperty(key);
        if (value == null) throw new OpenApiException("没有有找到配置项," + key);
        return Integer.parseInt(value);
    }
}
