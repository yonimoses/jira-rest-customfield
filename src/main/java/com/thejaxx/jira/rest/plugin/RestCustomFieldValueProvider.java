package com.thejaxx.jira.rest.plugin;

import org.apache.log4j.Logger;

import com.atlassian.jira.issue.customfields.CustomFieldValueProvider;
import com.atlassian.jira.issue.customfields.MultiSelectCustomFieldValueProvider;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.transport.FieldValuesHolder;

public class RestCustomFieldValueProvider extends Object implements CustomFieldValueProvider {
	private static final Logger logger = Logger.getLogger( RestCustomFieldValueProvider.class );

	private final CustomFieldValueProvider customFieldValueProvider = new MultiSelectCustomFieldValueProvider();

	public RestCustomFieldValueProvider() {}

	/**
	 * This method uses the MultiSelectCustomFieldValueProvider class so that an array of values is returned
	 */
	public Object getStringValue(CustomField customField, FieldValuesHolder fieldValuesHolder) {
		Object values = customFieldValueProvider.getStringValue( customField, fieldValuesHolder );

		// The following code, modified from the original getStringValue,
		//returns the values of the row, not the indexes.  It appears that the original version (in 3.13, at least) returned the index.
		// Also, seeing as the DatabaseValuesViewHelper.getHtmlForSearch uses indexes, this method must return those at this point.
		// I'm not sure if this is intended, but, in order to get working, this method needs to return the indexes.
		/*
		if(values != null && values instanceof List<?>){
			List<String> arrayValues = (List<String>)values;
			DatabaseValuesViewHelper databaseValuesViewHelper = DatabaseValuesSearcher.getViewHelper(customField);
			if(arrayValues.size() > 1){
				ArrayList<String> result = new ArrayList<String>(arrayValues.size());
				for(String value : arrayValues){
					//String sorting = databaseValuesViewHelper.getStringForSearch(value);
					String sorting = databaseValuesViewHelper.getStringForSorting(value);
					result.add( sorting );
				}
				return result;
			}
			return databaseValuesViewHelper.getStringForSorting(arrayValues.get(0));
		}
		*/

		return values;
	}

	public Object getValue(CustomField customField, FieldValuesHolder fieldValuesHolder) {
		return getStringValue(customField, fieldValuesHolder);
	}

}
