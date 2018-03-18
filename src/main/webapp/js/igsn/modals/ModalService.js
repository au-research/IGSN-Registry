app.service('addRegistrantModalService',['$uibModal','$q',function ($modal,$q) {
	//VT: GOOGLE MAP MODALS
     this.open = function () {
    	 return $q(function(resolve, reject) {
    		 var modalInstance = $modal.open({
    	         animation: true,
    	         templateUrl: 'widget/addRegistrant.html',
    	         controller: 'addRegistrantCtrl',
    	         size: 'lg'         
    	       });
    		 
    	       modalInstance.result.then(function (result) {
    	 	      resolve(result);
    	 	    });
    	 },1000);

     };
}])





app.service('addPrefixModalService',['$uibModal','$q',function ($modal,$q) {
	//VT: GOOGLE MAP MODALS
     this.open = function () {
    	 return $q(function(resolve, reject) {
    		 var modalInstance = $modal.open({
    	         animation: true,
    	         templateUrl: 'widget/addPrefix.html',
    	         controller: 'addPrefixCtrl',
    	         size: 'md'         
    	       });
    		 
    	       modalInstance.result.then(function (result) {
    	 	      resolve(result);
    	 	    });
    	 },1000);

     };
}])