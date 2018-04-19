allControllers.controller('LoginStatusCtrl', ['$scope','$http','currentAuthService',
    function ($scope,$http,currentAuthService) {


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
                    currentAuthService.setAuthenticated(true);
                }else{
                    currentAuthService.setAuthenticated(false);
                }
            }).error(function(data) {
                currentAuthService.setAuthenticated(false);
            });
        }
    }]);