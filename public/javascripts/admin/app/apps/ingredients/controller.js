define(function(require) {
  'use strict';

  var Ctrl = require('utils/controller');
  var IngredientsView = require('./view');
  var channel = require('backbone.radio').channel('app');

  var Controller = Ctrl.extend({
    initialize: function(options) {
      var ingredients = channel.request('entities:collection:ingredients');
      var tags = channel.request('entities:collection:ingredient:tags');

      this.view = this.getView(ingredients, tags);
      this.region.show(this.view);
    },

    getView: function(ingredients, tags) {
      return new IngredientsView({collection: ingredients, tags: tags});
    }
  });

  return Controller;
});