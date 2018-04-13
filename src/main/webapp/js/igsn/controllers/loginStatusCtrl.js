allControllers.controller('LoginStatusCtrl', ['$scope','$http','currentAuthService',
    function ($scope,$http,currentAuthService) {


        $scope.status = currentAuthService.getStatus();

        if(!$scope.status.authenticated){
            $http.get('getUser.do', {}).success(function(response) {
                if(response.username){
                    currentAuthService.setUsername(response.username);
                    currentAuthService.setIsAllocator(response.allocatorid);
                    if(response.registrantname){
                        currentAuthService.setName(response.registrantname);
                    }
                    else if(response.contactname){
                        currentAuthService.setName(response.registrantname);
                    }
                    else{
                        currentAuthService.setname(response.username);
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