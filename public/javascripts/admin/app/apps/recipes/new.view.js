define(function(require) {
  'use strict';

  var _ = require('underscore');
  var Backbone = require('backbone');
  var Marionette = require('marionette');

  var template = require('hbs!./templates/new/layout');
  var nameTemplate = require('hbs!./templates/new/name');
  var tagsTemplate = require('hbs!./templates/new/tags');
  var timeTemplate = require('hbs!./templates/new/time');
  var languageTemplate = require('hbs!./templates/new/language');
  var descriptionTemplate = require('hbs!./templates/new/description');
  var procedureTemplate = require('hbs!./templates/new/procedure');
  var strengthTemplate = require('hbs!./templates/new/strength');
  var ingredientTemplate = require('hbs!./templates/new/ingredients');
  var ingredientItemTemplate = require('hbs!./templates/new/ingredients_item')
  var authorTemplate = require('hbs!./templates/new/author');

  var Bloodhound = require('bloodhound');
  var Typeahead = require('typeahead');

  require('backbone.stickit');

  var NameView = Marionette.ItemView.extend({
    template: nameTemplate,
    className: 'col-md-12 recipe-parameter',

    bindings: {
      '[data-js-name]': 'name'
    },

    onRender: function() {
      this.stickit();
    }
  });

  var TagsView = Marionette.ItemView.extend({
    template: tagsTemplate,
    className: 'col-md-12 recipe-parameter',
    bindings: {
      '[data-js-tags]': {
        observe: 'tags',
        onSet: function(value) {
          return _.map(value.split(','), function(t) { return t.trim() });
        }
      }
    },

    onRender: function() {
      // TODO: autocomplete tags
      this.stickit();
    }
  });

  var TimeView = Marionette.ItemView.extend({
    template: timeTemplate,
    className: 'col-md-12 recipe-parameter',

    events: {
      'click [data-js-time]': 'updateModel'
    },

    updateModel: function(e) {
      var val = $(e.target).attr('value');
      this.model.set('time', val);
    },
    
    getTimes: function(n) {
      var minutes = function(i) {
        var min = 10;
        return min + i * 10;
      }

      var hours = function(minutes) {
        if (minutes < 60) return minutes+"m";
        var h = parseInt(minutes / 60);
        return h +"t "+ (minutes - h * 60)+"m"
      }

      var array = [];
      _(n).times(function(a) {
        array.push({minutes: minutes(a), hourmins: hours(minutes(a))})
      })
      return array;
    },

    serializeData: function() {
      return {
        times: this.getTimes(18)
      }
    }
  });
  
  var LanguageView = Marionette.CompositeView.extend({
    template: languageTemplate,
    className: 'col-md-12 recipe-parameter',
    childViewContainer: '[data-js-languages]',
    childView: Marionette.ItemView.extend({
      tagName: 'option',
      template: _.template('<%- name %>'),
      onShow: function() {
        this.$el.attr('value', this.model.get('id'))
      },
    })

    // TODO: bind to mainmodel
  });
  
  var DescriptionView = Marionette.ItemView.extend({
    template: descriptionTemplate,
    className: 'col-md-12 recipe-parameter',

    bindings: {
      '[data-js-description]': 'description'
    },

    onRender: function() {
      this.stickit();
    }
  });

  var ProcedureView = Marionette.ItemView.extend({
    template: procedureTemplate,
    className: 'col-md-12 recipe-parameter',

    bindings: {
      '[data-js-procedure]': 'procedure'
    },

    onRender: function() {
      this.stickit();
    }
  });
  
  var StrengthView = Marionette.ItemView.extend({
    template: strengthTemplate,
    className: 'col-md-12 recipe-parameter',

    serializeData: function() {
      return { levels: [1, 2, 3] }
    },
  
    events: {
      'click [data-js-strength]': 'updateModel'
    },

    updateModel: function(e) {
      var val = parseInt($(e.target).attr('value'), 10);
      this.model.set('strength', val);
    }
  });

  var _IngredientItem = Marionette.ItemView.extend({
    template: ingredientItemTemplate,
    ui: {
      name: '.typeahead'
    },

    bindings: {
      '[data-js-amount]': 'amount',
      '[data-js-unit]': 'unit'
    },

    triggers: {
      'click [data-js-delete]': 'close'
    },

    initialize: function(options) {
      this.engine = new Bloodhound({
        name: 'ingredients',
        local: options.ingredients.toJSON(),
        datumTokenizer: function(d) {
          return Bloodhound.tokenizers.whitespace(d.name)
        },
        queryTokenizer: Bloodhound.tokenizers.whitespace
      });

      this.engine.initialize();
    },

    onClose: function() {
      this.model.destroy();
    },

    onRender: function() {
      var self = this;
      this.ui.name.typeahead({
        minLength: 1,
        highlight: true,
        hint: true
      },{
        displayKey: 'name',
        source: this.engine.ttAdapter()
      }).on('typeahead:selected', function(e, element) {
        self.model.set('name', element.name);
      }).on('typeahead:autocompleted', function(e, element) {
        self.model.set('name', element.name);
      });

      this.stickit();
    }
  });
  
  var IngredientView = Marionette.CompositeView.extend({
    template: ingredientTemplate,
    className: 'col-md-12 recipe-parameter',
    childView: _IngredientItem,
    childViewContainer: '[data-js-ingredient-items]',

    triggers: {
      'click [data-js-add]': 'add:ingredient'
    },

    initialize: function(options) {
      this.childViewOptions = {ingredients: options.ingredients};
      this.collection = new Backbone.Collection([{amount: 0, unit: 'gram'}]);
      this.model.set('ingredients', this.collection);
    },

    onAddIngredient: function() {
      this.collection.push({amount: 0, unit: 'gram'});
    }
  });

  var AuthorView = Marionette.ItemView.extend({
    template: authorTemplate,
    className: 'col-md-12 recipe-parameter',

    initialize: function() {
      // TODO: Select another user?
      this.model.set('createdBy', {id: 1});
    }
  });

  var ResultView = Marionette.ItemView.extend({
    template: _.template('<pre><%- json %></pre>'),
    serializeData: function() {
      return {
        json: JSON.stringify(this.model.toJSON(), null, '\t')
      }
    }
  });

  var Layout = Marionette.LayoutView.extend({
    template: template,
    regions: {
      name: '[data-js-name]', 
      tags: '[data-js-tags]', 
      time: '[data-js-time]', 
      language: '[data-js-language]', 
      description: '[data-js-description]', 
      procedure: '[data-js-procedure]', 
      strength: '[data-js-strength]', 
      ingredients: '[data-js-ingredients]', 
      author: '[data-js-author]', 
      result: '[data-js-result]'
    },

    initialize: function(options) {
      this.languagesCollection = options.languages;
      this.ingredientsCollection = options.ingredients;
      this.tagsCollection = options.tags;
      this.model = new Backbone.Model();
    },

    triggers: {
      'click [data-js-json]': 'show:json'
    },

    onShowJson: function() {
      this.result.show(new ResultView({model: this.model}));
    },

    onShow: function() {
      this.name.show(new NameView({model: this.model}));
      this.tags.show(new TagsView({model: this.model}));
      this.time.show(new TimeView({model: this.model}));
      this.language.show(new LanguageView({model: this.model, collection: this.languagesCollection}));
      this.description.show(new DescriptionView({model: this.model}));
      this.procedure.show(new ProcedureView({model: this.model}));
      this.strength.show(new StrengthView({model: this.model}));
      this.ingredients.show(new IngredientView({model: this.model, ingredients: this.ingredientsCollection}));
      this.author.show(new AuthorView({model: this.model}));
    }
  });

  return Layout;
});