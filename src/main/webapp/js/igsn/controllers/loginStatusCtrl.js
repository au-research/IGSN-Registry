allControllers.controller('LoginStatusCtrl', ['$scope','$rootScope','$http','currentAuthService',
    function ($scope,$rootScope, $http,currentAuthService) {

        $scope.status = currentAuthService.getStatus();

        if(!$scope.status.authenticated){
            $http.get('getUser.do', {}).success(function(response) {
                if(response.username){
                    currentAuthService.setUsername(response.username);
                    currentAuthService.setIsAllocator(response.allocator);
                    if(response.name){
                        currentAuthService.setName(response.name);
                    }
                    if(response.email){
                        currentAuthService.setEmail(response.email);
                    }
                    currentAuthService.setTcAccepted(response.tcAccepted);

                    currentAuthService.setAuthenticated(true);
                }else{
                    currentAuthService.setAuthenticated(false);
                }
            }).error(function(data) {
                currentAuthService.setAuthenticated(false);
            });
        }

        $rootScope.$on('authenticated', function() {
            $scope.status = currentAuthService.getStatus();
            $http.get('getUser.do', {})
                .success(function(response) {
                    currentAuthService.setIsAllocator(response.allocator);
                    currentAuthService.setTcAccepted(response.tcAccepted);
                });
        });
    }]);