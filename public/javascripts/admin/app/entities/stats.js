define(function(require) {
  'use strict';

  var Backbone = require('backbone');
  var channel = require('backbone.radio').channel('app');

  var Stats = Backbone.Model.extend({
    url: '/api/stats',
    parse: function(response) {
      return response.stats;
    }
  });

  channel.reply('entities:model:stats', function() {
    var stats = new Stats();
    stats.fetch();
    return stats;
  });
});
