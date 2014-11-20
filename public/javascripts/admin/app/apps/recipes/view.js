define(function(require) {
  'use strict';

  var Marionette = require('marionette');
  var template = require('hbs!./templates/recipes');
  var recipeTemplate = require('hbs!./templates/recipe');
  var recipeIngredientTemplate = require('hbs!./templates/recipe_ingredient');
  var recipeItemTemplate = require('hbs!./templates/recipe_item');

  var RecipeIngredientEmpty = Marionette.ItemView.extend({
    tagName: 'tr',
    template: _.template('<td colspan="3" class="text-center">No ingredients</td>')
  });

  var RecipeIngredient = Marionette.ItemView.extend({
    tagName: 'tr',
    template: recipeIngredientTemplate
  });

  var RecipeView = Marionette.CompositeView.extend({
    template: recipeTemplate,
    childView: RecipeIngredient,
    childViewContainer: '[data-js-ingredients]',
    emptyView: RecipeIngredientEmpty,

    initialize: function() {
      this.collection = new Backbone.Collection(this.model.get('ingredients'));
    }
  });

  var RecipeItem = Marionette.ItemView.extend({
    template: recipeItemTemplate,
    tagName: 'tr',

    triggers: {
      'click': 'recipe:clicked'
    }
  });

  var ListView = Marionette.CompositeView.extend({
    template: template,
    childView: RecipeItem,
    childViewContainer: '[data-js-recipes]'
  });

  return {
    ListView: ListView,
    RecipeView: RecipeView
  };
});