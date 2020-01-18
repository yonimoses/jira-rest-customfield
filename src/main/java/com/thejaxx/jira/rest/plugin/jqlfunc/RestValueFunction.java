//package com.bnhp.jira.plugin.rest.sap.jqlfunc;
//
//import com.atlassian.jira.JiraDataType;
//import com.atlassian.jira.JiraDataTypes;
//import com.atlassian.jira.issue.CustomFieldManager;
//import com.atlassian.jira.issue.fields.CustomField;
//import com.atlassian.jira.issue.search.managers.SearchHandlerManager;
//import com.atlassian.jira.jql.operand.QueryLiteral;
//import com.atlassian.jira.jql.query.QueryCreationContext;
//import com.atlassian.jira.plugin.jql.function.AbstractJqlFunction;
//import com.atlassian.jira.user.ApplicationUser;
//import com.atlassian.jira.util.MessageSet;
//import com.atlassian.jira.util.MessageSetImpl;
//import com.atlassian.query.clause.TerminalClause;
//import com.atlassian.query.operand.FunctionOperand;
//import com.bnhp.jira.plugin.rest.sap.utils.LogLevel;
//import com.bnhp.jira.plugin.rest.sap.utils.LoggerUtils;
//import com.bnhp.jira.plugin.rest.sap.RestCustomField;
//import com.bnhp.jira.plugin.rest.sap.RestValuesViewHelper;
//
//import javax.annotation.Nonnull;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
///**
// * JQL function to allow searching on 'secondary' properties of the value that is stored in the database.
// */
//
//public class RestValueFunction extends AbstractJqlFunction {
//
//    private static final int MIN_NUM_OF_ARGS = 2;
//
//    private final SearchHandlerManager m_searchHandlerManager;
//    private final CustomFieldManager m_customFieldManager;
//
//    public RestValueFunction(final SearchHandlerManager searchHandlerManager,
//                             final CustomFieldManager customFieldManager) {
//        m_searchHandlerManager = searchHandlerManager;
//        m_customFieldManager = customFieldManager;
//    }
//
//    public JiraDataType getDataType() {
//        return JiraDataTypes.TEXT;
//    }
//
//    @Override
//    public String getFunctionName() {
//        return "sapValues";
//    }
//
//    @Override
//    public boolean isList() {
//        return true;
//    }
//
//    public int getMinimumNumberOfExpectedArguments() {
//        return MIN_NUM_OF_ARGS;
//    }
//
//    @Nonnull
//    @Override
//    public MessageSet validate(ApplicationUser applicationUser, @Nonnull FunctionOperand operand, @Nonnull TerminalClause terminalClause) {
//        logger.debug( "validate  {}", applicationUser.getName());
//        logger.debug( "validate  {}", operand.getName());
//        logger.debug( "validate {}", operand.getDisplayString());
//        logger.debug( "validate  {}", operand.getArgs());
//        MessageSet result = new MessageSetImpl();
//        // Check if the customfield is a database values custom field
//        final Set<CustomField> fields = resolveField(false, applicationUser, terminalClause.getName());
//        if (fields.size() != 1) {
//            result.addErrorMessage("The " + getFunctionName() + " JQL function cannot be used with the field '" + terminalClause.getName() + "'");
//            return result;
//        }
//
//        // Check if we have 2 arguments to our function
//        final List<String> arguments = operand.getArgs();
////        if (arguments.size() != 2) {
////            result.addErrorMessage("The " + getFunctionName() + " JQL function needs 2 arguments (QueryReference, Value)");
////            return result;
////        }
//
//        // Check if the first argument is a valid query reference
//        String queryReference = arguments.get(0);
//        CustomField customField = fields.iterator().next();
////        RestValuesViewHelper viewHelper = RestValuesViewHelper.getViewHelper(customField);
////        if (!viewHelper.isExistingQueryReference(queryReference)) {
////            result.addErrorMessage("The query reference '" + queryReference + "' does not exist. Possible values are: " + viewHelper.getQueryReferences());
////            return result;
////        }
//        logger.debug( "Result  " + result.toString());
//
//        return result;
//    }
//
//    public List<QueryLiteral> getValues(QueryCreationContext queryCreationContext, FunctionOperand operand, TerminalClause terminalClause) {
//        logger.debug( "getValues {} ", operand.getArgs());
//        List<String> arguments = operand.getArgs();
//        if (arguments.size() != 2) {
//            return Collections.emptyList();
//        }
//
//        final Set<CustomField> fields = resolveField(queryCreationContext.isSecurityOverriden(), queryCreationContext.getApplicationUser(), terminalClause.getName());
//        if (fields.size() != 1) {
//            return Collections.emptyList();
//        }
//
//        CustomField customField = fields.iterator().next();
//        RestValuesViewHelper viewHelper = RestValuesViewHelper.getViewHelper(customField);
//
//        List<QueryLiteral> queryLiteralList = new ArrayList<QueryLiteral>();
//
//        String queryReference = arguments.get(0);
//        String queryValue = arguments.get(1);
//        Set<Object> queryValues = viewHelper.getQueryValues(queryReference, queryValue);
//        for (Object value : queryValues) {
//            queryLiteralList.add(new QueryLiteral(operand, String.valueOf(value)));
//        }
//        logger.debug( "getValues  queryLiteralList {}", queryLiteralList);
//
//        return queryLiteralList;
//    }
//
//    /**
//     * @param overrideSecurity false if only fields which the user can see should be resolved
//     * @param searcher         the user performing the search
//     * @param clauseName       the clause name used
//     * @return the set of {@link com.atlassian.jira.issue.fields.CustomField}s which map to the clause name and are also
//     * of the type {@link RestCustomField}; never null.
//     */
//    private Set<CustomField> resolveField(final boolean overrideSecurity, final ApplicationUser searcher, final String clauseName) {
//        logger.debug( "resolveField  {}", clauseName);
//
//        final Set<CustomField> fields = new HashSet<CustomField>();
//        final Collection<String> fieldIds = overrideSecurity ? m_searchHandlerManager.getFieldIds(clauseName) : m_searchHandlerManager.getFieldIds(searcher, clauseName);
//        for (String fieldId : fieldIds) {
//            final CustomField field = m_customFieldManager.getCustomFieldObject(fieldId);
//            if (field != null && field.getCustomFieldType() instanceof RestCustomField) {
//                fields.add(field);
//            } else {
//                LoggerUtils.main(LogLevel.INFO, String.format("jql clause name %s does not resolve to a database values field", clauseName));
//            }
//
//        }
//        return fields;
//    }
//}
