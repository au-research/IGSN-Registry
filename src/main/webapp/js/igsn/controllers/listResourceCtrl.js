allControllers.controller('listResourceController', ['$scope','$rootScope','$http','currentAuthService','$route','$templateCache','$location','modalService','selectListService','$routeParams','$filter','$sce',
    function ($scope,$rootScope, $http,currentAuthService,$route,$templateCache,$location,modalService,selectListService,$routeParams,$filter,$sce) {

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

    }]);