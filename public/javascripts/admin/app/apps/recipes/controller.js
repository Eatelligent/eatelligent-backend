define(function(require) {
  'use strict';

  var Backbone = require('backbone');
  var Ctrl = require('utils/controller');
  var Views = require('./view');
  var channel = require('backbone.radio').channel('app');

  var Controller = Ctrl.extend({
    initialize: function(options) {
      var self = this;
 
      if(options.id) {
        var recipe = channel.request('entities:model:recipe', options.id);

        channel.request('when:fetched', recipe, function() {
          self.showRecipeView(recipe);
        });

      } else {
        var recipes = channel.request('entities:collection:recipes');
        this.view = this.getListView(recipes);

        this.view.on('childview:recipe:clicked', function(child, obj) {
          Backbone.history.navigate('recipes/'+obj.model.get('id'), {trigger: true});
        });

        this.region.show(this.view);
      }
    },

    getListView: function(collection) {
      return new Views.ListView({collection: collection});
    },

    showRecipeView: function(model) {
      var view = new Views.RecipeView({model: model});
      this.region.show(view);
    }
  });

  return Controller;
});