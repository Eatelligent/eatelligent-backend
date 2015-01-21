define(function(require) {
  'use strict';

  var Marionette = require('marionette');
  var template = require('hbs!./templates/recipes');
  var recipeTemplate = require('hbs!./templates/recipe');
  var recipeIngredientTemplate = require('hbs!./templates/recipe_ingredient');
  var recipeItemTemplate = require('hbs!./templates/recipe_item');
  var Dropzone = require('dropzone');

  var RecipeIngredientEmpty = Marionette.ItemView.extend({
    tagName: 'tr',
    template: _.template('<td colspan="3" class="text-center">No ingredients</td>')
  });

  var RecipeIngredient = Marionette.ItemView.extend({
    tagName: 'tr',
    template: recipeIngredientTemplate,
    triggers: {
      'click [data-js-delete]': 'destroy'
    }
  });

  var RecipeView = Marionette.CompositeView.extend({
    template: recipeTemplate,
    childView: RecipeIngredient,
    childViewContainer: '[data-js-ingredients]',
    emptyView: RecipeIngredientEmpty,

    initialize: function() {
      this.collection = new Backbone.Collection(this.model.get('ingredients'));
    },

    triggers: {
      'click [data-js-edit]': 'edit:clicked',
      'click [data-js-publish]': 'publish:clicked',
      'click [data-js-imageupload]': 'imageupload:clicked'
    },

    onPublishClicked: function() {
      this.model.set('publish', true);
      this.model.save({success: function() {
        $('[data-js-publish]').hide();
      }});
    },

    onImageuploadClicked: function() {
      $('[data-js-imageupload]').hide();
      $('.hidden').removeClass('hidden');
    },

    onShow: function() {
      new Dropzone('#recipe-image-upload', {
        url: 'api/recipes/'+this.model.get('id'),
        paramName: 'image',
        maxFiles: 1,
        success: function() {
          window.location.reload();
        }
      });
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
    childViewContainer: '[data-js-recipes]',
    triggers: {
      'click [data-js-show-published]': 'show:published'
    },

    onShowPublished: function() {
      if(this.collection.url === '/api/recipes') {
        this.collection.url = '/api/recipes?published=false&deleted=false';
        $('[data-js-show-published]', this.$el).html('Show published');
        $('.header-status', this.$el).html('(unpublished)');
      } else {
        this.collection.url = '/api/recipes';
        $('[data-js-show-published]', this.$el).html('Show unpublished');
        $('.header-status', this.$el).html('(published)');
      }

      this.collection.fetch({reset: true});
    }
  });

  return {
    ListView: ListView,
    RecipeView: RecipeView
  };
});