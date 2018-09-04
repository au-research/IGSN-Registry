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
        when('/listResources', {
        templateUrl: 'restricted/listResource.html'
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

app.directive('showorcid', function () {
    return {
        require: 'ngModel',
        link: function(scope, element, attrs, ngModelCtrl){
            var orcid = attrs.orcid;
              $.ajax({
                url:'http://devl.ands.org.au/liz/registry/api/v2.0/orcid.jsonp/lookup/'+orcid,
                dataType: 'jsonp',
                success: function(data) {
                        obj = data.person;
                        var resStr = '';
                        resStr += "<div class='col-md-offset-1'>"
                        resStr += "<h5>ORCID Identifier</h5>";
                        var orcid = data.orcid;
                        resStr += orcid;
                        if(typeof(obj.biography) !== "undefined" && obj.biography!=null)
                        {
                            if(typeof obj.biography['content']=='string' && obj.biography['content']!='')
                            {
                                resStr += "<h5>Biography</h5>";
                                var biography = obj.biography['content'];
                                biography = biography.replace(/"/g,'&quot;');
                                resStr +="<p>"+biography+"</p>";
                            }
                        }
                        if(obj.name)
                        {
                            resStr +="<h5>Personal Details</h5>"
                            resStr +="<p>";
                            if(obj.name['credit-name'])
                                resStr +="Credit name: "+obj.name['credit-name']['value']+" <br />";
                            if(obj.name['family-name'])
                                resStr +="Family name: "+obj.name['family-name']['value']+" <br />";
                            if(obj.name['given-names'])
                                resStr +="Given names: "+obj.name['given-names']['value']+" <br />";
                            if(obj.name['other-names'] && obj.name['other-names'].length)
                            {
                                resStr +="Other names: ";
                                var count = 0;
                                for(i=0; i< (obj.name['other-names'].length -1);i++)
                                {
                                    resStr += obj.name['other-names']['other-name'][i] + ", ";
                                    count++;
                                }
                                resStr += obj.name['other-names']['other-name'][count] + "<br />";
                            }
                        }
                        if(obj.keywords)
                        {
                            var wordsString = obj.keywords['keyword'];
                            if (typeof(wordsString) == 'string') {
                                var wordArray = wordsString.split(',');
                            } else {
                                var wordArray = wordsString;
                            }
                            if (wordArray.length > 0) {
                                resStr += "<h5>Keywords</h5>"
                                var count = 0;
                                for (i = 0; i < (wordArray.length - 1); i++) {
                                    resStr += wordArray[i]['content'] + ", ";
                                    count++;
                                }
                                resStr += wordArray[count]['content'] + "<br />";
                            }
                        }
                        resStr += "</div>";
                        if(obj['researcher-urls'] && obj['researcher-urls'].length)
                        {
                            resStr +="<h5>Research URLs</h5>"
                            var count = 0;
                            if(obj['researcher-urls']['researcher-url']){
                                for(i=0; i< (obj['researcher-urls']['researcher-url'].length -1);i++)
                                {
                                    if(obj['researcher-urls']['researcher-url'][i]['url']!='')
                                        resStr += "URL : " + obj['researcher-urls']['researcher-url'][i]['url'] + "<br /> ";
                                    if(obj['researcher-urls']['researcher-url'][i]['url-name'])
                                        resStr += "URL Name : " + obj['researcher-urls']['researcher-url'][i]['url-name'] + "<br /> ";
                                    count++;
                                }
                            }
                            if(count===0){
                                if(obj['researcher-urls']['researcher-url']['url'])
                                {
                                    if(obj['researcher-urls']['researcher-url']['url']!='')
                                        resStr += "URL : " + obj['researcher-urls']['researcher-url']['url'] + "<br />";
                                }
                                if(obj['researcher-urls']['researcher-url']['url-name'])
                                {
                                    if(obj['researcher-urls']['researcher-url']['url-name']!='')
                                        resStr += "URL Name : " + obj['researcher-urls']['researcher-url']['url-name'] + "<br />";
                                }

                            }else{
                                if(obj['researcher-urls']['researcher-url'][count]['url'])
                                {
                                    if(obj['researcher-urls']['researcher-url'][count]['url']!='')
                                        resStr += "URL : " + obj['researcher-urls']['researcher-url'][count]['url'] + "<br />";
                                }
                                if(obj['researcher-urls']['researcher-url'][count]['url-name'])
                                {
                                    if(obj['researcher-urls']['researcher-url'][count]['url-name']!='')
                                        resStr += "URL Name : " + obj['researcher-urls']['researcher-url'][count]['url-name'] + "<br />";
                                }
                            }
                        }
                        resStr += "</div>"
                    element.context.innerHTML=resStr;
                }
            });
        },
       };
});

