allControllers.controller('LoginCtrl', ['$scope','$timeout','$http','currentAuthService','$route','$templateCache','$location','modalService','$routeParams',
    function ($scope,$timeout,$http,currentAuthService,$route,$templateCache,$location,modalService,$routeParams) {

        $scope.isRedirect = false;

        if(Object.keys($routeParams).length > 0 && !$routeParams.sessionid && !$routeParams.callbackurl){
            $scope.isRedirect = true;
            $scope.redirectIGSN = $routeParams.igsn;
        }

        var authenticate = function(credentials) {
            if(!(credentials.username && credentials.password)){
                return;
            }

            var details = 'j_username=' + encodeURIComponent(credentials.username) +
                '&j_password=' + encodeURIComponent(credentials.password);


            $http.post('views/login.html',details, {
                headers : {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).success(function(data,status) {
                if(data.name || data.username) {
                    currentAuthService.setAuthenticated(true);
                    if(data.username && !data.name){
                        currentAuthService.setUsername(data.username);
                        currentAuthService.setName(data.username);
                    }
                    else if(!data.username && data.name){
                        currentAuthService.setUsername(data.name);
                        currentAuthService.setName(data.name);
                    }
                    else{
                        currentAuthService.setUsername(data.username);
                        currentAuthService.setName(data.name);
                    }
                    currentAuthService.setPermissions(data.userPermission);
                    currentAuthService.setIsAllocator(data.isAllocator);
                    if($routeParams.path && $routeParams.igsn){
                        $timeout(function(){
                            $location.path("/" + $routeParams.path + "/" + $routeParams.igsn);
                            window.location.href = $location.absUrl();
                            var currentPageTemplate = $route.current.templateUrl;
                            $templateCache.remove(currentPageTemplate);
                            $route.reload();
                        },0);
                    }else if($location.path()=='/login'){
                        $location.path("/");
                    }else{
                        var currentPageTemplate = $route.current.templateUrl;
                        $templateCache.remove(currentPageTemplate);
                        $route.reload();
                    }

                }else{
                    if(data.restricted){
                        modalService.showModal({}, {
                            headerText: "Limited Access",
                            bodyText: "Although your authentication is successful, you do not have the required permission"
                        });
                    }
                    currentAuthService.setAuthenticated(false);
                    $scope.error=true;
                }

            }).error(function() {
                currentAuthService.setAuthenticated(false);
            });

        }


        $scope.credentials = {};
        $scope.login = function() {
            authenticate($scope.credentials);
        };


        $scope.logout = function() {
            $http.get('logout', {}).success(function() {
                currentAuthService.setAuthenticated(false);
            }).error(function(data) {
                currentAuthService.setAuthenticated(false);
            });
        }


    }]);