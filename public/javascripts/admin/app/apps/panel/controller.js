define(function(require) {
  'use strict';

  var Ctrl = require('utils/controller');
  var PanelView = require('./view');
  var channel = require('backbone.radio').channel('app');

  var Controller = Ctrl.extend({
    initialize: function() {
      var self = this;
      var stats = channel.request('entities:model:stats');

      channel.request('when:fetched', stats, function() {
        self.view = self.getView(stats);
        self.region.show(self.view);
      });
    },

    getView: function(model) {
      return new PanelView({model: model});
    }
  });

  return Controller;
});