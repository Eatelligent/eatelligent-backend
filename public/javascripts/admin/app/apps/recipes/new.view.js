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
  var difficultyTemplate = require('hbs!./templates/new/difficulty');
  var ingredientTemplate = require('hbs!./templates/new/ingredients');
  var ingredientItemTemplate = require('hbs!./templates/new/ingredients_item');
  var sourceTemplate = require('hbs!./templates/new/source');

  var Bloodhound = require('bloodhound');
  var Typeahead = require('typeahead');

  var channel = require('backbone.radio').channel('app');

  require('backbone.stickit');
  require('summernote');

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
          return _.map(value.split(','), function(t) { return t.trim(); });
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
      $('[data-js-time]', this.$el).removeClass('btn-primary');
      $(e.target).addClass('btn-primary');
      var val = parseInt($(e.target).attr('value'));
      this.model.set('time', val);
    },

    getTimes: function(n) {
      var minutes = function(i) {
        var min = 10;
        return min + i * 10;
      };

      var hours = function(minutes) {
        if (minutes < 60) return minutes+"m";
        var h = parseInt(minutes / 60);
        return h +"t "+ (minutes - h * 60)+"m";
      };

      var array = [];
      _(n).times(function(a) {
        array.push({minutes: minutes(a), hourmins: hours(minutes(a))});
      });
      return array;
    },

    serializeData: function() {
      return {
        times: this.getTimes(18)
      };
    },

    onShow: function() {
      var time = this.model.get('time');
      if (time) {
        $('[value='+time+']').click();
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
        this.$el.attr('value', this.model.get('id'));
      }
    })
  });

  var DescriptionView = Marionette.ItemView.extend({
    template: descriptionTemplate,
    className: 'col-md-12 recipe-parameter',
    bindings: {
      '[data-js-description]': 'description'
    },

    onRender: function() { this.stickit(); }
  });

  var ProcedureView = Marionette.ItemView.extend({
    template: procedureTemplate,
    className: 'col-md-12 recipe-parameter',

    onShow: function() {
      var self = this;
      $('[data-js-procedure]', this.$el).summernote({
        onChange: function() {
          var procedure = $('[data-js-procedure]', self.$el).code();
          self.model.set('procedure', procedure);
        },
        height: 150
      });
    }
  });

  var StrengthView = Marionette.ItemView.extend({
    template: strengthTemplate,
    className: 'col-md-12 recipe-parameter',

    serializeData: function() {
      return {
        levels: [1, 2, 3]
      };
    },

    events: {
      'click [data-js-strength]': 'updateModel'
    },

    updateModel: function(e) {
      $('[data-js-strength]', this.$el).removeClass('btn-primary');
      $(e.target, this.$el).addClass('btn-primary');
      var val = parseInt($(e.target).attr('value'), 10);
      this.model.set('spicy', val);
    },
    onRender: function() {
      $('[value='+this.model.get('spicy')+']', this.$el).addClass('btn-primary');
    }
  });

  var DifficultyView = Marionette.ItemView.extend({
    template: difficultyTemplate,
    className: 'col-md-12 recipe-parameter',

    serializeData: function() {
      return {
        levels: ['Enkel', 'Middels', 'Vanskelig']
      }
    },

    events: {
      'click [data-js-difficulty]': 'updateModel'
    },

    updateModel: function(e) {
      $('[data-js-difficulty]', this.$el).removeClass('btn-primary');
      $(e.target, this.$el).addClass('btn-primary');
      var val = $(e.target).attr('value');
      this.model.set('difficulty', val);
    },

    onRender: function() {
      $('[value='+this.model.get('difficulty')+']', this.$el).addClass('btn-primary');
    }

  });

  var _IngredientItem = Marionette.ItemView.extend({
    template: ingredientItemTemplate,
    ui: {
      name: '.typeahead'
    },

    bindings: {
      '.typeahead': {
        observe: 'name',
        onSet: function() {
          return $('.typeahead', this.$el).typeahead('val');
        }
      },
      '[data-js-amount]': {
        observe: 'amount',
        onSet: function(v) {
          return parseInt(v, 10);
        }
      },
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
          return Bloodhound.tokenizers.whitespace(d.name);
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

      if(this.model.get('ingredients')) {
        var ingredients = this.model.get('ingredients').map(function(ing) {
          return {name: ing.name, image: ing.image, amount: ing.amount, unit: ing.unit};
        });
        this.collection = new Backbone.Collection(ingredients);
      } else {
        this.collection = new Backbone.Collection([{amount: 0, unit: 'gram'}]);
      }
      var model = this.model.set('ingredients', this.collection);
      this.collection.on('all', function() {
        model.trigger('change');
      });
    },

    onAddIngredient: function() {
      this.collection.push({amount: 0, unit: 'gram'});
    }
  });

  var SourceView = Marionette.ItemView.extend({
    template: sourceTemplate,
    className: 'col-md-12 recipe-parameter',

    bindings: {
      '[data-js-source-url]': 'source'
    },

    onRender: function() {
      this.stickit();
    }
  });

  var ResultView = Marionette.ItemView.extend({
    template: _.template('<pre class="mb3"><%- json %></pre><button class="btn btn-lg btn-success mb3">Save recipe</button>'),
    serializeData: function() {
      return {
        json: JSON.stringify(this.model.toJSON(), null, '\t')
      };
    },
    triggers: {
      'click button': 'save:clicked'
    },
    onSaveClicked: function() {
      this.model.set('published', !!this.model.get('published'));
      this.model.save();
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
      difficulty: '[data-js-difficulty]',
      ingredients: '[data-js-ingredients]',
      source: '[data-js-source]',
      result: '[data-js-result]'
    },

    initialize: function(options) {
      this.languagesCollection = options.languages;
      this.ingredientsCollection = options.ingredients;
      this.tagsCollection = options.tags;

      if(options.model) {
        this.model = options.model;
      } else {
        this.model = channel.request('model:new:recipe');
        this.model.set('language', this.languagesCollection.where({name: 'NO-no'})[0].get('id'));
      }

      this.listenTo(this.model, 'change', function() {
        this.onShowJson();
      });

      this.listenTo(this.model, 'sync', function() {
        // Backbone.history.navigate('recipes/'+this.model.get('recipe').id, {trigger: true});
        window.history.back();
      });
    },

    events: {
      'change [data-js-languages]': function(e) {
        this.model.set('language', parseInt($(e.target).val()));
      }
    },

    triggers: {
      'click [data-js-json]': 'show:json'
    },

    onShowJson: function() {
      this.result.show(new ResultView({model: this.model}));
    },

    onShow: function() {
      if(!this.model.isNew()) {
        $('h1', this.$el).html('Edit recipe');
      }

      this.name.show(new NameView({model: this.model}));
      this.tags.show(new TagsView({model: this.model}));
      this.time.show(new TimeView({model: this.model}));
      this.language.show(new LanguageView({model: this.model, collection: this.languagesCollection}));
      this.description.show(new DescriptionView({model: this.model}));
      this.procedure.show(new ProcedureView({model: this.model}));
      this.strength.show(new StrengthView({model: this.model}));
      this.difficulty.show(new DifficultyView({model: this.model}));
      this.ingredients.show(new IngredientView({model: this.model, ingredients: this.ingredientsCollection}));
      this.source.show(new SourceView({model: this.model}));
    }
  });

  return Layout;
});