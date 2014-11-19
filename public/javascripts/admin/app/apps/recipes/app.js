define(function(require) {
  'use strict';

  var channel = require('backbone.radio').channel('app');
  var NewController = require('./new.controller');
  var Controller = require('./controller');

  channel.comply('module:new:recipe', function() {
    var  controller = new NewController();
  })

  channel.comply('module:recipes', function(id) {
    var controller = new Controller({id: id});
  });
});