define(function(require) {
  'use strict';

  var Ctrl = require('utils/controller');
  var LoginView = require('./view');
  var channel = require('backbone.radio').channel('app');

  var Controller = Ctrl.extend({
    initialize: function(options) {
      var self = this;
      var model = channel.request('entities:model:login');

      this.view = this.getView(model);

      this.view.on('login:clicked', function() {
        model.login();
      });

      model.on('sync', function() {
        Backbone.history.navigate('/', {trigger: true});
        channel.command('module:header');
      });

      model.on('error', function(model, xhr) {
        self.view.displayError(xhr.responseJSON.message);
      });

      this.region.show(this.view);
    },

    getView: function(model) {
      return new LoginView({model: model});
    }
  });

  return Controller;
});