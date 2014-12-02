define(function(require) {
  'use strict';

  var Backbone = require('backbone');
  var channel = require('backbone.radio').channel('app');

  var LoginModel = Backbone.Model.extend({
    defaults: {
      email: 'admin@admin.com',
      password: 'admin'
    },

    url: '/api/authenticate',

    login: function() {
      this.save();
    }
  });

  channel.reply('entities:model:login', function() {
    var loginModel = new LoginModel();
    return loginModel;
  });
});
