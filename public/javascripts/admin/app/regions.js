define(function(require) {
  'use strict';

  var Marionette = require('marionette');

  var mainRegion = new Marionette.Region({el: '[data-js-main-region]'});
  var headerRegion = new Marionette.Region({el: '[data-js-nav-region]'});

  return {
    main: mainRegion,
    header: headerRegion
  };
});