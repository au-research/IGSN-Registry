allControllers.controller('addRegistrantCtrl', ['$scope','$http','$uibModalInstance','modalService',
                                                    function ($scope,$http,$uibModalInstance,modalService) {
	
	$scope.form={};
	
	$scope.ok = function(){
		   $http.get('web/addRegistrant.do', {
			 	params: $scope.form
	        }).success(function(response) {
	        	$uibModalInstance.close(true);
		    }).error(function(response) {
		    	modalService.showModal({}, {    	            	           
		    		 headerText: response.header ,
			         bodyText: "FAILURE:" + response.message
		    	 });
		    });	   
	}
	
	$scope.cancel = function () {		
		$uibModalInstance.dismiss('cancel');
	 };
	
}]);


allControllers.controller('addPrefixCtrl', ['$scope','$http','$uibModalInstance','modalService',
                                                function ($scope,$http,$uibModalInstance,modalService) {
		
	$scope.form={};
	
	$scope.ok = function(){
		   $http.get('web/addPrefix.do', {
			 	params: $scope.form
	        }).success(function(response) {
	        	$uibModalInstance.close(true);
		    }).error(function(response) {
		    	modalService.showModal({}, {    	            	           
		    		 headerText: response.header ,
			         bodyText: "FAILURE:" + response.message
		    	 });
		    });	   
	}
	
	$scope.cancel = function () {		
		$uibModalInstance.dismiss('cancel');
	 };


}]);