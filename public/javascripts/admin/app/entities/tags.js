define(function(require) {
  'use strict';

  var Backbone = require('backbone');
  var channel = require('backbone.radio').channel('app');

  var Recipes = Backbone.Collection.extend({
    initialize: function(models, options) {
      this.q = options.q;
    },
    
    url: function() {
      return '/api/tags/'+this.q; 
    },

    parse: function(response) {
      return response.recipes;
    }
  });

  channel.reply('entities:collection:tags:recipe', function(query) {
    var recipes = new Recipes(null, {q: query});
    recipes.fetch({reset: true});
    return recipes;
  });
});
