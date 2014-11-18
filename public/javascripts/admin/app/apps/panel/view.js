define(function(require) {
  'use strict';

  var Marionette = require('marionette');
  var template = require('hbs!./templates/panel');

  var PanelView = Marionette.ItemView.extend({
    template: template
  });

  return PanelView;
});