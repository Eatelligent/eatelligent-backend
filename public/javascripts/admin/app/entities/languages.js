define(function(require) {
  'use strict';

  var Backbone = require('backbone');
  var channel = require('backbone.radio').channel('app');

  var Languages = Backbone.Collection.extend({
    url: '/api/languages',
    parse: function(response) {
      return response.languages;
    }
  });

  channel.reply('entities:collection:languages', function() {
    var languages = new Languages();
    languages.fetch({reset: true});
    return languages;
  });
});
