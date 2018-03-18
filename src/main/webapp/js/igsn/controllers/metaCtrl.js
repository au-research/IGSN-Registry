allControllers.controller('metaCtrl', ['$rootScope','$scope','$http','currentAuthService','$templateCache','$location','modalService','$routeParams','leafletData',
                                                    function ($rootScope,$scope,$http,currentAuthService,$templateCache,$location,modalService,$routeParams,leafletData) {	
	
	angular.extend($scope, {
	    defaults: {
	        //tileLayer: 'http://{s}.tile.opencyclemap.org/cycle/{z}/{x}/{y}.png',
	        maxZoom: 14,
	        path: {
	            weight: 10,
	            color: '#800000',
	            opacity: 1
	        }
	    },
	    center: {
	        lat: -27.7,
	        lng: 133,
	        zoom: 4
	    }
	});
	
	$scope.edit = function(){
		if(currentAuthService.getAuthenticated()){
			$location.path("/addresource/" + $routeParams.igsn);
		}else{
			$location.path("/login/addresource/" + $routeParams.igsn);
		}
	}
	
	
	$scope.resource={};
	if($routeParams.igsn){
		$http.get('public/getResource.do', {
		 	params: {
		 		resourceIdentifier: $routeParams.igsn,		 		
		 		}
        }).success(function(data,status) {
        	$scope.resource = data; 
        	
        	 leafletData.getMap().then(function(mymap) {
        		 if(data.location && data.location.wkt!=null){
     	        	var wkt = new Wkt.Wkt();        	
     	        	wkt.read(data.location.wkt);        	
     	        	wkt.toObject();	        	
     	        	var myLayer = L.geoJSON().addTo(mymap);
     	        	myLayer.addData(wkt.toJson());
     	        	if(wkt.type=="point"){
     	        		mymap.panTo(L.latLng(wkt.components[0].y, wkt.components[0].x));
     	        	}else if(wkt.components[0][0]){
     	        		var arr = [];
     	        		for(var i = 0; i<wkt.components[0].length;i++){
     	        			arr.push(L.latLng(wkt.components[0][i].y, wkt.components[0][i].x));
     	        		}
     	        		var polygon = L.polygon(arr);
     	        		mymap.panTo(polygon.getBounds().getCenter());
     	        	}
     	        	
             	}
             });        	        	
	    }).error(function(response,status) {
	    	if(status == 401){
	    		$location.path("/login/meta/" + $routeParams.igsn);
	    	}else{
	    		modalService.showModal({}, {    	            	           
		    		 headerText: response.header ,
			           bodyText: "FAILURE:" + response.message
		    	 });
	    	}
	    	
	    });	  
	}
	
	
	
   
}]);