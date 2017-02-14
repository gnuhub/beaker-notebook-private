var widgets = require('jupyter-js-widgets');
var _ = require('underscore');
var d3 = require('./../bower_components/d3/d3.min');

var plotScope = require('./plot/plotScope');

window.d3 = d3;

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
    console.log('render', this);
    console.log('json??', this.model.get('json'));

    var plotModel = JSON.parse(this.model.get('json'));
    this.initStandardPlot(plotModel);

    // this.value_changed();
    // this.model.on('change:value', this.value_changed, this);
  },

  // value_changed: function() {
  //   this.el.textContent = this.model.get('value');
  // },

  initStandardPlot: function (data) {
    var currentScope = new PlotScope('wrap_'+this.id),
      tmpl = currentScope.buildTemplate();

    // $('div#'+'wrap_'+this.id).append(tmpl);
    //
    // currentScope.setModelData(data);
    // currentScope.init();
  }
});


module.exports = {
  PlotModel: PlotModel,
  PlotView: PlotView
};
