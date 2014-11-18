define(function(require) {
  'use strict';

  var Marionette = require('marionette');
  var template = require('hbs!./templates/layout');
  var searchTemplate = require('hbs!./templates/search');
  var resultTemplate = require('hbs!./templates/results');
  var resultItemTemplate = require('hbs!./templates/result_item');

  var ResultEmpty = Marionette.ItemView.extend({
    initialize: function() {
      console.log('emp', this);
    },
    template: _.template('<td colspan="2" class="text-center">No results</td>'),
    tagName: 'tr'
  });

  var ResultItem = Marionette.ItemView.extend({
    template: resultItemTemplate,
    tagName: 'tr'
  });

  var ResultView = Marionette.CompositeView.extend({
    template: resultTemplate,
    childView: ResultItem,
    childViewContainer: '[data-js-result]',
    emptyView: ResultEmpty
  });

  var SearchView = Marionette.ItemView.extend({
    template: searchTemplate,
    ui: {
      searchInput: '[data-js-search]'
    },

    triggers: {
      'keyup [data-js-search]': 'search'
    }
  });

  var TagLayout = Marionette.LayoutView.extend({
    template: template,

    initialize: function() {
      this.searchView = new SearchView();
      this.searchView.on('search', _.bind(function(obj) {
        this.trigger('search', obj.view.ui.searchInput.val());
      }, this));
    },

    regions: {
      searchRegion: '[data-js-search-region]',
      resultRegion: '[data-js-result-region]'
    },

    showResults: function(collection) {
      this.resultRegion.show(new ResultView({collection: collection}));
    },

    onShow: function() {
      this.searchRegion.show(this.searchView);
    }
  });

  return TagLayout;
});