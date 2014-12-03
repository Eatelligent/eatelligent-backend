define(function(require) {
  'use strict';

  var Marionette = require('marionette');
  var template = require('hbs!./templates/login');

  require('backbone.stickit');

  var LoginView = Marionette.ItemView.extend({
    template: template,

    ui: {
      'errorbox': '[data-js-errors]'
    },
    
    triggers: {
      'submit form': 'login:clicked'
    },

    bindings: {
      '[data-js-email]': 'email',
      '[data-js-password]': 'password'
    },

    onRender: function() {
      this.stickit();
    },

    displayError: function(msg) {
      this.ui.errorbox.addClass('bg-danger');
      this.ui.errorbox.html(msg);
    }
  });

  return LoginView;
});