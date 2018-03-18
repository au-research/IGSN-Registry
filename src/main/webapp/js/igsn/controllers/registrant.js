allControllers.controller('registrantCtrl', ['$scope','$http','addRegistrantModalService','addPrefixModalService','modalService',
                                                    function ($scope,$http,addRegistrantModalService,addPrefixModalService,modalService) {
	
	
	listRegistrants = function(){
		$http.get('web/listRegistrants.do')     
	     .success(function(data) {
	    	 $scope.registrantList = data;
	    	 $scope.setSelected($scope.registrantList[0]);
	    	 
	     })
	     .error(function(data, status) {    	
	    	 modalService.showModal({}, {    	            	           
		           headerText: "Retrieve registrant" ,
		           bodyText: "FAILURE: Please contact administrator"
	    	 });			       
	     })
	};
	
	listRegistrants();
	
     
	listPrefix = function(){
		$http.get('web/listPrefix.do')     
	     .success(function(data) {
	    	 $scope.prefixList = data;
	    	 checkAssignedRegistrant();
	     })
	     .error(function(data, status) {    	
	    	 modalService.showModal({}, {    	            	           
		           headerText: "Retrieve prefix" ,
		           bodyText: "FAILURE: Please contact your administrator"
	    	 });			       
	     })
	};
	listPrefix();
	
	
	$scope.addRegistrant = function(){		
		var promise = addRegistrantModalService.open();
	  	 promise.then(function(status) { 
	  		listRegistrants();
	  	 }, function() {
	  		 alert('Failed:');
	  	 });
	}
	
	$scope.addPrefix = function(){
		var promise = addPrefixModalService.open();
		 promise.then(function(status) { 
			 listPrefix();
	  	 }, function() {
	  		 alert('Failed:');
	  	 });
	}
	
	$scope.setSelected = function(registrant){
		$scope.selectedRegistrant = registrant;
		checkAssignedRegistrant();
	}
	
	$scope.allocate = function(prefix,username){
		 $http.get('web/allocatePrefix.do', {
			 	params: {
			 		prefix: prefix,
			 		username : username
			 		}
	        }).success(function(data,status) {
	        	$scope.prefixList = data;
	       	 	checkAssignedRegistrant();
		    }).error(function(response) {
		    	modalService.showModal({}, {    	            	           
		    		 headerText: response.header ,
			           bodyText: "FAILURE:" + response.message
		    	 });
		    });	   
	}
	
	$scope.unAllocate = function(prefix,username){
		 $http.get('web/unAllocatePrefix.do', {
			 	params: {
			 		prefix: prefix,
			 		username : username
			 		}
	        }).success(function(data,status) {
	        	$scope.prefixList = data;
	       	 	checkAssignedRegistrant();
		    }).error(function(response) {
		    	modalService.showModal({}, {    	            	           
		    		 headerText: response.header ,
			           bodyText: "FAILURE:" + response.message
		    	 });
		    });	   
	}
	
	$scope.removeRegistrant = function(username){
		$http.get('web/removeRegistrants.do', {
		 	params: {		 	
		 		username : username
		 		}
        }).success(function(data,status) {
        	 $scope.registrantList = data;
        	 $scope.setSelected($scope.registrantList[0]);
	    }).error(function(response,status) {
	    	modalService.showModal({}, {    	            	           
		           headerText: response.header ,
		           bodyText: "FAILURE:" + response.message
	    	 });
	    });	   
	}
	
	checkAssignedRegistrant = function(){
		if($scope.selectedRegistrant==null){
			return;
		}
		for(var prefixIndex in $scope.prefixList){
			for(prefixRegistrantIndex in $scope.prefixList[prefixIndex].registrants){
				if($scope.prefixList[prefixIndex].registrants[prefixRegistrantIndex].registrantid ==$scope.selectedRegistrant.registrantid){
					$scope.prefixList[prefixIndex].isAssignedToSelectedRegistrant=true;
					break;
				}else{
					$scope.prefixList[prefixIndex].isAssignedToSelectedRegistrant=false;
				}
			}
			
		}
		
	}
	
	$scope.changeActive = function(username,isactive){
		$http.get('web/setRegistrantActive.do', {
		 	params: {
		 		active 	: isactive,
		 		username : username
		 		}
        }).success(function(data,status) {
        	        	 
	    }).error(function(response,status) {
	    	modalService.showModal({}, {    	            	           
		           headerText: response.header ,
		           bodyText: "FAILURE:" + response.message
	    	 });
	    });	   
	}
	
	
     
	
	
	
}]);