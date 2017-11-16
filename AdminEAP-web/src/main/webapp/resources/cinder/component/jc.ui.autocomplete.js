/*
 * Jc Autocomplete
 * http://www.cpcn.com.cn
 *
 * @version v1.0.0.0
 * @auther dongyan
 *
 * Date: 2016-10-10
 */
Jc().$package(function(Jc) {

	var utils = (function() {
			return {
				escapeRegExChars: function(value) {
					return value.replace(/[\-\[\]\/\{\}\(\)\*\+\?\.\\\^\$\|]/g, "\\$&");
				},
				createNode: function(containerClass) {
					var div = document.createElement('div');
					div.className = containerClass;
					div.style.position = 'absolute';
					div.style.display = 'none';
					return div;
				}
			};
		}()),

		keys = {
			ESC: 27,
			TAB: 9,
			RETURN: 13,
			LEFT: 37,
			UP: 38,
			RIGHT: 39,
			DOWN: 40
		};
	/**
	 * 构造函数
	 * @param {} el      [description]
	 * @param {} options [description]
	 */
	function Autocomplete(el, options) {
		var noop = function() {},
			that = this,
			/**
			 * 初始化参数 options
			 * @property {ajaxSettings} ajaxSettings  ajax请求参数
			 * @property {dataArray} dataArray 数组参数
			 * @property {onSelect} onSelect 选中某一项后的回调函数 function
			 *
			 * 
			 * @memberOf Jc.autocomplete.prototype
			 */
			defaults = {
				ajaxSettings: {}, //ajax请求参数
				autoSelectFirst: false,
				appendTo: document.body,
				serviceUrl: null,
				dataArray: null,
				onSelect: null,
				width: 'auto',
				minChars: 1,
				maxHeight: 300,
				deferRequestBy: 0,
				params: {},
				formatResult: Autocomplete.formatResult,
				delimiter: null,
				zIndex: 9999,
				type: 'POST',
				noCache: false,
				onSearchStart: noop,
				onSearchComplete: noop,
				onSearchError: noop,
				preserveInput: false,
				containerClass: 'autocomplete-suggestions',
				tabDisabled: false,
				dataType: 'text',
				currentRequest: null,
				triggerSelectOnValidInput: true,
				preventBadQueries: true,
				lookupFilter: function(suggestion, originalQuery, queryLowerCase) {
					return suggestion.value.toLowerCase().indexOf(queryLowerCase) !== -1;
				},
				paramName: 'query',
				transformResult: function(response) {
					var result;
					if (typeof response === 'string') {
						var jsonStr = $.parseJSON(response);
						if (that.options.naluvalue) jsonStr.suggestions.unshift({
							"data": "",
							"value": ""
						});
						result = jsonStr;
					} else {
						result = response;
					}

					return result;
				},
				showNoSuggestionNotice: false,
				noSuggestionNotice: 'No results',
				orientation: 'bottom',
				forceFixPosition: false,
				showAllSuggestionFlag: 'SHOWALLSUGGESTION',
				basedSelect: null,
				getStaticDataFromUrl: false,
				afterInit: noop,
				naluvalue: true,
				focusSearch: true
			};

		that.element = el;
		that.el = $(el);
		that.suggestions = [];
		that.badQueries = [];
		that.selectedIndex = -1;
		that.currentValue = that.element.value;
		that.intervalId = 0;
		that.cachedResponse = {};
		that.onChangeInterval = null;
		that.onChange = null;
		that.isLocal = false;
		that.suggestionsContainer = null;
		that.noSuggestionsContainer = null;
		that.options = $.extend({}, defaults, options);
		that.classes = {
			selected: 'autocomplete-selected',
			suggestion: 'autocomplete-suggestion'
		};
		that.hint = null;
		that.hintValue = '';
		that.selection = null;

		that.initialize();
		that.setOptions(options);
	}

	Autocomplete.utils = utils;

	//$.Autocomplete = Autocomplete;
	/**
	 * 格式化输出
	 *
	 * @method formatResult
	 * @memberOf Jc.autocomplete.prototype
	 * 
	 * @param  {} suggestion   
	 * @param  {} currentValue 
	 * @return {}              
	 */
	Autocomplete.formatResult = function(suggestion, currentValue) {
		if (!currentValue) {
			return suggestion.value;
		}

		var pattern = '(' + utils.escapeRegExChars(currentValue) + ')';

		return suggestion.value
			.replace(new RegExp(pattern, 'gi'), '<strong>$1<\/strong>')
			.replace(/&/g, '&amp;')
			.replace(/</g, '&lt;')
			.replace(/>/g, '&gt;')
			.replace(/"/g, '&quot;')
			.replace(/&lt;(\/?strong)&gt;/g, '<$1>');
	};

	Autocomplete.prototype = {

		killerFn: null,
		/**
		 * 初始化方法
		 * @return {} [description]
		 *
		 * @private
		 */
		initialize: function() {
			var that = this,
				suggestionSelector = '.' + that.classes.suggestion,
				selected = that.classes.selected,
				options = that.options,
				container;

			that.element.setAttribute('autocomplete', 'off');

			that.killerFn = function(e) {
				if ($(e.target).closest('.' + that.options.containerClass).length === 0) {
					that.killSuggestions();
					that.disableKillerFn();
				}
			};

			that.noSuggestionsContainer = $('<div class="autocomplete-no-suggestion"></div>')
				.html(this.options.noSuggestionNotice).get(0);

			that.suggestionsContainer = Autocomplete.utils.createNode(options.containerClass);

			container = $(that.suggestionsContainer);

			container.appendTo(options.appendTo);

			if (options.width !== 'auto') {
				container.width(options.width);
			}

			container.on('mouseover.autocomplete', suggestionSelector, function() {
				that.activate($(this).data('index'));
			});

			container.on('mouseout.autocomplete', function() {
				that.selectedIndex = -1;
				container.children('.' + selected).removeClass(selected);
			});

			container.on('click.autocomplete', suggestionSelector, function() {
				that.select($(this).data('index'));
			});

			that.fixPositionCapture = function() {
				if (that.visible) {
					that.fixPosition();
				}
			};

			$(window).on('resize.autocomplete', that.fixPositionCapture);

			that.el.on('keydown.autocomplete', function(e) {
				that.onKeyPress(e);
			});
			that.el.on('keyup.autocomplete', function(e) {
				that.onKeyUp(e);
			});
			that.el.on('blur.autocomplete', function() {
				that.onBlur();
			});
			that.el.on('focus.autocomplete', function() {
				that.onFocus();
			});
			that.el.on('change.autocomplete', function(e) {
				that.onKeyUp(e);
			});
			that.el.on('input.autocomplete', function(e) {
				that.onKeyUp(e);
			});

			options.afterInit.call(that.element);

		},

		url2DataArray: function() {
			var that = this;

			that.abortAjax();

			var ajaxSettings = {
				url: that.options.dataArray,
				data: {
					query: ''
				},
				async: false,
				type: that.options.type,
				dataType: that.options.dataType
			};

			$.extend(ajaxSettings, that.options.ajaxSettings);

			$.ajax(ajaxSettings).done(function(data) {
				var result;
				that.currentRequest = null;
				result = that.options.transformResult(data);
				that.options.dataArray = result.suggestions;
			}).fail(function(jqXHR, textStatus, errorThrown) {
				options.onSearchError.call(that.element, q, jqXHR, textStatus, errorThrown);
			});
		},

		/**
		 * 焦点事件
		 * @return {} [description]
		 */
		onFocus: function() {
			var that = this;

			that.fixPosition();

			if (that.el.val().length >= that.options.minChars && that.options.focusSearch) {
				that.onValueChange();
			}
		},


		onBlur: function() {
			this.enableKillerFn();
		},
		/**
		 * @ignore
		 * @return {} [description]
		 */
		abortAjax: function() {
			var that = this;
			if (that.currentRequest) {
				that.currentRequest.abort();
				that.currentRequest = null;
			}
		},
		/**
		 * 设置参数
		 * @param {} suppliedOptions [description]
		 */
		setOptions: function(suppliedOptions) {
			var that = this,
				options = that.options;

			$.extend(options, suppliedOptions);

			/**dataArray支持url的function */
			if (that.options.getStaticDataFromUrl) {
				that.url2DataArray();
			}

			that.isLocal = $.isArray(options.dataArray);

			if (that.isLocal) {
				options.dataArray = that.verifySuggestionsFormat(options.dataArray);
			}

			options.orientation = that.validateOrientation(options.orientation, 'bottom');

			$(that.suggestionsContainer).css({
				'max-height': options.maxHeight + 'px',
				'width': options.width + 'px',
				'z-index': options.zIndex
			});
		},


		clearCache: function() {
			this.cachedResponse = {};
			this.badQueries = [];
		},

		clear: function() {
			this.clearCache();
			this.currentValue = '';
			this.suggestions = [];
		},

		disable: function() {
			var that = this;
			that.disabled = true;
			clearInterval(that.onChangeInterval);
			that.abortAjax();
		},

		enable: function() {
			this.disabled = false;
		},

		fixPosition: function() {

			var that = this,
				$container = $(that.suggestionsContainer),
				containerParent = $container.parent().get(0);
			if (containerParent !== document.body && !that.options.forceFixPosition) {
				return;
			}

			var orientation = that.options.orientation,
				containerHeight = $container.outerHeight(),
				height = that.el.outerHeight(),
				offset = that.el.offset(),
				styles = {
					'top': offset.top,
					'left': offset.left
				};

			if (orientation === 'auto') {
				var viewPortHeight = $(window).height(),
					scrollTop = $(window).scrollTop(),
					topOverflow = -scrollTop + offset.top - containerHeight,
					bottomOverflow = scrollTop + viewPortHeight - (offset.top + height + containerHeight);

				orientation = (Math.max(topOverflow, bottomOverflow) === topOverflow) ? 'top' : 'bottom';
			}

			if (orientation === 'top') {
				styles.top += -containerHeight;
			} else {
				styles.top += height;
			}

			if (containerParent !== document.body) {
				var opacity = $container.css('opacity'),
					parentOffsetDiff;

				if (!that.visible) {
					$container.css('opacity', 0).show();
				}

				parentOffsetDiff = $container.offsetParent().offset();
				styles.top -= parentOffsetDiff.top;
				styles.left -= parentOffsetDiff.left;

				if (!that.visible) {
					$container.css('opacity', opacity).hide();
				}
			}

			if (that.options.width === 'auto') {
				styles = $.extend({}, styles, {
					'min-width':(that.el.outerWidth() - 2) + 'px',
					'width':'initial'
				});
			}else{
				styles.width = (that.el.outerWidth() - 2) + 'px';
			}

			$container.css(styles);
		},

		enableKillerFn: function() {
			var that = this;
			$(document).on('click.autocomplete', that.killerFn);
		},

		disableKillerFn: function() {
			var that = this;
			$(document).off('click.autocomplete', that.killerFn);
		},

		killSuggestions: function() {
			var that = this;
			that.stopKillSuggestions();
			that.intervalId = window.setInterval(function() {
				if (that.visible) {
					that.el.val(that.currentValue);
					that.hide();
				}

				that.stopKillSuggestions();
			}, 50);
		},

		stopKillSuggestions: function() {
			window.clearInterval(this.intervalId);
		},

		isCursorAtEnd: function() {
			var that = this,
				valLength = that.el.val().length,
				selectionStart = that.element.selectionStart,
				range;

			if (typeof selectionStart === 'number') {
				return selectionStart === valLength;
			}
			if (document.selection) {
				range = document.selection.createRange();
				range.moveStart('character', -valLength);
				return valLength === range.text.length;
			}
			return true;
		},

		onKeyPress: function(e) {
			var that = this;

			if (!that.disabled && !that.visible && e.which === keys.DOWN && that.currentValue) {
				that.suggest();
				return;
			}

			if (that.disabled || !that.visible) {
				return;
			}

			switch (e.which) {
				case keys.ESC:
					that.el.val(that.currentValue);
					that.hide();
					break;
				case keys.RIGHT:
					if (that.hint && that.options.onHint && that.isCursorAtEnd()) {
						that.selectHint();
						break;
					}
					return;
				case keys.TAB:
					if (that.hint && that.options.onHint) {
						that.selectHint();
						return;
					}
					if (that.selectedIndex === -1) {
						that.hide();
						return;
					}
					that.select(that.selectedIndex);
					if (that.options.tabDisabled === false) {
						return;
					}
					break;
				case keys.RETURN:
					if (that.selectedIndex === -1) {
						that.hide();
						return;
					}
					that.select(that.selectedIndex);
					break;
				case keys.UP:
					that.moveUp();
					break;
				case keys.DOWN:
					that.moveDown();
					break;
				default:
					return;
			}

			e.stopImmediatePropagation();
			e.preventDefault();
		},

		onKeyUp: function(e) {
			var that = this;

			if (that.disabled) {
				return;
			}

			switch (e.which) {
				case keys.UP:
				case keys.DOWN:
					return;
			}

			clearInterval(that.onChangeInterval);

			if (that.currentValue !== that.el.val()) {
				that.findBestHint();
				if (that.options.deferRequestBy > 0) {
					that.onChangeInterval = setInterval(function() {
						that.onValueChange();
					}, that.options.deferRequestBy);
				} else {
					that.onValueChange();
				}
			}
		},

		onValueChange: function() {
			var that = this,
				options = that.options,
				value = that.el.val(),
				query = that.getQuery(value);

			if (that.selection && that.currentValue !== query) {
				that.selection = null;
				(options.onInvalidateSelection || $.noop).call(that.element);
			}

			clearInterval(that.onChangeInterval);
			that.currentValue = value;
			that.selectedIndex = -1;

			if (options.triggerSelectOnValidInput && that.isExactMatch(query)) {
				that.select(0);
				return;
			}

			if (query.length == 0) {
				options.onSearchStart.call(that.element, options.params);
			}

			if (query.length < options.minChars) {
				that.hide();
			} else {
				that.getSuggestions(query);
			}
		},

		isExactMatch: function(query) {
			var suggestions = this.suggestions;

			return (suggestions.length === 1 && suggestions[0].value.toLowerCase() === query.toLowerCase());
		},

		getQuery: function(value) {
			var delimiter = this.options.delimiter,
				parts;

			if (!delimiter) {
				return value;
			}
			parts = value.split(delimiter);
			return $.trim(parts[parts.length - 1]);
		},

		getSuggestionsLocal: function(query) {
			var that = this,
				options = that.options,
				queryLowerCase = query.toLowerCase(),
				filter = options.lookupFilter,
				limit = parseInt(options.lookupLimit, 10),
				data;
			if (options.showAllSuggestionFlag !== query) {
				data = {
					suggestions: $.grep(options.dataArray, function(suggestion) {
						return filter(suggestion, query, queryLowerCase);
					})
				};
			} else {
				data = {
					suggestions: options.dataArray
				}
			}


			if (limit && data.suggestions.length > limit) {
				data.suggestions = data.suggestions.slice(0, limit);
			}

			return data;
		},

		getSuggestions: function(q) {
			var response,
				that = this,
				options = that.options,
				serviceUrl = options.serviceUrl,
				params,
				cacheKey,
				ajaxSettings;

			options.params[options.paramName] = q;
			params = options.ignoreParams ? null : options.params;

			if (options.onSearchStart.call(that.element, options.params) === false) {
				return;
			}

			if ($.isFunction(options.dataArray)) {
				options.dataArray(q, function(data) {
					that.suggestions = data.suggestions;
					that.suggest();
					options.onSearchComplete.call(that.element, q, data.suggestions);
				});
				return;
			}

			if (that.isLocal) {
				response = that.getSuggestionsLocal(q);
			} else {
				if ($.isFunction(serviceUrl)) {
					serviceUrl = serviceUrl.call(that.element, q);
				}
				cacheKey = serviceUrl + '?' + $.param(params || {});
				response = that.cachedResponse[cacheKey];
			}

			if (response && $.isArray(response.suggestions)) {
				that.suggestions = response.suggestions;
				that.suggest();
				options.onSearchComplete.call(that.element, q, response.suggestions);
			} else if (!that.isBadQuery(q)) {
				that.abortAjax();

				ajaxSettings = {
					url: serviceUrl,
					data: params,
					type: options.type,
					dataType: options.dataType
				};

				$.extend(ajaxSettings, options.ajaxSettings);

				that.currentRequest = $.ajax(ajaxSettings).done(function(data) {
					var result;
					that.currentRequest = null;
					result = options.transformResult(data, q);
					that.processResponse(result, q, cacheKey);
					options.onSearchComplete.call(that.element, q, result.suggestions);
				}).fail(function(jqXHR, textStatus, errorThrown) {
					options.onSearchError.call(that.element, q, jqXHR, textStatus, errorThrown);
				});
			} else {
				options.onSearchComplete.call(that.element, q, []);
			}
		},

		isBadQuery: function(q) {
			if (!this.options.preventBadQueries) {
				return false;
			}

			var badQueries = this.badQueries,
				i = badQueries.length;

			while (i--) {
				if (q.indexOf(badQueries[i]) === 0) {
					return true;
				}
			}

			return false;
		},

		hide: function() {
			var that = this,
				container = $(that.suggestionsContainer);

			if ($.isFunction(that.options.onHide) && that.visible) {
				that.options.onHide.call(that.element, container);
			}

			that.visible = false;
			that.selectedIndex = -1;
			clearInterval(that.onChangeInterval);
			$(that.suggestionsContainer).hide();
			that.signalHint(null);
		},

		suggest: function() {
			if (this.suggestions.length === 0) {
				if (this.options.showNoSuggestionNotice) {
					this.noSuggestions();
				} else {
					this.hide();
				}
				return;
			}

			var that = this,
				options = that.options,
				groupBy = options.groupBy,
				formatResult = options.formatResult,
				value = that.getQuery(that.currentValue),
				className = that.classes.suggestion,
				classSelected = that.classes.selected,
				container = $(that.suggestionsContainer),
				noSuggestionsContainer = $(that.noSuggestionsContainer),
				beforeRender = options.beforeRender,
				html = '',
				category,
				formatGroup = function(suggestion, index) {
					var currentCategory = suggestion.data[groupBy];

					if (category === currentCategory) {
						return '';
					}

					category = currentCategory;

					return '<div class="autocomplete-group"><strong>' + category + '</strong></div>';
				};

			if (options.triggerSelectOnValidInput && that.isExactMatch(value)) {
				that.select(0);
				return;
			}
			//html += '<div class="' + className + '" data-index="-1"> </div>';
			$.each(that.suggestions, function(i, suggestion) {
				if (groupBy) {
					html += formatGroup(suggestion, value, i);
				}

				html += '<div class="' + className + '" data-index="' + i + '">' + formatResult(suggestion, value) + '</div>';
			});

			this.adjustContainerWidth();

			noSuggestionsContainer.detach();
			container.html(html);

			if ($.isFunction(beforeRender)) {
				beforeRender.call(that.element, container);
			}

			that.fixPosition();
			container.show();

			if (options.autoSelectFirst) {
				that.selectedIndex = 0;
				container.scrollTop(0);
				container.children('.' + className).first().addClass(classSelected);
			}

			that.visible = true;
			that.findBestHint();
		},

		noSuggestions: function() {
			var that = this,
				container = $(that.suggestionsContainer),
				noSuggestionsContainer = $(that.noSuggestionsContainer);

			this.adjustContainerWidth();

			noSuggestionsContainer.detach();
			container.empty();
			container.append(noSuggestionsContainer);

			that.fixPosition();

			container.show();
			that.visible = true;
		},

		adjustContainerWidth: function() {
			var that = this,
				options = that.options,
				width,
				container = $(that.suggestionsContainer);

			if (options.width === 'auto') {
				width = that.el.outerWidth() - 2;
				container.width(width > 0 ? width : 300);
			}
		},

		findBestHint: function() {
			var that = this,
				value = that.el.val().toLowerCase(),
				bestMatch = null;

			if (!value) {
				return;
			}

			$.each(that.suggestions, function(i, suggestion) {
				var foundMatch = suggestion.value.toLowerCase().indexOf(value) === 0;
				if (foundMatch) {
					bestMatch = suggestion;
				}
				return !foundMatch;
			});

			that.signalHint(bestMatch);
		},

		signalHint: function(suggestion) {
			var hintValue = '',
				that = this;
			if (suggestion) {
				hintValue = that.currentValue + suggestion.value.substr(that.currentValue.length);
			}
			if (that.hintValue !== hintValue) {
				that.hintValue = hintValue;
				that.hint = suggestion;
				(this.options.onHint || $.noop)(hintValue);
			}
		},

		verifySuggestionsFormat: function(suggestions) {
			if (suggestions.length && typeof suggestions[0] === 'string') {
				return $.map(suggestions, function(value) {
					return {
						value: value,
						data: null
					};
				});
			}

			if (suggestions[0].data && suggestions[0].value && suggestions[0].data != '-1') {
				if (this.options.naluvalue) suggestions.unshift({
					"data": "",
					"value": ""
				});
			}

			return suggestions;
		},

		validateOrientation: function(orientation, fallback) {
			orientation = $.trim(orientation || '').toLowerCase();

			if ($.inArray(orientation, ['auto', 'bottom', 'top']) === -1) {
				orientation = fallback;
			}

			return orientation;
		},

		processResponse: function(result, originalQuery, cacheKey) {
			var that = this,
				options = that.options,
				filter = options.lookupFilter;

			result.suggestions = that.verifySuggestionsFormat(result.suggestions);

			if (!options.noCache) {
				that.cachedResponse[cacheKey] = result;
				if (options.preventBadQueries && result.suggestions.length === 0) {
					that.badQueries.push(originalQuery);
				}
			}

			if (originalQuery !== that.getQuery(that.currentValue) && options.showAllSuggestionFlag !== originalQuery) {
				return;
			}

			if (options.showAllSuggestionFlag === originalQuery) {
				that.suggestions = result.suggestions;
			} else {
				that.suggestions = $.grep(result.suggestions, function(suggestion) {
					return filter(suggestion, originalQuery, originalQuery.toLowerCase());
				});
			}

			that.suggest();
		},

		activate: function(index) {
			var that = this,
				activeItem,
				selected = that.classes.selected,
				container = $(that.suggestionsContainer),
				children = container.find('.' + that.classes.suggestion);

			container.find('.' + selected).removeClass(selected);

			that.selectedIndex = index;

			if (that.selectedIndex !== -1 && children.length > that.selectedIndex) {
				activeItem = children.get(that.selectedIndex);
				$(activeItem).addClass(selected);
				return activeItem;
			}

			return null;
		},

		selectHint: function() {
			var that = this,
				i = $.inArray(that.hint, that.suggestions);

			that.select(i);
		},

		select: function(i) {
			var that = this;
			that.hide();
			that.onSelect(i);
		},

		moveUp: function() {
			var that = this;

			if (that.selectedIndex === -1) {
				return;
			}

			if (that.selectedIndex === 0) {
				$(that.suggestionsContainer).children().first().removeClass(that.classes.selected);
				that.selectedIndex = -1;
				that.el.val(that.currentValue);
				that.findBestHint();
				return;
			}

			that.adjustScroll(that.selectedIndex - 1);
		},

		moveDown: function() {
			var that = this;

			if (that.selectedIndex === (that.suggestions.length - 1)) {
				return;
			}

			that.adjustScroll(that.selectedIndex + 1);
		},

		adjustScroll: function(index) {
			var that = this,
				activeItem = that.activate(index);

			if (!activeItem) {
				return;
			}

			var offsetTop,
				upperBound,
				lowerBound,
				heightDelta = $(activeItem).outerHeight();

			offsetTop = activeItem.offsetTop;
			upperBound = $(that.suggestionsContainer).scrollTop();
			lowerBound = upperBound + that.options.maxHeight - heightDelta;

			if (offsetTop < upperBound) {
				$(that.suggestionsContainer).scrollTop(offsetTop);
			} else if (offsetTop > lowerBound) {
				$(that.suggestionsContainer).scrollTop(offsetTop - that.options.maxHeight + heightDelta);
			}

			if (!that.options.preserveInput) {
				that.el.val(that.getValue(that.suggestions[index].value));
			}
			that.signalHint(null);
		},

		onSelect: function(index) {
			var that = this,
				onSelectCallback = that.options.onSelect,
				suggestion = that.suggestions[index];

			/**
			if(index == '-1'){
				suggestion = {"data":"","value":""}
			}else{
				suggestion = that.suggestions[index];
			}
			*/

			that.currentValue = that.getValue(suggestion.value);

			if (that.currentValue !== that.el.val() && !that.options.preserveInput) {
				that.el.val(that.currentValue);
			}

			that.signalHint(null);
			that.suggestions = [];
			that.selection = suggestion;

			if ($.isFunction(onSelectCallback)) {
				onSelectCallback.call(that.options.basedSelect || that.element, suggestion);
			}
		},

		getValue: function(value) {
			var that = this,
				delimiter = that.options.delimiter,
				currentValue,
				parts;

			if (!delimiter) {
				return value;
			}

			currentValue = that.currentValue;
			parts = currentValue.split(delimiter);

			if (parts.length === 1) {
				return value;
			}

			return currentValue.substr(0, currentValue.length - parts[parts.length - 1].length) + value;
		},

		dispose: function() {
			var that = this;
			that.el.off('.autocomplete').removeData('autocomplete');
			that.disableKillerFn();
			$(window).off('resize.autocomplete', that.fixPositionCapture);
			$(that.suggestionsContainer).remove();
		},

		showAllSuggestion: function() {
			var that = this,
				query = 'SHOWALLSUGGESTION';
			that.disableKillerFn();
			that.getSuggestions(query);
		},

		wasOpened: function() {
			var that = this;
			return $(that.suggestionsContainer).is(':visible');
		}

	}

	/**
	 * @class
	 * @constructor Jc.autocomplete
	 * 
	 * 
	 * @param  {obj} contexts 需要初始化autocomplete的jQuery对象
	 * @param  {obj} options  初始化时传递的参数
	 * @param  {obj} args     预留参数位
	 * @return {obj}  
	 *
	 * @example
	 * var testData = [
	 *    { value: '测试', data: 'AD' },
	 *    // ...
	 *    { value: '中金支付', data: 'ZZ' }
	 * ];
	 *
	 * Jc().autocomplete($('#autocomplete'),{
	 *   dataArray: testData,
	 *   onSelect: function (suggestion) {
	 *       alert(suggestion.value + ', ' + suggestion.data);
	 *   }
	 * });
	 * 
	 */
	Jc.autocomplete = function(contexts, options, args) {
		var dataKey = 'Jc.autocomplete';
		if (arguments.length === 1) {
			return contexts.first().data(dataKey);
		}

		return contexts.each(function() {
			var inputElement = $(this),
				instance = inputElement.data(dataKey);

			if (typeof options === 'string') {
				if (instance && typeof instance[options] === 'function') {
					instance[options](args);
				}
			} else {
				if (instance && instance.dispose) {
					instance.dispose();
				}
				instance = new Autocomplete(this, options);
				inputElement.data(dataKey, instance);
			}
		});
	}

});