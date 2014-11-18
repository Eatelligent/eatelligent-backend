define(function(require) {
  'use strict';

  var Ctrl = require('utils/controller');
  var IngredientsView = require('./view');
  var channel = require('backbone.radio').channel('app');

  var Controller = Ctrl.extend({
    initialize: function() {
      var ingredients = channel.request('entities:collection:ingredients');

      this.view = this.getView(ingredients);
      this.region.show(this.view);
    },

    getView: function(collection) {
      return new IngredientsView({collection: collection});
    }
  });

  return Controller;
});