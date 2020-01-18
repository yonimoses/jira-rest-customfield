package com.thejaxx.jira.rest.plugin.config;

import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;

public class ConfigUtils {

    private static final Logger logger = Logger.getLogger(ConfigUtils.class);

    public static final String PLUGIN_KEY = "REST_CF_PLUGIN";
    public static PluginSettingsFactory _factory;
    public static PluginSettings _settings;
    public static Gson _gson = new GsonBuilder().setPrettyPrinting().create();

    private static ConfigEntity defaultEntity() {
        ConfigEntity _default = new ConfigEntity();
        _default.setRenderingEditPattern("{2}, {1}");
        _default.setChangeLogViewPattern("{2}, {1}");
        _default.setJsonValue("value");
        _default.setJsonKey("key");
        _default.setEditPattern("{2}, {1}");
        _default.setViewPattern("{2}, {1}");
        _default.setStatisticsViewPattern("{1} {2}");
        _default.setUrl("countries.json");
        return _default;
    }


    public static ConfigEntity save(ConfigEntity entity) {
        _settings.put(ConfigEntity._KEY, _gson.toJson(entity));
        logger.info("Plugin Config :: Key  - " + ConfigUtils.PLUGIN_KEY + ", Value - \r\n" + _gson.toJson(get()));
        return entity;
    }

    public static ConfigEntity get() {

        return _gson.fromJson(_settings.get(ConfigEntity._KEY).toString(), ConfigEntity.class);
    }

    public static void init(PluginSettingsFactory _factory) {
        logger.info("Initiating Config");
        ConfigUtils._factory = _factory;
        ConfigUtils._settings = ConfigUtils._factory.createSettingsForKey(ConfigUtils.PLUGIN_KEY);
        if (_settings.get(ConfigEntity._KEY) == null) {
            save(defaultEntity());
        }
        logger.info("Plugin Config :: Key  - " + ConfigUtils.PLUGIN_KEY + ", Value - \r\n" + _gson.toJson(get()));
    }
}