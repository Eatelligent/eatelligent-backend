define(function(require) {
  'use strict';

  var channel = require('backbone.radio').channel('app');
  var Controller = require('./controller');

  channel.comply('module:tags', function(query) {
    var controller = new Controller({q: query});
  });
});