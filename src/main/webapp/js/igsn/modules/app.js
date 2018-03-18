var app = angular.module('app', ['ngRoute','allControllers','ui.bootstrap','ngAnimate','ngFileUpload','ui-leaflet','nemLogging','monospaced.qrcode']);

app.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/', {
    	  redirectTo: '/addresource'
      
      }).
      when('/addresource', {
        templateUrl: 'restricted/addResource.html'
     
      }).  
      when('/addresource/:igsn', {
          templateUrl: 'restricted/addResource.html'
       
        }). 
      when('/registrant', {
          templateUrl: 'restricted/registrant.html',
          resolve:{
              "check":function($location,currentAuthService){   
                  if(currentAuthService.getAuthenticated() && !currentAuthService.getIsAllocator()){ 
                	  $location.path('/');    //redirect user to home.
                      alert("You don't have access here");
                  }
              }
          }
        }). 
      when('/meta/:igsn', {
          templateUrl: 'views/meta.html'        
      }).    
      when('/login/:path/:igsn', {
          templateUrl: 'views/login.html'        
      }).      
      otherwise({
        redirectTo: '/'
      });
  
  }]);


app.directive('capitalize', function() {
  return {
    require: 'ngModel',
    link: function(scope, element, attrs, modelCtrl) {
      var capitalize = function(inputValue) {
        if (inputValue == undefined) inputValue = '';
        var capitalized = inputValue.toUpperCase();
        if (capitalized !== inputValue) {
          modelCtrl.$setViewValue(capitalized);
          modelCtrl.$render();
        }
        return capitalized;
      }
      modelCtrl.$parsers.push(capitalize);
      capitalize(scope[attrs.ngModel]); // capitalize initial value
    }
  };
});


app.directive("formatDate", function(){
  return {
   require: 'ngModel',
    link: function(scope, elem, attr, modelCtrl) {
      modelCtrl.$formatters.push(function(modelValue){
    	  if(modelValue!=null){
    		  return new Date(modelValue);
    	  }
      })
    }
  }
})

app.directive('jqdatepicker', function () {
    return {
        restrict: 'A',
        require: 'ngModel',
         link: function (scope, element, attrs, ngModelCtrl) {
            element.datepicker({
            	dateFormat: 'd/M/yy',
                onSelect: function (date) {
                	ngModelCtrl.$setViewValue(date);
                	ngModelCtrl.$render();
                    scope.$apply();
                }
            });
        }
    };
});

