allControllers.controller('LoginStatusCtrl', ['$scope','$http','currentAuthService',
    function ($scope,$http,currentAuthService) {


        $scope.status = currentAuthService.getStatus();

        if(!$scope.status.authenticated){
            $http.get('getUser.do', {}).success(function(response) {
                if(response.name || response.username){
                    if(response.username && !response.name){
                        currentAuthService.setUsername(response.username);
                        currentAuthService.setName(response.username);
                    }
                    else if(!response.username && response.name){
                        currentAuthService.setUsername(response.name);
                        currentAuthService.setName(response.name);
                    }
                    else{
                        currentAuthService.setUsername(response.username);
                        currentAuthService.setName(response.name);
                    }
                    currentAuthService.setAuthenticated(true);
                    currentAuthService.setIsAllocator(response.allocator);
                }else{
                    currentAuthService.setAuthenticated(false);
                }
            }).error(function(data) {
                currentAuthService.setAuthenticated(false);
            });
        }


    }]);