define(function(require) {
  'use strict';

  var Ctrl = require('utils/controller');
  var Views = require('./view');
  var channel = require('backbone.radio').channel('app');

  var Controller = Ctrl.extend({
    initialize: function(options) {
      var users = channel.request('entities:collection:users');
      var self = this;
      this.view = this.getListView(users);

      this.view.on('childview:user:clicked', function(child) {
        var model = child.model;
        model.fetch();
        channel.request('when:fetched', model, function() {
          self.region.show(self.getUserView(child.model));
        });
      });
      this.region.show(this.view);
    },

    getListView: function(collection) {
      return new Views.ListView({collection: collection});
    },

    getUserView: function(model) {
      return new Views.UserView({model: model});
    }
  });

  return Controller;
});