define(function(require) {
  'use strict';

  var Ctrl = require('utils/controller');
  var LoginView = require('./view');
  var channel = require('backbone.radio').channel('app');

  var Controller = Ctrl.extend({
    initialize: function(options) {
      var model = channel.request('entities:model:login');

      model.on('change', function(response) {
        console.log('change', arguments);
      });

      this.view = this.getView(model);

      this.view.on('login:clicked', function() {
        model.login();
      });

      this.region.show(this.view);
    },

    getView: function(model) {
      return new LoginView({model: model});
    }
  });

  return Controller;
});