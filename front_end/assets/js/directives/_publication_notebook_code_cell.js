;(function(angular, app) {
  app.controller('publication-cell-code', function($scope) {
    $scope.toggleCollapsed = function(zone) {
      $scope.cell[zone] = $scope.cell[zone] || {};
      $scope.cell[zone].collapsed = !$scope.cell[zone].collapsed
    }
  });

  app.directive('publicationCellCode', function() {
    return {
      scope: {
        cell: "="
      },
      restrict: "E",
      template: templates.publication_cell_code
    }
  });

  app.directive('publicationCellCodeOutput', function() {
    return {
      scope: {
        cell: "="
      },
      restrict: "E",
      controller: function($scope) {
        $scope.getOutputType = function(cell) {
          if (_.isObject(cell.output.result)) {
            return "publication_output_obj"
          } else {
            return "publication_output_raw"
          }
        }
      },
      template: templates.publication_cell_code_output
    }
  });
})(angular, window.bunsen);
