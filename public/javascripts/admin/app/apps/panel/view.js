define(function(require) {
  'use strict';

  var Marionette = require('marionette');
  var template = require('hbs!./templates/panel');
  var statsTemplate =require('hbs!./templates/stats');

  var d3 = require('d3');
  var MetricsGraphics = require('metrics-graphics');
  var moment = require('moment');

  var RatingView = Marionette.ItemView.extend({
    template: _.template('<div class="data-js-last-ratings"></div>'),
    className: 'col-md-6 text-center',

    onShow: function() {
      var data = [];
      for (var i = 0; i < 30; i++) {
        data.push({
          date: moment().subtract(i, 'days').format('YYYY-MM-DD'),
          value: _.random(30-i, (30-i)+10)
        });
      }
      data = convert_dates(data, 'date');
      var fake_baselines = [{value:d3.mean(data, function(d) {
        return d.value;
      }), label:'Average last 30 days'}];

      data_graphic({
        title: "Number of ratings last 30 days",
        data: data,
        width: 500,
        area: false,
        show_years: false,
        baselines: fake_baselines,
        target: '.data-js-last-ratings',
        x_accessor: 'date',
        y_accessor: 'value'
      });
    }
  });

  var UserView = Marionette.ItemView.extend({
    template: _.template('<div class="data-js-last-users"></div>'),
    className: 'col-md-6 text-center',

    onShow: function() {
      var data = [];
      for (var i = 0; i < 30; i++) {
        data.push({
          date: moment().subtract(i, 'days').format('YYYY-MM-DD'),
          value: _.random(30-i, (30-i)+10)
        });
      }
      data = convert_dates(data, 'date');
      var fake_baselines = [{value:d3.mean(data, function(d) {
        return d.value;
      }), label:'Average last 30 days'}];

      data_graphic({
        title: "Number of users last 30 days",
        data: data,
        baselines: fake_baselines,
        width: 500,
        area: false,
        show_years: false,
        target: '.data-js-last-users',
        x_accessor: 'date',
        y_accessor: 'value'
      });
    }
  });

  var StatsView = Marionette.ItemView.extend({
    template: statsTemplate,
    className: 'col-md-6 col-md-offset-3'
  });

  var PanelView = Marionette.LayoutView.extend({
    template: template,

    regions: {
      rating: '[data-js-ratings]',
      users: '[data-js-users]',
      stats: '[data-js-stats]'
    },

    onShow: function() {
      var model = new Backbone.Model({
        num_recipes: 50,
        num_ingredients: 152,
        num_users: 25,
        num_ratings: 100,
        num_tags: 23,
        rating_ratio: 100/25
      });
      this.rating.show(new RatingView({model: model}));
      this.users.show(new UserView({model: model}));
      this.stats.show(new StatsView({model: model}));
    }
  });

  return PanelView;
});