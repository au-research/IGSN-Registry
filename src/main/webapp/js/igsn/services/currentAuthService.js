app.service('currentAuthService', function() {
	
	 var status = {};	

    return {
    	getAuthenticated: function () {
            return status.authenticated;
        },
        setAuthenticated: function (auth) {
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
        getName : function(){
        	return status.name;
        },
        setPermissions : function(permissions){
        	status.permissions=permissions;
        },
        getPermissions : function(){
        	return status.permissions;
        },
        getStatus : function(){
        	return status;
        },
        setIsAllocator : function(isAllocator){
        	status.isAllocator=isAllocator;
        },
        getIsAllocator : function(){
        	return status.isAllocator;
        }
    };	    
    
});