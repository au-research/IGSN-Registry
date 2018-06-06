allControllers.controller('listResourceController', ['$scope','$rootScope','$http','currentAuthService','$route','$templateCache','$location','modalService','selectListService','$routeParams','$filter','$sce',
    function ($scope,$rootScope, $http,currentAuthService,$route,$templateCache,$location,modalService,selectListService,$routeParams,$filter,$sce) {

        // Fetching and displaying Resources
        $scope.igsns = [];
        $scope.fetch = function() {
            $http.get('web/getAllRecords.do', {})
                .success(function(response) {
                    $scope.igsns = response;
                }).error(function(data) {
                    // handle
                });
        };
        $scope.fetch();

        // Ordering
        $scope.order = '-modified';
        $scope.setOrderProperty = function(propertyName) {
            if ($scope.order === propertyName) {
                $scope.order = '-' + propertyName;
            } else if ($scope.order === '-' + propertyName) {
                $scope.order = propertyName;
            } else {
                $scope.order = propertyName;
            }
        }

    }]);