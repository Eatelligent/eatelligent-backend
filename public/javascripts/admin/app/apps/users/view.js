define(function(require) {
  'use strict';

  var Marionette = require('marionette');
  var template = require('hbs!./templates/list');
  var userTemplate = require('hbs!./templates/user_item');

  var UserItem = Marionette.ItemView.extend({
    template: userTemplate,
    tagName: 'tr'
  });

  var ListView = Marionette.CompositeView.extend({
    template: template,
    childView: UserItem,
    childViewContainer: '[data-js-user]'
  });

  return ListView;
});