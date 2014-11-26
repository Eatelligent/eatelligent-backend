define(function(require) {
  'use strict';

  var $ = require('jquery');
  var Marionette = require('marionette');

  var regions = require('regions');
  var app = new Marionette.Application();
  var appTemplate = require('hbs!./app');
  var channel = require('backbone.radio').channel('app');

  // Load utils
  require('utils/sync');
  require('utils/fetch');
  require('utils/templateHelpers');

  // Load entities
  require('entities/login');
  require('entities/recipes');
  require('entities/languages');
  require('entities/ingredients');
  require('entities/tags');

  // Load modules
  require('apps/login/app');
  require('apps/header/header');
  require('apps/panel/app');
  require('apps/recipes/app');
  require('apps/users/app');
  require('apps/ingredients/app');
  require('apps/tags/app');

  var Router = Backbone.Router.extend({
    routes: {
      '(/)': 'panel',
      '(/)login': 'login',
      '(/)recipes/new': 'new:recipe',
      '(/)recipes(/:id)': 'recipes',
      '(/)tags(/:tag)': 'tags',
      '(/)ingredients(/:id)': 'ingredients',
      '(/)users(/:id)': 'users',
    },

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