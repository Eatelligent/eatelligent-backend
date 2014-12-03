define(function(require) {
  'use strict';

  var Ctrl = require('utils/controller');
  var TagView = require('./view');
  var channel = require('backbone.radio').channel('app');

  var Controller = Ctrl.extend({
    initialize: function(options) {
      var searchmodel = new Backbone.Model(options);
      this.view = this.getView(searchmodel);

      this.view.on('all', function() {
        console.log(arguments);
      })

      this.view.on('search', function() {
        var query = searchmodel.get('q');
        var recipes = channel.request('entities:collection:tags:recipe', query);
        this.showResults(recipes);

        Backbone.history.navigate('tags'+ (query !== '' ? '/' + query : ''));
      });

      this.region.show(this.view);
    },

    getView: function(searchmodel) {
      return new TagView({model: searchmodel});
    }
  });

  return Controller;
});