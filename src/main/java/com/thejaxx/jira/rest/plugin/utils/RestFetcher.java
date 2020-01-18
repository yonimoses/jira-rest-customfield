package com.thejaxx.jira.rest.plugin.utils;

import com.atlassian.jira.util.json.JSONArray;
import com.atlassian.jira.util.json.JSONObject;
import com.thejaxx.jira.rest.plugin.RestRow;
import com.thejaxx.jira.rest.plugin.config.ConfigEntity;
import com.thejaxx.jira.rest.plugin.config.ConfigUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import com.atlassian.core.util.ClassLoaderUtils;

public final class RestFetcher {

    private static final Logger logger = Logger.getLogger(ConfigUtils.class);

    private static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static String loadLocal(String name) throws IOException {

        InputStream stream = ClassLoaderUtils.getResourceAsStream(name, RestFetcher.class);
        if (stream == null) {
            URL resource = ClassLoaderUtils.getResource(name, RestFetcher.class);
            if (resource != null) {
                return FileCopyUtils.copyToString(new InputStreamReader(resource.openStream()));
            }
        } else {
            return FileCopyUtils.copyToString(new InputStreamReader(stream));
        }
        return "[]";
    }

    public static List<RestRow> doQuery(String projectKey) {
        ConfigEntity entity = ConfigUtils.get();
        logger.info("doQuery in progress for project  " + projectKey + ", with URL " + entity.getUrl());
        ;
        List<RestRow> result = new LinkedList<RestRow>();

        try {
            JSONArray array;
            if (entity.getUrl().startsWith("http")) {
                array = new JSONArray(HttpRequest.get(entity.getUrl()).body());
            } else {
                array = new JSONArray(loadLocal(entity.getUrl()));
            }
            logger.debug("Got array of " + array.length());
            if (array.length() > 0)
                logger.debug("First element is " + array.get(0).toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = (JSONObject) array.get(i);
                /**
                 * for now, we support only keys
                 */
                result.add(new RestRow(jsonObject.getString(entity.getJsonKey()), jsonObject.getString(entity.getJsonKey())));
            }
        } catch (Throwable e) {
            logger.error("doQuery is in exception  " + e.getMessage(), e);
        }
        return result;
    }

}
