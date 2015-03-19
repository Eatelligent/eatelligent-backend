define(function(require) {
  'use strict';

  var Marionette = require('marionette');
  var template = require('hbs!./templates/ingredients');
  var ingredientTemplate = require('hbs!./templates/ingredient');

  var TagItem = Marionette.ItemView.extend({
    template: _.template('<%- name %>'),
    tagName: 'option',
    className: function() {
      return this.model.get('name')
    },

    initialize: function(options) {
      this.selected = options.selected;
    },

    onShow: function() {
      if (this.selected) {
        this.$el.attr('selected', 'selected');
      }
    }
  });

  var TagSelectionItem = Marionette.CompositeView.extend({
    template: _.template('<select class="items"></select>'),
    childView: TagItem,
    childViewContainer: '.items',
    className: 'display-inline',

    buildChildView: function(model) {
      if(this.model.get('name') === model.get('name')) {
        return new TagItem({model: model, selected: true});
      }
      return new TagItem({model: model})
    },

    onShow: function() {
      this.$el.on('change', _.bind(function(e) {
        this.trigger('tag:selected', $(e.target.selectedOptions).attr('class'));
      }, this));
    },


    initialize: function() {
      this.listenTo(this, 'tag:selected', function(name) {
        if(name === '--') {
          this.trigger('remove:tag', this.model.get('prevstate'));
          this.destroy();
        } else {
          this.trigger('add:tag', name);
          this.trigger('add:new:tag:selector')
        }
        this.model.set('prevstate', name);
      });

      this.model.set('prevstate', this.model.get('name') || undefined);
    }
  });

  var IngredientItem = Marionette.CompositeView.extend({
    template: ingredientTemplate,
    childView: TagSelectionItem,
    initialize: function(options) {
      this.tags = options.tags;
      this._counter = 0;
      this.collection = new Backbone.Collection([{id: ++this._counter}]);

      this.on('childview:remove:tag', function(child, name) {
        var t = _.without(this.model.get('tags') || [], name);
        this.model.set('tags', t);
        this.model.save();
      });

      this.on('childview:add:tag', function(child, name) {
        var prevtags = (this.model.get('tags') || []).filter(function(x) { x != null });
        prevtags.push(name);
        this.model.set('tags', prevtags);
        this.model.save();
      })

      this.on('childview:add:new:tag:selector', function(tag) {
        this.collection.add({id: ++this._counter, name: tag});
      });

      _.each(this.model.get('tags'), _.bind(function(tag) {
        this.trigger('childview:add:new:tag:selector', tag)
      }, this));
    },
    buildChildView: function(m) {
      return new TagSelectionItem({model: m, collection: this.tags})
    },
    tagName: 'tr',
    childViewContainer: '.tags',
  });

  var IngredientsView = Marionette.CompositeView.extend({
    template: template,
    childView: IngredientItem,
    childViewContainer: '[data-js-ingredients]',
    initialize: function(options) {
      this._tags = options.tags;
    },
    buildChildView: function(model) {
      return new IngredientItem({model: model, tags: this._tags});
    }
  });

  return IngredientsView;
});