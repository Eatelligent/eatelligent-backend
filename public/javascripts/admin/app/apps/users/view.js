define(function(require) {
  'use strict';

  var Marionette = require('marionette');
  var template = require('hbs!./templates/list');
  var userItemTemplate = require('hbs!./templates/user_item');
  var userTemplate = require('hbs!./templates/user');

  var UserView = Marionette.ItemView.extend({
    template: userTemplate
  });

  var UserItem = Marionette.ItemView.extend({
    template: userItemTemplate,
    tagName: 'tr',

    triggers: {
      'click': 'user:clicked'
    }
  });

  var ListView = Marionette.CompositeView.extend({
    template: template,
    childView: UserItem,
    childViewContainer: '[data-js-users]'
  });

  return {
    ListView: ListView,
    UserView: UserView
  };
});