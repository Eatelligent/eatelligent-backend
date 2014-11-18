define(function(require) {
  'use strict';

  var channel = require('backbone.radio').channel('app');
  var Controller = require('./controller');

  channel.comply('module:tags', function(region) {
    var controller = new Controller();
  });
});