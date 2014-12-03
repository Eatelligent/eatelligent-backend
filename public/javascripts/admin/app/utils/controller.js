define(function(require) {
  'use strict';

  var Marionette = require('marionette');
  var channel = require('backbone.radio').channel('app');

  var Controller = Marionette.Controller.extend({
    constructor: function(options) {
      options = options || {};

      this.region = options.region || channel.request('region:main');
      this.listenTo(this.region, 'empty', this.destroy);

      Marionette.Controller.prototype.constructor.call(this, options);
    }
  });

  return Controller;
});