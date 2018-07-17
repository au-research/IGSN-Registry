app.service('currentAuthService', function($rootScope) {
	
	 var status = {};	

    return {
    	getAuthenticated: function () {
            return status.authenticated;
        },
        setAuthenticated: function (auth) {
    	    if (auth) {
    	        $rootScope.$broadcast('authenticated');
            }
        	status.authenticated = auth;
        },
        setUsername : function(name){
        	status.username=name;
        },
        getUsername : function(){
        	return status.username;
        },
        setName : function(name){
        	status.name=name;
        },
        setEmail : function(email){
            status.email=email;
        },
        getName : function(){
        	return status.name;
        },
        getEmail : function(){
            return status.email;
        },
        setPermissions : function(permissions){
        	status.permissions=permissions;
        },
        getPermissions : function(){
        	return status.permissions;
        },
        setIsAllocator : function(isAllocator){
        	status.isAllocator=isAllocator;
        },
        getIsAllocator : function(){
        	return status.isAllocator;
        },
        getTcAccepted : function(){
            return status.tc_accepted;
        },
        setTcAccepted : function(tcAccepted){
            status.tc_accepted = tcAccepted;
        },
        getStatus : function(){
            return status;
        }
    };	    
    
});