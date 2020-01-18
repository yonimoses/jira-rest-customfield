package com.thejaxx.jira.rest.plugin;

import com.atlassian.jira.bc.issue.search.QueryContextConverter;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.customfields.searchers.ExactTextSearcher;
import com.atlassian.jira.issue.customfields.searchers.transformer.CustomFieldInputHelper;
import com.atlassian.jira.issue.customfields.statistics.AbstractCustomFieldStatisticsMapper;
import com.atlassian.jira.issue.customfields.statistics.CustomFieldStattable;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.search.ClauseNames;
import com.atlassian.jira.issue.search.LuceneFieldSorter;
import com.atlassian.jira.issue.search.searchers.renderer.SearchRenderer;
import com.atlassian.jira.issue.search.searchers.transformer.SearchInputTransformer;
import com.atlassian.jira.issue.statistics.StatisticsMapper;
import com.atlassian.jira.issue.statistics.TextFieldSorter;
import com.atlassian.jira.jql.operand.JqlOperandResolver;
import com.atlassian.jira.jql.util.JqlSelectOptionsUtil;
import com.atlassian.jira.web.FieldVisibilityManager;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.apache.log4j.Logger;

import java.util.Comparator;

/**
 * Much thanks to the author of the Jira Labels Plugin.  Their code was a hugh help.
 *
 * @author rbarham
 */
@Scanned
public class RestValuesSearcher extends ExactTextSearcher implements CustomFieldStattable {
    private static final Logger logger = Logger.getLogger(RestValuesSearcher.class);
    private JqlOperandResolver jqlOperandResolver;
    private CustomFieldInputHelper customFieldInputHelper;
    private CustomField customField;

    private SearchInputTransformer searchInputTransformer;
    private SearchRenderer searchRenderer;

    public RestValuesSearcher(
           @ComponentImport JqlOperandResolver jqlOperandResolver,
           @ComponentImport CustomFieldInputHelper customFieldInputHelper,
           @ComponentImport FieldVisibilityManager fieldVisibilityManager) {
        super(jqlOperandResolver, customFieldInputHelper, fieldVisibilityManager);
        this.jqlOperandResolver = jqlOperandResolver;
        this.customFieldInputHelper = customFieldInputHelper;
        logger.info("RestValuesSearcher");
        logger.info("RestValuesSearcher");
        logger.info("RestValuesSearcher");
        logger.info("RestValuesSearcher");
        logger.info("RestValuesSearcher");
        logger.info("RestValuesSearcher");
        logger.info("RestValuesSearcher");
        logger.info("RestValuesSearcher");
        logger.info("RestValuesSearcher");
        logger.info("RestValuesSearcher");
        logger.info("RestValuesSearcher");
        logger.info("RestValuesSearcher");

    }

    @Override
    public void init(CustomField field) {
        logger.info("initRestValuesSearcher");
        logger.info("initRestValuesSearcher");
        logger.info("initRestValuesSearcher");
        logger.info("initRestValuesSearcher");
        logger.info("initRestValuesSearcher");
        logger.info("initRestValuesSearcher");
        logger.info("initRestValuesSearcher");
        logger.info("initRestValuesSearcher");
        logger.info("initRestValuesSearcher");
        logger.debug("Init. Custom field is " + field.getFieldName());

        customField = field;

        ClauseNames clauseNames = customField.getClauseNames();
        JqlSelectOptionsUtil jqlSelectOptionsUtil = ComponentAccessor.getComponent(JqlSelectOptionsUtil.class);
        QueryContextConverter queryContextConverter = new QueryContextConverter();
        FieldVisibilityManager fieldVisibilityManager = ComponentAccessor.getComponent(FieldVisibilityManager.class);

        searchInputTransformer = new RestSearchInputTransformer(
                customField.getId(),
                clauseNames,
                field,
                jqlOperandResolver,
                jqlSelectOptionsUtil,
                queryContextConverter,
                customFieldInputHelper);

        searchRenderer = new RestCustomFieldRenderer(
                clauseNames,
                getDescriptor(),
                customField,
                new RestCustomFieldValueProvider(),
                fieldVisibilityManager);

        super.init(field);
    }

    @Override
    public SearchInputTransformer getSearchInputTransformer() {
        return searchInputTransformer;
    }

    @Override
    public SearchRenderer getSearchRenderer() {
        return searchRenderer;
    }

    @Override
    public LuceneFieldSorter getSorter(final CustomField customField) {
        return new TextFieldSorter(customField.getId()) {
            @Override
            public Comparator<String> getComparator() {
                return new SortStringComparator(customField);
            }
        };
    }

    public StatisticsMapper getStatisticsMapper(CustomField customField) {
        return new AbstractCustomFieldStatisticsMapper(customField) {
            @Override
            protected String getSearchValue(Object o) {
                if (o == null) {
                    return null;
                } else {
                    return ((IdAndString) o).getId();
                }
            }

            public Object getValueFromLuceneField(String id) {
                // Convert the primary key into something nice
                if (id != null) {
                    String text = RestValuesViewHelper.getViewHelper(customField).getTextForStatistics(id, getDescriptor().getI18nBean());
                    return new IdAndString(id, text);
                } else {
                    return null;
                }
            }

            @Override
            public Comparator getComparator() {
                return (o1, o2) -> {
                    if (o1 == null && o2 == null) {
                        return 0;
                    } else if (o1 == null) {
                        return 1;
                    } else if (o2 == null) {
                        return -1;
                    }
                    return ((IdAndString) o1).getText().compareTo(((IdAndString) o2).getText());
                };
            }
        };
    }

    /**
     * Small wrapper class that allows to get the original id back easily, so click-through
     * on the pie chart works
     */
    public static class IdAndString {
        private String m_id;
        private String m_text;

        public IdAndString(String id, String text) {
            m_id = id;
            m_text = text;
        }

        public String getId() {
            return m_id;
        }

        public String getText() {
            return m_text;
        }

        @Override
        public String toString() {
            // This is shown as the text in the webbrowser, so we return something nice
            return getText();
        }
    }

    private static class SortStringComparator implements Comparator<String> {
        private final CustomField m_customField;

        public SortStringComparator(CustomField customField) {
            m_customField = customField;
        }

        public int compare(String s, String s1) {
            return s.compareTo(s1);
        }
    }
}
