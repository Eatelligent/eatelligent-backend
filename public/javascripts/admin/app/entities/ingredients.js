define(function(require) {
  'use strict';

  var Backbone = require('backbone');
  var channel = require('backbone.radio').channel('app');

  var IngTags = Backbone.Collection.extend({
    url: '/api/ingredients/tags',
    parse: function(response) {
      return response.ingredient_tags;
    }
  });

  var Ingredients = Backbone.Collection.extend({
    url: '/api/ingredients',
    parse: function(response) {
      return response.ingredients;
    }
  });

  channel.reply('entities:collection:ingredients', function() {
    var ingredients = new Ingredients();
    ingredients.fetch({reset: true});
    return ingredients;
  });

  channel.reply('entities:collection:ingredient:tags', function() {
    var tags = new IngTags();
    tags.fetch({reset: true, success: function() {
      tags.add({id: 0, name: '--'}, {at: 0})
    }});
    return tags;
  });
});
