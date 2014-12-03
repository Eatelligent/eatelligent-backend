define(function(require) {
  'use strict';

  var _ = require('underscore');
  var channel = require('backbone.radio').channel('app');

  channel.reply('when:fetched', function(entities, callback) {
    var xhrs = _.chain([entities]).flatten().pluck("_fetch").value();

    return $.when.apply($, xhrs).done(function() {
        callback();
    });
  });
});
