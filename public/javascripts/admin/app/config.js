require.config({
  paths: {
    'backbone': '../bower_components/backbone/backbone',
    'backbone.babysitter': '../bower_components/backbone.babysitter/lib/backbone.babysitter',
    'backbone.wreqr': '../bower_components/backbone.wreqr/lib/backbone.wreqr',
    'backbone.radio': '../bower_components/backbone.radio/build/backbone.radio',
    'backbone.stickit': '../bower_components/backbone.stickit/backbone.stickit',
    'jquery': '../bower_components/jquery/dist/jquery',
    'lodash': '../bower_components/lodash/dist/lodash.compat',
    'marionette': '../bower_components/marionette/lib/core/backbone.marionette',
    'moment': '../bower_components/moment/moment',
    'requireLib': '../bower_components/requirejs/require',
    'handlebars': '../bower_components/handlebars/handlebars',
    'metrics-graphics': '../bower_components/metrics-graphics/js/metricsgraphics.min',
    'd3': '../bower_components/d3/d3',
    'handlebars-compiler': '../bower_components/handlebars/handlebars',
    'text': '../bower_components/requirejs-text/text',
    'bloodhound': '../bower_components/typeahead.js/dist/bloodhound',
    'bootstrap': '../bower_components/bootstrap/dist/js/bootstrap',
    'summernote': '../bower_components/summernote/dist/summernote',
    'typeahead': '../bower_components/typeahead.js/dist/typeahead.jquery'
  },

  waitSeconds: 30,

  hbs: {
    compilerPath: 'handlebars-compiler'
  },

  packages: [
    {
      name: 'hbs',
      location: '../bower_components/requirejs-hbs',
      main: 'hbs'
    }
  ],

  shim: {

    'underscore': {
      exports: '_'
    },

    'handlebars': {
      exports: 'Handlebars'
    },

    'bloodhound': {
      exports: 'Bloodhound'
    },

    'metrics-graphics': {
      exports: 'data_graphic'
    },

    'summernote': {
      deps: ['bootstrap']
    }
  },

  map: {
    '*': {
      // use lodash instead of underscore
      'underscore': 'lodash'
    }
  }
});
