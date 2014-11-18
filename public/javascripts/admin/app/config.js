require.config({
  paths: {
    'backbone': '../bower_components/backbone/backbone',
    'backbone.babysitter': '../bower_components/backbone.babysitter/lib/backbone.babysitter',
    'backbone.wreqr': '../bower_components/backbone.wreqr/lib/backbone.wreqr',
    'backbone.radio': '../bower_components/backbone.radio/build/backbone.radio',
    'jquery': '../bower_components/jquery/dist/jquery',
    'lodash': '../bower_components/lodash/dist/lodash.compat',
    'marionette': '../bower_components/marionette/lib/core/backbone.marionette',
    'moment': '../bower_components/moment/moment',
    'requireLib': '../bower_components/requirejs/require',
    'handlebars': '../bower_components/handlebars/handlebars',
    'handlebars-compiler': '../bower_components/handlebars/handlebars',
    'text': '../bower_components/requirejs-text/text',
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
    }
  },

  map: {
    '*': {
      // use lodash instead of underscore
      'underscore': 'lodash'
    }
  }
});
