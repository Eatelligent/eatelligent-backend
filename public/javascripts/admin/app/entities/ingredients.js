define(function(require) {
  'use strict';

  var Backbone = require('backbone');
  var channel = require('backbone.radio').channel('app');

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
});
