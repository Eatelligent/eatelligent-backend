define(function(require) {
  'use strict';

  var Marionette = require('marionette');
  var template = require('hbs!./templates/layout');
  var searchTemplate = require('hbs!./templates/search');
  var resultTemplate = require('hbs!./templates/results');
  var resultItemTemplate = require('hbs!./templates/result_item');

  var ResultEmpty = Marionette.ItemView.extend({
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

    events: {
      'keyup [data-js-search]': 'onChangeQuery'
    },

    onChangeQuery: function(e) {
      this.model.set({q: this.ui.searchInput.val()});
    },

    onRender: function() {
      if (this.model.get('q')) {
        this.model.trigger('change');
      }
    }
  });

  var TagLayout = Marionette.LayoutView.extend({
    template: template,

    initialize: function() {
      this.model.on('change', _.bind(function() {
        this.trigger('search');
      }, this));

      this.searchView = new SearchView({model: this.model});
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