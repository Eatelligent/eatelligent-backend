define(function(require) {
  'use strict';

  var Marionette = require('marionette');
  var template = require('hbs!./templates/login');

  require('backbone.stickit');

  var LoginView = Marionette.ItemView.extend({
    template: template,
    triggers: {
      'submit form': 'login:clicked'
    },

    bindings: {
      '[data-js-email]': 'email',
      '[data-js-password]': 'password'
    },

    onRender: function() {
      this.stickit();
    }
  });

  return LoginView;
});