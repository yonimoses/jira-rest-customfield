package com.thejaxx.jira.rest.plugin;

import com.atlassian.jira.issue.customfields.CustomFieldValueProvider;
import com.atlassian.jira.issue.customfields.searchers.renderer.CustomFieldRenderer;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.search.ClauseNames;
import com.atlassian.jira.issue.search.searchers.impl.NamedTerminalClauseCollectingVisitor;
import com.atlassian.jira.plugin.customfield.CustomFieldSearcherModuleDescriptor;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.FieldVisibilityManager;
import com.atlassian.query.Query;
import org.apache.log4j.Logger;

public class RestCustomFieldRenderer extends CustomFieldRenderer {

	private ClauseNames clauseNames;

	public RestCustomFieldRenderer(
			ClauseNames clauseNames,
			CustomFieldSearcherModuleDescriptor customFieldSearcherModuleDescriptor,
			CustomField field,
			CustomFieldValueProvider customFieldValueProvider,
			FieldVisibilityManager fieldVisibilityManager) {
		super(clauseNames, customFieldSearcherModuleDescriptor, field, customFieldValueProvider, fieldVisibilityManager);
		
		this.clauseNames = clauseNames;
	}

	/**
	 * @see <a href="http://confluence.atlassian.com/display/JIRA/Updating+JIRA+Plugins+for+JIRA+4.0#UpdatingJIRAPluginsforJIRA4.0-NewSearching">Updating JIRA Plugins for JIRA 4.0</a>
	 */
	@Override
	public boolean isRelevantForQuery(ApplicationUser searcher, Query query) {
		final NamedTerminalClauseCollectingVisitor clauseVisitor = new NamedTerminalClauseCollectingVisitor(clauseNames.getJqlFieldNames());
		if(query != null && query.getWhereClause() != null){
			query.getWhereClause().accept(clauseVisitor);
		}
		return clauseVisitor.containsNamedClause();
	}
}
