define(function(require) {
  'use strict';

  var Backbone = require('backbone');
  var channel = require('backbone.radio').channel('app');

  var User = Backbone.Model.extend({
    urlRoot: '/api/users',
    parse: function(response) {
      return response.user || response;
    }
  });

  var Users = Backbone.Collection.extend({
    url: '/api/users',
    model: User,
    parse: function(response) {
      return response.users || response;
    }
  });

  channel.reply('entities:collection:users', function() {
    var users = new Users();
    users.fetch({reset: true});
    return users;
  });
});
