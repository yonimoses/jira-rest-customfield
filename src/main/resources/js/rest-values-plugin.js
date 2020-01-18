jQuery.noConflict();
console.log("HELLO FROM JS");
jQuery(function () {

    var IS_LOGGING_ENABLED = true;

    function log(s) {
        IS_LOGGING_ENABLED && AJS.log('Rest Plugin ::', s);
    }


    function disableInlineEditForCustomField(customfieldId) {

        if (typeof JIRA.Issues === 'undefined') {
            return;
        }

        var _this = this;
        JIRA.bind(JIRA.Events.NEW_CONTENT_ADDED, function (a, b) {
            _this._disableInlineEditForCustomField(customfieldId);

        });
        this._disableInlineEditForCustomField(customfieldId);
    }

    function _disableInlineEditForCustomField(customfieldId) {
        var view = AJS.$(JIRA.Issues.IssueFieldUtil.getFieldSelector(customfieldId));
        var disableFunction = function (e) {
            if (AJS.$(this).is('.editable-field') || (AJS.$(this).is('.editable-field > *'))) {
                log('Disabling inline edit for :: ' + customfieldId);
                view.removeClass('editable-field');
                view.removeClass('inactive');
                view.attr('title', AJS.I18n.getText("at.celix.jira.plugins.zones.fieldDisabled"));
                view.find('.icon').remove();
                view.unbind('mouseenter').unbind('mouseleave').unbind('mousemove').unbind('mouseover');
            }
        };
        if (view.is(':hover')) {
            view.delegate('*,', 'mouseover', disableFunction);
            view.hover(disableFunction);
            view.mousemove(disableFunction);
        } else {
            view.hover(disableFunction);
        }
    }


    function registerAutocomplete() {

        jQuery('.rest-ui-autocomplete-input').each(function (index, element) {
            var $me = jQuery(this);
            // Note, the element-id is 'databasevalues_<customfieldId>'.
            var customFieldId = $me.data('customfieldid');
            var projectKey = $me.data('projectkey');
            var ajaxPurposeName = $me.data('ajaxpurposename');
            var hiddenInputFieldId = $me.data('hiddeninputfieldid');

            var strJson = "{";
            $.each($me.data(), function(i, v) {
                strJson += '"' + i + '":"' + v + '",';
            });
            strJson = strJson.substring(0, strJson.length - 1);
            strJson += '}';
            var asJson = $.parseJSON( strJson );

            if(IS_LOGGING_ENABLED){
                console.debug(asJson);
            }
            // var json = '{id'
            // log('register autoComplete, id ' + $me.attr('id')
            //     + ', customFieldId ' + customFieldId
            //     + ', projectKey ' + projectKey
            //     + ', ajaxPurposeName ' + ajaxPurposeName
            //     + ', hiddenInputFieldId ' + hiddenInputFieldId);

            jQuery(this).autoCompleteCD({
                minChars: 2,
                source: function (
                    searchTerm,        // Current search term
                    choiceObjects,     // array with possible options
                    suggestFn    // Function with the suggestions
                ) {
                    jQuery.ajax({
                        url: AJS.contextPath() + '/secure/ajaxResultsPageAction!default.jspa',
                        dataType: "json",
                        data: {
                            decorator: "none",
                            q: searchTerm,
                            customfieldId: customFieldId,
                            projectKey: projectKey,
                            purpose: ajaxPurposeName
                        },
                        success: function (data) {
                            log('Data returned ::' + JSON.stringify(data));

                            var suggestions = $.map(data, function (item) {
                                return {
                                    plainText: item.inputlabel,
                                    formattedText: item.label,
                                    pKey: item.value
                                }
                            });

                            // inputlabel => plainTextChoice
                            // label => formattedChoice
                            // value => pKey

                            log('Suggestions :: ');
                            // log('ajax success, suggestions ' + JSON.stringify(suggestions));

                            suggestFn(suggestions);
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            console.log("ERROR");
                            AJS.log(JSON.stringify(jqXHR));
                            AJS.log(textStatus);
                            AJS.log(errorThrown);
                        }
                    });
                },
                renderChoiceElement: function (choiceObject, searchTerm) {
                    log('renderChoiceElement: choiceObject ' + JSON.stringify(choiceObject) + ', search ' + JSON.stringify(searchTerm));

                    // escape special characters
                    searchTerm = searchTerm.replace(/[-\/\\^$*+?.()|[\]{}]/g, '\\$&');
                    var re = new RegExp("(" + searchTerm.split(' ').join('|') + ")", "gi");

                    // Escape " characters
                    var escapedPlainText = choiceObject.plainText.replace(/"/g, "&#34;");

                    return '<div class="autocompleteCD-suggestion"'
                        + ' data-val="' + escapedPlainText + '"'
                        + ' data-plaintext="' + escapedPlainText + '"'
                        + ' data-pkey="' + choiceObject.pKey + '"'
                        + ' >'
                        + escapedPlainText.replace(re, "<b>$1</b>") + '</div>';
                },
                onSelect: function (e, $autocompleteElement, valueWithoutSuggestion, $choiceElement) {
                    log('onSelect: $choiceElement plaintext ' + $choiceElement.data('plaintext') + ', pkey ' + $choiceElement.data('pkey'));
                    log('  customFieldId ' + customFieldId + ', hiddenInputFieldId ' + hiddenInputFieldId);
                    jQuery('#rest_' + customFieldId).val($choiceElement.data('plaintext')); // display the selected text
                    jQuery('#' + hiddenInputFieldId).val($choiceElement.data('pkey')); // save selected id to hidden input
                },
                onBlur: function ($autocompleteElement) {
                    var $hiddenInputField = $('#' + hiddenInputFieldId);

                    log('onBlur: '
                        + $autocompleteElement.attr('id') + ' Val length ' + $autocompleteElement.val().length
                        + ', '
                        + $hiddenInputField.attr('id') + ' val length ' + $hiddenInputField.val().length);

                    if ($autocompleteElement.val().length === 0 || $hiddenInputField.val().length === 0) {
                        log('  Clear val of ' + $autocompleteElement.attr('id') + ', ' + $hiddenInputField.attr('id'));
                        $autocompleteElement.val('');
                        $hiddenInputField.val('');
                    }
                }
            });
        });
    }

    AJS.toInit(function () {
        log('toInit :: NEW_CONTENT_ADDED');
        // When using /secure/CreateIssue.jspa, there is never a NEW_CONTENT_ADDED event, so we have to
        // register here already. For the pages that need the event, this is no problem as the registerAutocomplete
        // will not actually do anything as there will be no input fields on the page to register on.
        registerAutocomplete();

        JIRA.bind(JIRA.Events.NEW_CONTENT_ADDED, function (e, context, reason) {
            if (jQuery('#edit-issue-dialog').length > 0 || reason === 'inlineEditStarted' || reason === 'dialogReady') {
                log('bind');
                registerAutocomplete();
            }
        });
    });


});
