define(function(require) {
  'use strict';

  var Handlebars = require('handlebars');
  var moment = require('moment');

  Handlebars.registerHelper('time', function(unix) {
    var str = moment(unix).format('DD. MMM YYYY');
    return new Handlebars.SafeString(str);
  });

  Handlebars.registerHelper('linkTags', function(array) {
    var array = _.map(array, function(el) { 
      return '<a href="#tags/'+el.name+'">'+el.name+'</a>'; 
    });
    return new Handlebars.SafeString(array.join(', '));
  });

});