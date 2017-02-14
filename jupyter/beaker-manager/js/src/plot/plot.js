define([
  'nbextensions/beaker/bower_components/d3/d3.min',
  'nbextensions/beaker/plot/plotScope'
], function(
  d3,
  PlotScope
) {

  window.d3 = d3;

  return {
    createScope: createScope
  };

  // -----

  function createScope(data) {
      return new PlotScope(data);
  }

});