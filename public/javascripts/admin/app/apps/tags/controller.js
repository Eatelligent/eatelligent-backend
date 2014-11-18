define(function(require) {
  'use strict';

  var Ctrl = require('utils/controller');
  var TagView = require('./view');
  var channel = require('backbone.radio').channel('app');

  var Controller = Ctrl.extend({
    initialize: function() {
      this.view = this.getView();

      this.view.on('search', function(query) {
        var recipes = channel.request('entities:collection:tags:recipe', query);
        this.showResults(recipes);
      });

      this.region.show(this.view);
    },

    getView: function() {
      return new TagView();
    }
  });

  return Controller;
});