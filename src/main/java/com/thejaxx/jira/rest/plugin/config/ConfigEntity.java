package com.thejaxx.jira.rest.plugin.config;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigEntity implements Serializable {

    public static final String _KEY = ConfigEntity.class.getName();
    @XmlElement
    private String changeLogViewPattern;
    @XmlElement
    private String url;
    @XmlElement
    private String username;
    @XmlElement
    private String password;
    @XmlElement
    private String renderingEditPattern;
    @XmlElement
    private String editPattern;
    @XmlElement
    private String viewPattern;
    @XmlElement
    private String jsonKey;
    private String jsonValue;
    @XmlElement
    private String statisticsViewPattern;
    @XmlElement
    private String renderingSearchPattern;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getChangeLogViewPattern() {
        return changeLogViewPattern;
    }

    public void setChangeLogViewPattern(String changeLogViewPattern) {
        this.changeLogViewPattern = changeLogViewPattern;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRenderingEditPattern() {
        return renderingEditPattern;
    }

    public void setRenderingEditPattern(String renderingEditPattern) {
        this.renderingEditPattern = renderingEditPattern;
    }

    public String getEditPattern() {
        return editPattern;
    }

    public void setEditPattern(String editPattern) {
        this.editPattern = editPattern;
    }

    public String getViewPattern() {
        return viewPattern;
    }

    public void setViewPattern(String viewPattern) {
        this.viewPattern = viewPattern;
    }

    public String getJsonKey() {
        return jsonKey;
    }

    public void setJsonKey(String jsonKey) {
        this.jsonKey = jsonKey;
    }

    public String getJsonValue() {
        return jsonValue;
    }

    public void setJsonValue(String jsonValue) {
        this.jsonValue = jsonValue;
    }

    public String getStatisticsViewPattern() {
        return statisticsViewPattern;
    }

    public void setStatisticsViewPattern(String statisticsViewPattern) {
        this.statisticsViewPattern = statisticsViewPattern;
    }

    public String getRenderingSearchPattern() {
        return renderingSearchPattern;
    }

    public void setRenderingSearchPattern(String renderingSearchPattern) {
        this.renderingSearchPattern = renderingSearchPattern;
    }

}
