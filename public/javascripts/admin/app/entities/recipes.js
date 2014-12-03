define(function(require) {
  'use strict';

  var Backbone = require('backbone');
  var channel = require('backbone.radio').channel('app');

  var Recipe = Backbone.Model.extend({
    urlRoot: '/api/recipes/',

    initialize: function(options) {
      this.id = options.id || 0;
    },

    parse: function(response) {
      return response.recipe || response;
    }
  });

  var Recipes = Backbone.Collection.extend({
    model: Recipe,

    url: '/api/recipes',
    
    parse: function(response) {
      return response.recipes;
    }
  });

  channel.reply('entities:collection:recipes', function() {
    var recipes = new Recipes();
    recipes.fetch({reset: true});
    return recipes;
  });

  channel.reply('entities:model:recipe', function(id) {
    var recipe = new Recipe({id: id});
    recipe.fetch({reset: true});
    return recipe;
  });
});
