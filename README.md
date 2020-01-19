## Inspired by

This plugin is heavily inspired by `jdvp`.
 [the code is here](https://bitbucket.org/wimdeblauwe/jdvp/).
Although it's a complete rewrite and the original is couple of years, it's still Rockz!

## General

This plugin, build for JIRA 8+ , defined a rest custom field which allows you to connect the field to a rest endpoint.
After adding this custom field to an issue edit screen, you can select one of those values so it will be associated with the current issue.

# Development
Please refer to atlassian and their [atlassian-sdk](https://developer.atlassian.com/server/framework/atlassian-sdk/) on how to run/debug/develop it.
It's been a journey learning it.

# Installation

1. Copy the `thejaxx-rest-plugin-xxx.jar` (or `obr`) to JIRA's plugin as explained in JIRA's docs
2. Add a new custom field of type 'Rest Values Selection Field' 

# Configuration

The custom field can be configured with the Configure button in the add-ons section in jira


# Frequently Asked Questions

## Things are not working, where do I get help?

* General issue? post a question to [Atlassian Answers](https://answers.atlassian.com/) (tag your question with `jira` and `plugin`).
* Open an issue here.
 

## I need something special!

* If it's not hard, and everyone will benefit from it - post an issue. 
* If it's REALLY custom - post an issue also :).
* Open a pull request, you're more then welcome. 

## How do I enable the logging of the plugin?

the logger is `com.thejaxx` . besides that it's log4j as all plugins in [JIRA](https://confluence.atlassian.com/doc/configuring-logging-181535215.html).