define(function(require) {
  'use strict';

  var Marionette = require('marionette');
  var Ctrl = require('utils/controller');
  var template = require('hbs!./header');
  var channel = require('backbone.radio').channel('app');

  var HeaderView = Marionette.ItemView.extend({
    template: template
  });

  var Controller = Ctrl.extend({
    initialize: function() {
      this.view = this.getView();
      this.region.show(this.view);
    },

    getView: function() {
      return new HeaderView();
    }
  });

  channel.comply('module:header', function() {
    var region = channel.request('region:header');
    var controller = new Controller({region: region});
  });
});