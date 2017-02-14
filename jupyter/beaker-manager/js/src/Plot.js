var widgets = require('jupyter-js-widgets');
var _ = require('underscore');


// Custom Model. Custom widgets models must at least provide default values
// for model attributes, including `_model_name`, `_view_name`, `_model_module`
// and `_view_module` when different from the base class.
//
// When serialiazing entire widget state for embedding, only values different from the
// defaults will be specified.
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
    this.value_changed();
    this.model.on('change:value', this.value_changed, this);
  },

  value_changed: function() {
    this.el.textContent = this.model.get('value');
  }
});


module.exports = {
  PlotModel: PlotModel,
  PlotView: PlotView
};
