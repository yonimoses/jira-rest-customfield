package com.thejaxx.jira.rest.plugin;

import com.atlassian.jira.util.I18nHelper;

public class RestRowFormatter
{
	private RestRowFormatter()
	{

	}

	public static String formatRestRow(RestRow restRow, String renderingPattern, I18nHelper i18nHelper )
	{
//		LoggerUtils.main(LogLevel.DEBUG,"formatRestRow :: " + restRow);
		String result = renderingPattern;
		if (restRow != null)
		{
			String value = restRow.getValue();
//			if (value != null)
//			{
//				result = result.replaceAll( "\\{" + 0 + "(,[^}]*)?\\}", Matcher.quoteReplacement( value.trim() ) );
//			}
			return value;
		}

//		if (restRow != null)
//		{
//			for (int i = 0; i < 1; i++)
//			{
//				Object value = restRow.getValue();
//				if (value != null)
//				{
//					result = result.replaceAll( "\\{" + i + "(,[^}]*)?\\}", Matcher.quoteReplacement( value.toString().trim() ) );
//				}
//				else
//				{
//					result = result.replaceAll( "\\{" + i + "(,([^}]*))?\\}", "$2" );
//				}
//			}
//		}
//		result = result.replaceAll( "'", "&#39;" );

		return "";
	}

}
