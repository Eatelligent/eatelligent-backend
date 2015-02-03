define(function(require) {
  'use strict';

  var Backbone = require('backbone');
  var moment = require('moment');
  var channel = require('backbone.radio').channel('app');

  var DATEFORMAT = 'YYYY-MM-DD';

  var StatsOverTimeCollection = Backbone.Collection.extend({
    initialize: function(options) {
      this.type = options.type;
      this.from = moment().subtract(options.days, 'days').format(DATEFORMAT);
      this.to = moment().format(DATEFORMAT);
    },
    url: function() {
      return '/api/stats/'+this.type+'?from=' + this.from + '&to='+this.to;
    },

    parse: function(response) {
      return response.stats;
    }
  })

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

  channel.reply('stats:ratings', function(options) {
    var stats = new StatsOverTimeCollection({
      type: 'ratings',
      days: options.days || 30
    });
    stats.fetch();
    return stats;
  });

  channel.reply('stats:users', function(options) {
    var stats = new StatsOverTimeCollection({
      type: 'users',
      days: options.days || 30
    });
    stats.fetch();
    return stats;
  });

});
