define(function(require) {
  'use strict';

  var Backbone = require('backbone');
  var channel = require('backbone.radio').channel('app');

  var Users = Backbone.Collection.extend({
    url: '/api/users',
    parse: function(response) {
      return response.users;
    }
  });

  channel.reply('entities:collection:users', function() {
    var users = new Users();
    users.fetch({reset: true});
    return users;
  });
});
