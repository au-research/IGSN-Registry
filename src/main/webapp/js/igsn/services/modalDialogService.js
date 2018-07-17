app.service('modalService', ['$uibModal','$sce','$location','$http','currentAuthService', function ($uibModal,$sce,$location,$http,currentAuthService) {

      var modalDefaults = {
          backdrop: true,
          keyboard: true,
          modalFade: true,
          templateUrl: 'widget/modal.html'
      };

      var modalOptions = {
          closeButtonText: 'Close',
          actionButtonText: 'OK',
          headerText: 'Proceed?',
          bodyText: 'Perform this action?'
      };

      this.showModal = function (customModalDefaults, customModalOptions) {
          if (!customModalDefaults) customModalDefaults = {};
          customModalDefaults.backdrop = 'static';
          return this.show(customModalDefaults, customModalOptions);
      };

      this.show = function (customModalDefaults, customModalOptions) {
          //Create temp objects to work with since we're in a singleton service
          var tempModalDefaults = {};
          var tempModalOptions = {};

          //Map angular-ui modal custom defaults to modal defaults defined in service
          angular.extend(tempModalDefaults, modalDefaults, customModalDefaults);

          //Map modal.html $scope custom properties to defaults defined in service
          angular.extend(tempModalOptions, modalOptions, customModalOptions);

          if (!tempModalDefaults.controller) {
              tempModalDefaults.controller = function ($scope, $uibModalInstance) {
                  $scope.modalOptions = tempModalOptions;

                  $scope.modalOptions.ok = function (result) {
                	  $uibModalInstance.close("OK");
                  };
                  $scope.modalOptions.bodyText = $sce.trustAsHtml($scope.modalOptions.bodyText);
                  $scope.modalOptions.close = function (result) {
                	  $uibModalInstance.dismiss('cancel');
                  };
                  
                  if($scope.modalOptions.redirect){
                	  $scope.modalOptions.redirectFnc = function () {
                		  $uibModalInstance.close();
                    	  $location.path($scope.modalOptions.redirect);                    	                	 
                    	  window.location.href = $location.absUrl();
                    	  
                      };
                  }
                  
                  if($scope.modalOptions.addAnother){
                	  $scope.modalOptions.addAnotherFnc = function(){
                		  $uibModalInstance.close();
                    	  $location.path($scope.modalOptions.addAnother);                    	                	 
                    	  window.location.href = $location.absUrl();
                	  }
                  }

                  if($scope.modalOptions.cancelTC){
                      $scope.modalOptions.cancelTCFnc = function(){
                         // Terms and Conditions not accepted - we need to log them out;
                          window.location.href = "http://" + $location.host() + ":" + $location.port() + "/logout";
                      }
                  }

                  if($scope.modalOptions.acceptTC){
                      $scope.modalOptions.acceptTCFnc = function(){
                          // Terms and Conditions accepted we need to update the db
                          $http.get('web/setTC.do', {params:{username:$scope.modalOptions.acceptTC}});
                          $uibModalInstance.close();
                      }
                  }
                  
              }
          }

          return $uibModal.open(tempModalDefaults).result;
      };

  }]);