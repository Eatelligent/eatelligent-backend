define(function(require) {
  'use strict';

  var channel = require('backbone.radio').channel('app');
  var Controller = require('./controller');

  channel.comply('module:users', function(id) {
    var controller = new Controller({id: id});
  });
});