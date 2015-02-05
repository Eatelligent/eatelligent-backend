define(function(require) {
  'use strict';

  var Ctrl = require('utils/controller');
  var PanelView = require('./view');
  var channel = require('backbone.radio').channel('app');

  var Controller = Ctrl.extend({
    initialize: function() {
      var self = this;
      var stats = channel.request('entities:model:stats');
      var ratingStats = channel.request('stats:ratings', {days: 30});
      var userStats = channel.request('stats:users', {days: 30});
      var endpointCollection = channel.request('entities:endpoints:collection');

      channel.request('when:fetched', [stats, ratingStats, userStats, endpointCollection], function() {
        self.view = self.getView(stats, ratingStats, userStats, endpointCollection);
        self.region.show(self.view);
      });
    },

    getView: function(model, ratings, users, endpoints) {
      return new PanelView({
        model: model,
        ratings: ratings,
        users: users,
        endpoints: endpoints
      });
    }
  });

  return Controller;
});