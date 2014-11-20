define(function(require) {
  'use strict';

  var Ctrl = require('utils/controller');
  var UsersView = require('./view');
  var channel = require('backbone.radio').channel('app');

  var Controller = Ctrl.extend({
    initialize: function(options) {
      var users = channel.request('entities:collection:users');

      this.view = this.getView(users);
      this.region.show(this.view);
    },

    getView: function(collection) {
      return new UsersView({collection: collection});
    }
  });

  return Controller;
});