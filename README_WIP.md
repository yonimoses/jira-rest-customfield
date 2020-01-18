**Table Of Contents**

[TOC]

# Description/Features

## General

This plugin, build for JIRA 8+ , defined a rest custom field which allows you to connect the field to a rest endpoint.
After adding this custom field to an issue edit screen, you can select one of those values so it will be associated with the current issue.

You can choose how the values are rendered for viewing, editing and searching independently. For the editing, you can use a combobox (single select or cascading select) or an AJAX-style input field.
Internally, the plugin will store only the primary key of the item you have selected, so you can safely edit things in your database, as long as you keep the primary key constant.

## Example

Suppose you already have a database with customer information. Your JIRA is not open to customers, but you would like to track which customer has put in which request. With this custom field, you point to that database, put in an SQL script that extracts the customers and you can start choosing the correct customer in your issues.

# Installation

1. Copy the `database-values-plugin-xxx.jar` to `WEB-INF/lib`
2. Add a new custom field of type 'Rest Values Selection Field' (You might want to do this before creating the properties file so you can know the custom field id)

Screenshot when the configuration file could not be found by the plugin:

![No config file yet](screenshots/no%20config%20file%20yet.png)

# Configuration

The custom field must be configured using a properties file. E.g.:

```

    # The database connection parameters
    database.driver=org.hsqldb.jdbcDriver
    database.user=sa
    database.password=
    database.connection.url=jdbc:hsqldb:mem:plugintestdb

    # Cache Timeout (= 15 minutes by default). The actual db is queried only once and then the results are kept in the cache for the given timeout. Uncomment the line below to change it.
    #cache.timeout=900000

    # The SQL Query that will be executed on the database
    sql.query=select id, firstname, lastname, city, country from customer
    # The column number (starting from 0) that contains the primary key of the returned data.
    primarykey.column.number=0
    # The pattern that should be used to render the data in view mode. Use {column_number} as placeholders. You can use HTML, but make sure you close your tags properly!
    rendering.viewpattern={1} {2} from <a href="http://maps.google.com/maps?f=q&hl=nl&geocode=&q={3}, {4}">{3}, {4}</a>
    # The pattern that should be used to render the data in edit mode. Use {column_number} as placeholders.
    rendering.editpattern={2}, {1}
    # The pattern that should be used to render the data in searcher. Use {column_number} as placeholders.
    rendering.searchpattern={1} {2} ({0})
    # This is used when sorting in the issue navigator. When not defined, the 'rendering.viewpattern' is used.
    rendering.sortpattern={1} {2}
    # Use 0 to have a combobox for editing, 1 to have AJAX-style input field, 2 for cascading select
    edit.type=0
    # Use 0 to have a list for searching, 1 to have AJAX-style input field
    search.type=0
    # The pattern that is used for the history and the activity view. If not specified, the 'rendering.viewpattern' is used. Note that you cannot use HTML.
    rendering.changelog.viewpattern={1} {2}
    # The pattern that is used for the pie chart, 2d filter statistics and single level group by. If not specified, the 'rendering.viewpattern' is used. Note that you cannot use HTML.
    rendering.statistics.viewpattern={1} {2}
```

The name of the properties file must be jira-database-values-plugin-10000.properties where 10000 should be replaced with the id of the custom field. This allows you to define multiple custom fields with different queries or even pointing to completely different databases.

As you can see, you can even include some html into the viewing. In the above example, clicking the link will open a window of google maps with the address.

## Database parameters

The first 4 parameters (`database.driver`, `database.user`, `database.password`, `database.connection.url`) are needed to be able
to connect to the database. They define what database should be used, where it is and what user should be used to read from it.

The sql.query parameter contains the SQL statement that will be used to query the database. Note that I do not do any checking
on the query to see if you are not doing anything wrong with it. You are advised to only use SELECT statements :-)


## Rendering parameters

The `sql.query` will return a number of database rows with a number of columns.
The order of the columns is important for the rendering parameters (`rendering.viewpattern`, `rendering.editpattern`, `rendering.searchpattern` and `rendering.sortpattern`) and the `primarykey.column.number` parameter.
This last property defines what value the plugin will store in the JIRA database to know what value was selected by the user. You are strongly advised to use the column that contains your primary key there.

In the rendering patterns, you must use MessageFormat-alike formatting to define where the values should come. If we look at the following example:
```
    rendering.editpattern={2}, {1}
```

Here we define that the pattern for editing should contain the 3rd column, then a comma and a space and then the 2nd column (Note that we work zero-based!)

### Sorting

You can control how the custom field is sorted in the issue navigator via `rendering.sortpattern`.

The pattern is optional. When not defined, the `rendering.viewpattern` is used. This pattern can be useful if the `rendering.viewpattern` uses some HTML elements and you don't want those to interfere with proper sorting.
The sorting pattern is only used internaly to ensure proper sorting. The actual value you see in the issue navigator is defined by the `rendering.viewpattern`. The sorting is not case sensitive.

By using this separate property, we can display the names as:

> Wim Deblauwe from Heule, Belgium

but sort on last name for example by setting the `rendering.sortpattern={2}`.

TIP: If you want to sort in the drop-down menu that is used when selecting a value, then you need to use ORDER BY in you SQL query.

Example screenshot:

![Sorting](screenshots/sorting.png)

### Depending on the project

You can make the results of the custom field depend on the actual project that is being used if you use the following SQL query syntax:
```
    sql.query=select id, firstname, lastname, city, country from customer where jira_key like '${jira.project.key}'
```
The above example assumes that you have a `jira_key` column in your table containing the JIRA project key.

IMPORTANT: You need to use _like_ in your where clause otherwise, things will not work properly.

You also need to specify the `primarykey.column.name` property with the exact name of your primary key column. This is needed because the cache is bypassed for the issue navigator when depending on the project.

Also know that depending on the project has an impact on performance in the issue navigator.
Since issues from multiple projects can be shown in the issue navigator, I bypass the cache there (since otherwise, I still need to put everything in the cache).
To avoid the cache growing too big, you need to configure `cache.maximum.projects` when using this functionality. This parameter defines how many different projects are being kept in the cache at the same time.
By default this is 1. If you have only a small number of rows but many projects, you can increase this without problem to the number of projects you have.
If you have a large number of rows per project, you need to keep this low.

### Depending on the currently logged in user

To make the SQL query result depend on who is currently logged in, use `${jira.user}` in the `sql.query` property:
```
    sql.query=select id, firstname, lastname, city, country from customer where responsible like '${jira.user}'
```

## Configuring AJAX-style autocomplete input field

By default, a combobox is used to allow the user to input a value. However, you can configure the plugin to use an AJAX-style input field. For that, you need to set `edit.type` in the properties file to `1`.

It is also possible to add additional information with the AJAX-style input field that will be displayed when the user is making his choice, but will not appear in the input box once he has made his selection.
For this, add a <br/> tag in the configuration:

```
    rendering.editpattern={1} {2}<br/>{3}, {4}
```

You can control the width of the field by using the parameter `rendering.editwidth`. By default, this is 50.

Example screenshot:

![Autocomplete inputfield](screenshots/ajax.png)

It is also possible to format text in italic or bold using `<i/>` and `<b/>` tags. For example:

```
    rendering.editpattern={2}, {1}<br/>{3}, <i>{4}</i> <b>bold text<b/>
```

## Configurating cascading select comboboxes

You can use a cascading select (like the build-in custom field) also with this plugin. If you have a lot of entries, it might be easier for the user to first select some kind of category and then make this final selection. For this you need the following configuration:

```
    edit.type=2
    rendering.editpattern.group.column.number=4
```

An `edit.type` of 2 means you want a cascading select. The `rendering.editpattern.group.column.number` identifies the column you want to group by.
In our customer example, we group by the country. So a user can first select the country and then the 2nd combobox will only contain the customers from that country.

## Configuration of the JQL function

To support the `dbValuesMatching` JQL function, you need to do some configuration. The function takes 2 arguments: a query reference and the actual query value. The query reference allows the plugin to know what query should be executed against the database when the JQL function is used.

This is an example:
```
    jql.1.query.reference=Country
    jql.1.query=select id from customer where country = 'QUERY_VALUE'
    jql.2.query.reference=Lastname
    jql.2.query=select id from customer where lastname = 'QUERY_VALUE'
```

It defines 2 query references (you can define as much as you want). If you search like this:
```
Customer IN dbValuesMatching("Country", "Belgium")
```

then the plugin will look up what query it needs to do and replace `QUERY_VALUE` with the 2nd argument you pass into the JQL function. In this example, the above JQL query will result in this SQL query against the database:
```
select id from customer where country = 'Belgium'
```

Important is that the SQL statement can only return 1 column and that column *must* match the primary key column you have defined.

Example screenshot:

![Search](screenshots/search.png)

# Donations

This plugin is fully done in my spare time. As such, donations are more then welcome. This usually helps to put your requests quickly at the top of my priority list.
Donations are accepted via PayPal. See the donate button on the right of [my blog](https://wimdeblauwe.wordpress.com/).

# Frequently Asked Questions

## Things are not working, where do I get help?

If you have problems getting the plugin to work, please post a question on on [Atlassian Answers](https://answers.atlassian.com/) (tag your question with `jira` and `jdvp`).
Please do not email me directly (unless you need custom implementations/additions to this plugin). You are far more likely to get useful feedback there.

## How do I enable the logging of the plugin?

See [Enable the plugin logging](PluginLogging)
