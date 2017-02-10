define([
  'nbextensions/beaker/tableDisplay/tableScope'
], function(
  TableScope
) {

  return {
    createScope: createScope
  };

  // -----

  function createScope(wrapperId) {
    return new TableScope(wrapperId);
  }

});