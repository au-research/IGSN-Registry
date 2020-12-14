allControllers.controller('LoginCtrl', ['$scope','$rootScope','$timeout','$http','currentAuthService','$route','$templateCache','$location','modalService','$routeParams', '$window',
    function ($scope,$rootScope,$timeout,$http,currentAuthService,$route,$templateCache,$location,modalService,$routeParams,$window) {

        $scope.isRedirect = false;
        $window.location.href = $location.protocol() + "://" +$location.host() +"/igsn-registry";
        $scope.aaf = {};
        $scope.fetchAAFDetails = function() {
            $http.get('getAAF.do')
                .success(function(message) {
                    $scope.aaf = message;
                });
        };
        $scope.fetchAAFDetails();

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
                if(data.username){
                    currentAuthService.setUsername(data.username);
                    currentAuthService.setIsAllocator(data.allocator);
                    currentAuthService.setTcAccepted(data.tcAccepted);
                    if(data.name){
                        currentAuthService.setName(data.name);
                    }
                    if(data.email){
                        currentAuthService.setEmail(data.email);
                    }
                    currentAuthService.setAuthenticated(true);
                    if($routeParams.path && $routeParams.igsn){
                        $timeout(function(){
                            $location.path("/" + $routeParams.path + "/" + $routeParams.igsn);
                            window.location.href = $location.absUrl();
                            var currentPageTemplate = $route.current.templateUrl;
                            $templateCache.remove(currentPageTemplate);
                            $route.reload();
                        },0);
                    }else if($location.path()=='/login'){
                        if(data.allocator)
                            $locationpath("/#registrant");
                        else
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