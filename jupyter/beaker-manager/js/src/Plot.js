var widgets = require('jupyter-js-widgets');
var _ = require('underscore');
var d3 = require('./../bower_components/d3/d3.min');

var PlotScope = require('./plot/plotScope');

window.d3 = d3;

require('./plot/bko-combinedplot.css');
require('./plot/bko-plot.css');

var PlotModel = widgets.DOMWidgetModel.extend({
  defaults: _.extend({}, widgets.DOMWidgetModel.prototype.defaults, {
    _model_name : 'PlotModel',
    _view_name : 'PlotView',
    _model_module : 'beakermanager',
    _view_module : 'beakermanager'
  })
});


// Custom View. Renders the widget model.
var PlotView = widgets.DOMWidgetView.extend({
  render: function() {
    var that = this;

    this.displayed.then(function() {
      var plotModel = JSON.parse(that.model.get('model'));
      that.initStandardPlot(plotModel);
    });
  },

  initStandardPlot: function (data) {
    var currentScope = new PlotScope('wrap_'+this.id),
      tmpl = currentScope.buildTemplate(),
      tmplElement = $(tmpl);

    tmplElement.appendTo(this.$el);

    currentScope.setModelData(data);
    currentScope.setElement(tmplElement.children('.dtcontainer'));
    currentScope.init();
  }
});


module.exports = {
  PlotModel: PlotModel,
  PlotView: PlotView
};
