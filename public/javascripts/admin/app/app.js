define(function(require) {
  'use strict';

  var $ = require('jquery');
  var Marionette = require('marionette');

  var regions = require('regions');
  var app = new Marionette.Application();
  var appTemplate = require('hbs!./app');
  var channel = require('backbone.radio').channel('app');

  (function(Backbone) {

    var _sync = Backbone.sync;
    Backbone.sync = function(method, entity, options) {
      var sync = _sync(method, entity, options);
      if (!entity._fetch && method === "read") {
        entity._fetch = sync;
      }
    };
  })(Backbone);

  var Handlebars = require('handlebars');
  var moment = require('moment');

  Handlebars.registerHelper('time', function(unix) {
    var str = moment(unix).format('DD. MMM YYYY');
    return new Handlebars.SafeString(str);
  });

  Handlebars.registerHelper('concat', function(array) {
    var array = _.map(array, function(el) { return el.name });
    return new Handlebars.SafeString(array.join(', '));
  });

  // Load utils
  require('utils/fetch');

  // Load entities
  require('entities/recipes');
  require('entities/ingredients');
  require('entities/tags');

  // Load modules
  require('apps/header/header');
  require('apps/panel/app');
  require('apps/recipes/app');
  require('apps/ingredients/app');
  require('apps/tags/app');

  var Router = Backbone.Router.extend({
    routes: {
      '(/)': 'panel',
      '(/)recipes': 'recipes',
      '(/)recipes/:id': 'recipes',
      '(/)tags': 'tags',
      '(/)ingredients': 'ingredients',
      '(/)ingredients/:id': 'ingredients',
    }, // TODO: Funker det med x/:id og x(/:id) på samma linje?

    initialize: function() {
      this.on('route', function(route, params) {
        channel.command('module:'+route, params[0]);
      });
    }
  });

  app.on('before:start', function() {
    var router = new Router();
  });

  app.on('before:start', function() {
    if (Backbone.history) {
      Backbone.history.start();
    }
  });

  app.on('start', function() {
    channel.command('module:header');
  });

  $(function() {
    $('body').html(appTemplate());

    _.each(regions, function(region, name) {
      channel.reply('region:' + name, function() {
        return region;
      });
    });

    app.start();
  });
});