define(function(require) {
  'use strict';

  var $ = require('jquery');
  var Backbone = require('backbone');
  var channel = require('backbone.radio').channel('app');

  var Endpoints = Backbone.Collection.extend({

  });

  var doit = function(endpoint, collection, cb) {
    var m = new Backbone.Model({
      name: endpoint,
      value: '(pending)',
      code: 0,
      stylings: 'endpoints-waiting'
    });
    collection.add(m);

    setTimeout(function() {
      $.ajax({
        url: endpoint,
        type: 'GET',
        success: function(response, textStatus, xhr) {
          collection.add({name: endpoint, value: textStatus, code: xhr.status, stylings: 'endpoints-normal'});
          collection.remove(m);
          if (cb) {
            cb(response, collection);
          }
        },
        error: function(xhr, error, textStatus) {
          collection.add({name: endpoint, value: textStatus, code: xhr.status, stylings: 'endpoints-error'});
          collection.remove(m);
        }
      })
    }, 1000);

  }

  channel.reply('entities:endpoints:collection', function() {
    var endpoints = new Endpoints();

    doit('/api/users', endpoints);
    doit('/api/user', endpoints);
    doit('/api/ingredients/tags', endpoints);
    doit('/api/languages', endpoints);
    doit('/api/recipes/tags', endpoints);
    doit('/api/ratings/users', endpoints);
    doit('/api/ratings/users', endpoints);
    doit('/api/favorites/recipes', endpoints);

    doit('/api/ingredients', endpoints, function(response, col) {
      doit('/api/ingredients/'+[response.ingredients[0].id], col)
    });

    doit('/api/recipes', endpoints, function(response, col) {
      doit('/api/recipes/'+[response.recipes[0].id], col)
      doit('/api/ratings/recipes/'+[response.recipes[0].id], col)
    });


    return endpoints;
  });

});
