define(function(require) {
  'use strict';

  var Handlebars = require('handlebars');
  var moment = require('moment');

  Handlebars.registerHelper('time', function(unix) {
    var str = moment(unix).format('DD. MMM YYYY HH:mm');
    return new Handlebars.SafeString(str);
  });

  Handlebars.registerHelper('linkTags', function(tags) {
    var array = _.map(tags, function(tag) {
      return '<a href="#tags/'+tag+'">'+tag+'</a>';
    });
    return new Handlebars.SafeString(array.join(', '));
  });

  Handlebars.registerHelper('html', function(html) {
    return new Handlebars.SafeString(html);
  });

  Handlebars.registerHelper('converttime', function(t) {
    if(t >= 60) {
      return new Handlebars.SafeString(Math.floor(t/60) + 't, ' + (t - 60*Math.floor(t/60)) + 'm');
    }
    return new Handlebars.SafeString((t || 0) + 'm');
  });
});