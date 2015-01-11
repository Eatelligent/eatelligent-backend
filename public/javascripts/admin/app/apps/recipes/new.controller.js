define(function(require) {
  'use strict';

  var Backbone = require('backbone');
  var Ctrl = require('utils/controller');
  var Layout = require('./new.view');
  var channel = require('backbone.radio').channel('app');

  var Controller = Ctrl.extend({
    initialize: function(options) {

      var self = this;
      options = options || {};
      var model = options.model;

      var tags = channel.request('entities:collection:tags');
      var languages = channel.request('entities:collection:languages');
      var ingredients = channel.request('entities:collection:ingredients');

      channel.request('when:fetched', [tags, languages, ingredients], function() {
        self.view = self.getView(tags, languages, ingredients, model);
        self.region.show(self.view);
      });
    },

    getView: function(tags, languages, ingredients, model) {
      return new Layout({
        tags: tags,
        languages: languages,
        ingredients: ingredients,
        model: model
      });
    }
  });

  return Controller;
});