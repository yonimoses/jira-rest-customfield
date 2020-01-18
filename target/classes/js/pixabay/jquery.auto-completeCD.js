/*
    Copyright (c) 2014 Simon Steinberger / Pixabay
    Original repo: GitHub: https://github.com/Pixabay/jQuery-autoComplete
	License: http://www.opensource.org/licenses/mit-license.php
*/

(function ($) {
    $.fn.autoCompleteCD = function (options) {
        var o = $.extend({}, $.fn.autoCompleteCD.defaults, options);
        o.multiChoiceDelimiterRE = new RegExp(o.multiChoiceDelimiter + '\s*', "g");

        function log(s) {
            if (o.isLoggingEnabled) {
                console.log && console.log('Pixabay autoCompleteCD: ', s);
            }
        }

        // public methods
        if (typeof options == 'string') {
            this.each(function () {
                var that = $(this);
                if (options == 'destroy') {
                    $(window).off('resize.autocompleteCD', that.updateSC);
                    that.off('blur.autocompleteCD focus.autocompleteCD keydown.autocompleteCD keyup.autocompleteCD');
                    if (that.data('autocompleteCD'))
                        that.attr('autocompleteCD', that.data('autocompleteCD'));
                    else
                        that.removeAttr('autocompleteCD');
                    $(that.data('sc')).remove();
                    that.removeData('sc').removeData('autocompleteCD');
                }
            });
            return this;
        }

        return this.each(function () {
            var that = $(this);
            // sc: suggestions container
            that.sc = $('<div class="autocompleteCD-suggestions ' + o.menuClass + '"></div>');
            that.data('sc', that.sc).data('autocompleteCD', that.attr('autocompleteCD'));
            that.attr('autocompleteCD', 'off');
            that.cache = {};
            that.last_val = '';

            that.updateSC = function (resize, next) {
                that.sc.css({
                    top: that.offset().top + that.outerHeight(),
                    left: that.offset().left,
                    width: that.outerWidth()
                });
                if (!resize) {
                    that.sc.show();
                    if (!that.sc.maxHeight) that.sc.maxHeight = parseInt(that.sc.css('max-height'));
                    if (!that.sc.suggestionHeight) that.sc.suggestionHeight = $('.autocompleteCD-suggestion', that.sc).first().outerHeight();
                    if (that.sc.suggestionHeight)
                        if (!next) that.sc.scrollTop(0);
                        else {
                            var scrTop = that.sc.scrollTop(), selTop = next.offset().top - that.sc.offset().top;
                            if (selTop + that.sc.suggestionHeight - that.sc.maxHeight > 0)
                                that.sc.scrollTop(selTop + that.sc.suggestionHeight + scrTop - that.sc.maxHeight);
                            else if (selTop < 0)
                                that.sc.scrollTop(selTop + scrTop);
                        }
                }
            };

            $(window).on('resize.autocompleteCD', that.updateSC);

            that.sc.appendTo('body');

            that.sc.on('mouseleave', '.autocompleteCD-suggestion', function () {
                $('.autocompleteCD-suggestion.selected').removeClass('selected');
            });

            that.sc.on('mouseenter', '.autocompleteCD-suggestion', function () {
                $('.autocompleteCD-suggestion.selected').removeClass('selected');
                $(this).addClass('selected');
            });

            that.sc.on('mousedown click', '.autocompleteCD-suggestion', function (e) {
                var $choiceElement = $(this);
                var choiceElementDataVal = $choiceElement.data('val');
                if (choiceElementDataVal || $choiceElement.hasClass('autocompleteCD-suggestion')) { // else outside click
                    o.onSelect(e, that, getValueWithoutSuggestion(that.val()), $choiceElement);
                    that.sc.hide();
                }
                return false;
            });

            function getValueWithoutSuggestion(value) {
                var newValue;
                if (value.trim().length === 0) {
                    newValue = '';
                } else {
                    lastDelimiterPosition = value.lastIndexOf(o.multiChoiceDelimiter);
                    if (lastDelimiterPosition !== -1) {
                        newValue = value.slice(0, lastDelimiterPosition);
                    } else {
                        newValue = '';
                    }
                }
                return newValue;
            }

            that.on('blur.autocompleteCD', function () {
                try {
                    over_sb = $('.autocompleteCD-suggestions:hover').length;
                } catch (e) {
                    over_sb = 0;
                } // IE7 fix :hover
                if (!over_sb) {
                    that.last_val = that.val();
                    that.sc.hide();
                    setTimeout(function () {
                        that.sc.hide();
                    }, 350); // hide suggestions on fast input
                } else if (!that.is(':focus')) {
                    setTimeout(function () {
                        that.focus();
                    }, 20);
                }
                o.onBlur(that);
            });

            if (!o.minChars) that.on('focus.autocompleteCD', function () {
                that.last_val = '\n';
                that.trigger('keyup.autocompleteCD');
            });

            function suggestFn(data) {
                var val = that.val();
                that.cache[val] = data;
                var lengthOfLastSuggestion = val.split(o.multiChoiceDelimiterRE).pop().length;
                if (data.length && lengthOfLastSuggestion >= o.minChars) {
                    var s = '';
                    for (var i = 0; i < data.length; i++) s += o.renderChoiceElement(data[i], val);
                    that.sc.html(s);
                    that.updateSC(0);
                } else
                    that.sc.hide();
            }

            that.on('keydown.autocompleteCD', function (e) {
                // down (40), up (38)
                if ((e.which == 40 || e.which == 38) && that.sc.html()) {
                    var next, sel = $('.autocompleteCD-suggestion.selected', that.sc);
                    if (!sel.length) {
                        next = (e.which == 40) ? $('.autocompleteCD-suggestion', that.sc).first() : $('.autocompleteCD-suggestion', that.sc).last();
                        next.addClass('selected').data('val');
                    } else {
                        next = (e.which == 40) ? sel.next('.autocompleteCD-suggestion') : sel.prev('.autocompleteCD-suggestion');
                        if (next.length) {
                            sel.removeClass('selected');
                            next.addClass('selected').data('val');
                        } else {
                            sel.removeClass('selected');
                            next = 0;
                        }
                    }
                    that.updateSC(0, next);
                    return false;
                }
                // esc
                else if (e.which == 27) that.val(that.last_val).sc.hide();
                // enter or tab
                else if (e.which == 13 || e.which == 9) {
                    var $choiceElement = $('.autocompleteCD-suggestion.selected', that.sc);
                    if ($choiceElement.length && that.sc.is(':visible')) {
                        // Remove trailing newline. The newline occurs if the user selects a choice by pressing the Enter key.
                        var choiceElementDataVal = $choiceElement.data('val').replace(/[\n\r]*$/, '');
                        o.onSelect(e, that, getValueWithoutSuggestion(that.val()), $choiceElement);
                        // Prevent newline in the input element.
                        e.preventDefault();
                        setTimeout(function () {
                            that.sc.hide();
                        }, 20);
                    }
                }
            });

            that.on('keyup.autocompleteCD', function (e) {
                if (!~$.inArray(e.which, [13, 27, 35, 36, 37, 38, 39, 40])) {
                    var searchTermn = that.val();
                    var lengthOfLastSuggestion = searchTermn.split(/,\s*/).pop().length;
                    if (lengthOfLastSuggestion >= o.minChars) {
                        if (searchTermn != that.last_val) {
                            that.last_val = searchTermn;
                            clearTimeout(that.timer);
                            if (o.cache) {
                                if (searchTermn in that.cache) {
                                    suggestFn(that.cache[searchTermn]);
                                    return;
                                }
                                // no requests if previous suggestions were empty
                                for (var i = 1; i < searchTermn.length - o.minChars; i++) {
                                    var part = searchTermn.slice(0, searchTermn.length - i);
                                    if (part in that.cache && !that.cache[part].length) {
                                        suggestFn([]);
                                        return;
                                    }
                                }
                            }
                            that.timer = setTimeout(function () {
                                var lastSearchTerm = searchTermn.split(o.multiChoiceDelimiterRE).pop().trim();
                                o.source(lastSearchTerm, o.choiceObjects, suggestFn)
                            }, o.delay);
                        }
                    } else {
                        that.last_val = searchTermn;
                        that.sc.hide();
                    }
                }
            });
        });
    };

    $.fn.autoCompleteCD.defaults = {
        minChars: 2,
        delay: 150,
        cache: false,
        menuClass: '',
        // You can select multiple choices one after the other, and the autoCompleteCD will put them into the input field concatenated
        // by this delimiter.
        // Note: This delimiter must not be part of any choice!
        multiChoiceDelimiter: ',',
        choiceObjects: [],
        source: function (searchTerm, choiceObjects, suggestFn) {
            // console.log('source: searchTerm ', searchTerm, ', choiceObjects.length ', choiceObjects.length);

            // This default function handles a simple string array of choices. E. g. ['Choice A', 'Choice B']
            // In more complex scenarios, choices could be an array of objects: [ ['Choice A', 'A'], ['Choice B', 'B'] ].
            // Or a JSON: [ {"longName": "Choice A", "shortName": "A"}, {"longName": "Choice A", "shortName": "A"} ].
            // var lastTermLC = searchTerm.split(/,\s*/).pop().toLowerCase();
            var suggestions = [];
            var searchTermLC = searchTerm.toLowerCase();
            for (i = 0; i < choiceObjects.length; i++) {
                if (~choiceObjects[i].toLowerCase().indexOf(searchTermLC)) {
                    suggestions.push(choiceObjects[i]);
                }
            }
            suggestFn(suggestions);
        },
        renderChoiceElement: function (choiceObject, searchTerm) {
            // Escape special characters.
            searchTerm = searchTerm.replace(/[-\/\\^$*+?.()|[\]{}]/g, '\\$&');
            var re = new RegExp("(" + searchTerm.split(' ').join('|') + ")", "gi");
            // The div-text is the formatted display text. The data-val is mandatory and contains the plain text representing the
            // chosen choice.
            // All further choiceObject-attributes (if given) important at onSelect has to be conveyed as separate data-x.
            // In the most simple case as showed here, the choiceObject is a string (the choice itself).
            return '<div class="autocompleteCD-suggestion" data-val="' + choiceObject + '">' + choiceObject.replace(re, "<b>$1</b>") + '</div>';
        },
        onSelect: function (e, $autocompleteElement, valueWithoutSuggestion, $choiceElement) {
            var newValue;
            if(valueWithoutSuggestion.trim().length === 0){
                newValue = $choiceElement.data('val');
            }else{
                newValue = valueWithoutSuggestion + ', ' + $choiceElement.data('val');
            }
            $autocompleteElement.val(newValue);
        },
        onBlur: function ($autocompleteElement) {
        }
    };
}(jQuery));
