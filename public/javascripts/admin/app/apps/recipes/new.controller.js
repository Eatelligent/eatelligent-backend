define(function(require) {
  'use strict';

  var Backbone = require('backbone');
  var Ctrl = require('utils/controller');
  var Layout = require('./new.view');
  var channel = require('backbone.radio').channel('app');

  var Controller = Ctrl.extend({
    initialize: function(options) {
      var self = this;

      var tags = channel.request('entities:collection:tags');
      var languages = channel.request('entities:collection:languages');
      var ingredients = channel.request('entities:collection:ingredients');

      channel.request('when:fetched', [tags, languages, ingredients], function() {
        self.view = self.getView(tags, languages, ingredients);
        self.region.show(self.view);
      });
    },

    getView: function(tags, languages, ingredients) {
      return new Layout({
        tags: tags,
        languages: languages,
        ingredients: ingredients
      });
    }
  });

  return Controller;
});