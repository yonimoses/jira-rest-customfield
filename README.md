## Inspired by

This plugin is heavily inspired by `jdvp`.
 [the code is here](https://bitbucket.org/wimdeblauwe/jdvp/).
Although it's a complete rewrite and the original is couple of years, it's still Rockz!

## General

This plugin, build for JIRA 8+ , defined a rest custom field which allows you to connect the field to a rest endpoint.
After adding this custom field to an issue edit screen, you can select one of those values so it will be associated with the current issue.

# Development
Please refer to atlassian and their atlas cli on how to run/debug/develop it.

# Installation

1. Copy the `thejaxx-rest-plugin-xxx.jar` (or `obr`) to JIRA's plugin as explained in JIRA's docs
2. Add a new custom field of type 'Rest Values Selection Field' 

# Configuration

The custom field can be configured with the Configure button in the addons section  in jira.:


# Donations

None is required. This plugin is fully done in my spare time.

# Frequently Asked Questions

## Things are not working, where do I get help?

If you have problems getting the plugin to work, please post a question on on [Atlassian Answers](https://answers.atlassian.com/) (tag your question with `jira` and `rest plugin`).
or open a issue here. 
For custom implementation or whatever, you can also post a issue on github 

## How do I enable the logging of the plugin?
the logger is `com.thejaxx` . besides that it's log4j as all plugins in JIRA.