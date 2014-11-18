define(function(require) {
  'use strict';

  var Marionette = require('marionette');
  var template = require('hbs!./templates/ingredients');
  var ingredientTemplate = require('hbs!./templates/ingredient');

  var IngredientItem = Marionette.ItemView.extend({
    template: ingredientTemplate,
    tagName: 'tr'
  });

  var IngredientsView = Marionette.CompositeView.extend({
    template: template,
    childView: IngredientItem,
    childViewContainer: '[data-js-ingredients]'
  });

  return IngredientsView;
});