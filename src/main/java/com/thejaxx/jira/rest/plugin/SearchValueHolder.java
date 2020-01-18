package com.thejaxx.jira.rest.plugin;


public class SearchValueHolder
{
	private String value;
	private String id;

	SearchValueHolder( String id, String value )
	{
		this.id = id;
		this.value = value;
	}

	public String getId()
	{
		return id;
	}

	public String getValue()
	{
		return value;
	}

	public String toString()
	{
		return value;
	}

}
