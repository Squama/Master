/*
 * Jc Combobox 
 * http://www.cpcn.com.cn
 *
 * @version v1.0.0.0
 * @auther dongyan
 *
 * Date: 2016-10-10
 */
Jc().$package(function(Jc) {

    function Combobox(el, options) {
        var that = this,
            defaults = {
                serviceUrl: null,
                dataArray: null,
                selectAsSource: false,
                onSelect: $.noop,
                bDestroyed: true,
                /**
                oInitializingData:{
                    value:'显示的数据',
                    data:'传递的数据'
                },
                */
                oInitializingData: null,
                readonly:false
            };

        that.element = el;
        that.el = $(el);
        that.options = $.extend({}, defaults, options);
        that.init(el);
    }

    Combobox.prototype = {
        init: function() {
            var that = this;

            if (!that.options.bDestroyed && that.el.data('Jc.combobox')) {
                return;
            }

            if ($(that.el).parent()[0].tagName === 'SPAN') {
                that.wrapper = $(that.el).parent('span').addClass('jc-combobox-container');
            } else {
                that.wrapper = $('<span>')
                    .addClass('jc-combobox-container')
                    .insertAfter(that.element);
            }


            that.createAutocomplete();
            that.createShowAllButton();
        },

        createAutocomplete: function() {
            var that = this,
                selected = that.el.children(':selected'),
                value = selected.val() ? selected.text() : "",
                width = that.el.css('width'),
                initValue = that.el.attr('initValue'),
                initData = that.el.attr('initData'),
                minLength = that.el.attr('minLength'),
                maxLength = that.el.attr('maxLength'),
                dataType = that.el.attr('dataType'),
                validtip = that.el.attr('validtip');

            that.el.removeAttr('minLength');
            that.el.removeAttr('maxLength');
            that.el.removeAttr('dataType');
            that.el.removeAttr('validtip');

            /** 
            that.input = $('<input>')
                .addClass('combobox-input')
                .css('width', width)
                .appendTo(that.wrapper)
                .val(value);
			*/

            that.input = $('<input>', {
                    'class': 'combobox-input',
                    'initValue': that.el.attr('initValue'),
                    'initData': that.el.attr('initData'),
                    'minLength': minLength,
                    'maxLength': maxLength,
                    'dataType': dataType,
                    'validtip': validtip,
                    'comboBox':'jc.combobox'
                }).css('width', width);

                if(that.options.readonly){
                    that.input.attr('readonly','1');
                }
                that.input.appendTo(that.wrapper)
                .val(value);

            that.el.hide();

            if (that.options.selectAsSource) {
                var func = that.options.onSelect;
                var arraySource = that.source(),
                    option = {
                        serviceUrl: null,
                        dataArray: arraySource,
                        basedSelect: that.element,
                        naluvalue:false,
                        onSelect: function(suggestion) {
                            $(this).val(suggestion.data);
                            func.call(this,suggestion);
                        }
                    };
                that.options = $.extend({}, that.options, option);
                $(that.input).attr('readonly', '1');
            }

            Jc.autocomplete($(that.input), that.options);


        },

        createShowAllButton: function() {
            var that = this,
                input = that.input,
                wasOpen = false;

            /** 
            $("<input type = 'button'>").appendTo(that.wrapper)
                .addClass("jc-combobox-dropdown-button")
                .mousedown(function() {
                    wasOpen = Jc.autocomplete($(that.input)).wasOpened();
                })
                .click(function() {
                    input.focus();

                    if (wasOpen) {
                        return;
                    }

                    Jc.autocomplete($(that.input)).showAllSuggestion();
                });
                */
            that.input.mousedown(function() {
                wasOpen = Jc.autocomplete($(that.input)).wasOpened();
            }).click(function() {
                input.focus();
                if (wasOpen) {
                    return false;
                }
                Jc.autocomplete($(that.input)).showAllSuggestion();
            });
            /*$("<span>").appendTo(that.wrapper.children('a'))
                         .addClass("jc-combobox-dropdown-span");*/
        },

        source: function() {
            var that = this;
            var result = that.el.children("option").map(function() {
                /*var text = $(this).text();
                if (this.value && (!request.term || matcher.test(text)))
                    return {
                        label: text,
                        value: text,
                        option: this
                    };*/
                return {
                    value: $(this).text(),
                    data: $(this).val()
                }
            });
            return $.map(result, function(result) {
                return {
                    value: result.value,
                    data: result.data
                };
            });
        },
        dispose: function() {
            var that = this;
            //移除所有事件，移除datakey
            that.el.off('.combobox').removeData('Jc.combobox');
            //移除window上的resize
            //移除初始化时的mainContainer
            $(that.wrapper).remove();
        }
    }

    Jc.combobox = function(contexts, options, args) {
        var dataKey = "Jc.combobox";
        if (arguments.length === 1) {
            return contexts.first().data(dataKey);
        }


        return contexts.each(function() {
            var inputElement = $(this),
                instance = inputElement.data(dataKey);

            if (instance && instance.dispose && instance.options.bDestroyed) {
                instance.dispose();
            }
            instance = new Combobox(this, options);
            inputElement.data(dataKey, instance);
        });
    }

})