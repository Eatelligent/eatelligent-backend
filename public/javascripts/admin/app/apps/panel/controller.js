define(function(require) {
  'use strict';

  var Ctrl = require('utils/controller');
  var PanelView = require('./view');

  var Controller = Ctrl.extend({
    initialize: function() {
      this.view = this.getView();
      this.region.show(this.view);
    },

    getView: function() {
      return new PanelView();
    }
  });

  return Controller;
});