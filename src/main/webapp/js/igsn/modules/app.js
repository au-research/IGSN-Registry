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

app.directive('grantwidget', function() {
    return {
        require: 'ngModel',
        link: function(scope, element, attrs, modelCtrl) {
            var elem = element;
            var widget = elem.grant_widget({
                lookup: false,
                before_html: '',
                tooltip: true,
                wrap_html: '',
                search_text: '<i class="fa fa-search"></i>Grant Search',
                custom_select_handler: function (data, obj, settings) {
                    $('.select_grant_search_result').on('click', function () {
                        modelCtrl.$setViewValue($(this).attr('grant-id'));
                        obj.val($(this).attr('grant-id'));
                        modelCtrl.$render();
                      for(var res in scope.resource.relatedResourceses){
                          var resnum = res;
                          if(scope.resource.relatedResourceses[resnum].relatedResource == $(this).attr('grant-id')){
                              scope.resource.relatedResourceses[resnum].relationType="http://pid.geoscience.gov.au/def/voc/igsn-codelists/Participates";
                              scope.resource.relatedResourceses[resnum].cvIdentifierType = {identifierType:"http://pid.geoscience.gov.au/def/voc/igsn-codelists/PURL"};
                              obj.trigger('change');
                           }
                        }
                        modelCtrl.$setViewValue($(this).attr('grant-id'));
                        modelCtrl.$render();
                      if (settings.auto_close_search) $('#' + settings._wid).slideUp();

                    });
                }

            });
        }
    };
});
app.directive('showgrant', function() {
    return {
        require: 'ngModel',
        link: function(scope, element, attrs, ngModelCtrl) {
            var purl = attrs.grantid;
            var grantid = purl.split("/");
            var resStr = "";
            if (grantid[6]) {
                $.ajax({
                    url: 'https://researchdata.ands.org.au/registry/services/api/getGrants/?id=' + grantid[6],
                    dataType: 'jsonp',
                    success: function (data) {
                        if(data.message.numFound == 1) {
                            var obj = data.message.recordData;
                            resStr += "<div>"
                            resStr += "<h4>Grant Information</h4>";
                            if(obj[0]['title'])
                            {
                                resStr += "<h6>Title</h6>";
                                var title = $('<textarea />').html(obj[0]['title']).text();
                                title = title.replace(/"/g,'&quot;');
                                resStr +="<p>"+title+"</p>";
                            }
                            if(obj[0]['description'])
                            {
                                resStr += "<h6>Description</h6>";
                                var description = $('<textarea />').html(obj[0]['description']).text();
                                description = description.replace(/"/g,'&quot;');
                                resStr +="<p>"+description+"</p>";
                            }
                            if(obj[0]['identifiers'])
                            {
                                var i;
                                var identifier = '';

                                for (i in obj[0]['identifier_type']) {
                                    if (obj[0]['identifier_type'].hasOwnProperty(i)) {
                                        identifier = obj[0]['identifier_type'][i]
                                    }
                                }

                                for (j in obj[0]['identifier_type']) {
                                    if (obj[0]['identifier_type'].hasOwnProperty(j)) {
                                        if(j=='purl')
                                        {
                                            identifier = obj[0]['identifier_type'][j]
                                        }
                                    }
                                }

                                resStr += "<h6>Identifier</h6>";

                                resStr +="<p>"+identifier+"</p>";
                                obj[0]['identifier'] = identifier
                            }
                            if(obj[0]['relations'])
                            {
                                resStr += "<h6>Relationships</h6>";

                                if(typeof(obj[0]['relations']['isFundedBy'])=='string')
                                {
                                    resStr +="<p>Is funded by</p>";
                                    resStr +="<p>"+obj[0]['relations']['isFundedBy']+"</p>";
                                }else if (typeof(obj[0]['relations']['isFundedBy'])=='object'){
                                    resStr +="<p>Is funded by</p><p>";
                                    for(i=0;i<obj[0]['relations']['isFundedBy'].length;i++)
                                    {
                                        resStr +=obj[0]['relations']['isFundedBy'][i]+", ";
                                    }
                                    resStr = resStr.replace(/(^\s*,)|(,\s*$)/g, '');
                                    resStr +="</p>";
                                }
                                if(typeof(obj[0]['relations']['isManagedBy'])=='string')
                                {
                                    resStr +="<p>Is managed by</p>";
                                    resStr +="<p>"+obj[0]['relations']['isManagedBy']+"</p>";
                                }else if (typeof(obj[0]['relations']['isManagedBy'])=='object'){
                                    resStr +="<p>Is managed by</p><p>";
                                    for(i=0;i<obj[0]['relations']['isManagedBy'].length;i++)
                                    {
                                        resStr +=obj[0]['relations']['isManagedBy'][i]+", ";
                                    }
                                    resStr = resStr.replace(/(^\s*,)|(,\s*$)/g, '');
                                    resStr +="</p>";
                                }
                                if(typeof(obj[0]['relations']['isParticipantIn'])=='string')
                                {
                                    resStr +="<p>Has participant</p>";
                                    resStr +="<p>"+obj[0]['relations']['isParticipantIn']+"</p>";
                                }else if(typeof(obj[0]['relations']['isParticipantIn'])=='object'){
                                    resStr +="<p>Has participant</p><p>";
                                    for(i=0;i<obj[0]['relations']['isParticipantIn'].length;i++)
                                    {
                                        resStr +=obj[0]['relations']['isParticipantIn'][i]+", ";
                                    }
                                    resStr = resStr.replace(/(^\s*,)|(,\s*$)/g, '');
                                    resStr +="</p>";
                                }
                                if(typeof(obj[0]['relations']['isPrincipalInvestigatorOf'])=='string')
                                {
                                    resStr +="<p>Has principal investigator</p>";
                                    resStr +="<p>"+obj[0]['relations']['isPrincipalInvestigatorOf']+"</p>";
                                }else if (typeof(obj[0]['relations']['isPrincipalInvestigatorOf'])=='object'){
                                    resStr +="<p>Has principal investigator</p><p>";
                                    for(i=0;i<obj[0]['relations']['isPrincipalInvestigatorOf'].length;i++)
                                    {
                                        resStr +=obj[0]['relations']['isPrincipalInvestigatorOf'][i]+", ";
                                    }
                                    resStr = resStr.replace(/(^\s*,)|(,\s*$)/g, '');
                                    resStr +="</p>";
                                }

                            }
                        }
                        element.context.innerHTML=resStr;
                    }

                });
            }else{
                resStr = "";
            }
            element.context.innerHTML=resStr;
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
                url:'https://test.ands.org.au/api/v2.0/orcid.jsonp/lookup/'+orcid,
                dataType: 'jsonp',
                success: function(data) {
                        obj = data.person;
                        var resStr = '';
                        if(obj) {
                            resStr += "<div class='col-md-offset-1'>"
                            resStr += "<h5>ORCID Identifier</h5>";
                            var orcid = data.orcid;
                            resStr += orcid;
                            if (typeof(obj.biography) !== "undefined" && obj.biography != null) {
                                if (typeof obj.biography['content'] == 'string' && obj.biography['content'] != '') {
                                    resStr += "<h5>Biography</h5>";
                                    var biography = obj.biography['content'];
                                    biography = biography.replace(/"/g, '&quot;');
                                    resStr += "<p>" + biography + "</p>";
                                }
                            }
                            if (obj.name) {
                                resStr += "<h5>Personal Details</h5>"
                                resStr += "<p>";
                                if (obj.name['credit-name'])
                                    resStr += "Credit name: " + obj.name['credit-name']['value'] + " <br />";
                                if (obj.name['family-name'])
                                    resStr += "Family name: " + obj.name['family-name']['value'] + " <br />";
                                if (obj.name['given-names'])
                                    resStr += "Given names: " + obj.name['given-names']['value'] + " <br />";
                                if (obj.name['other-names'] && obj.name['other-names'].length) {
                                    resStr += "Other names: ";
                                    var count = 0;
                                    for (i = 0; i < (obj.name['other-names'].length - 1); i++) {
                                        resStr += obj.name['other-names']['other-name'][i] + ", ";
                                        count++;
                                    }
                                    resStr += obj.name['other-names']['other-name'][count] + "<br />";
                                }
                            }
                            if (obj.keywords) {
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
                            if (obj['researcher-urls'] && obj['researcher-urls'].length) {
                                resStr += "<h5>Research URLs</h5>"
                                var count = 0;
                                if (obj['researcher-urls']['researcher-url']) {
                                    for (i = 0; i < (obj['researcher-urls']['researcher-url'].length - 1); i++) {
                                        if (obj['researcher-urls']['researcher-url'][i]['url'] != '')
                                            resStr += "URL : " + obj['researcher-urls']['researcher-url'][i]['url'] + "<br /> ";
                                        if (obj['researcher-urls']['researcher-url'][i]['url-name'])
                                            resStr += "URL Name : " + obj['researcher-urls']['researcher-url'][i]['url-name'] + "<br /> ";
                                        count++;
                                    }
                                }
                                if (count === 0) {
                                    if (obj['researcher-urls']['researcher-url']['url']) {
                                        if (obj['researcher-urls']['researcher-url']['url'] != '')
                                            resStr += "URL : " + obj['researcher-urls']['researcher-url']['url'] + "<br />";
                                    }
                                    if (obj['researcher-urls']['researcher-url']['url-name']) {
                                        if (obj['researcher-urls']['researcher-url']['url-name'] != '')
                                            resStr += "URL Name : " + obj['researcher-urls']['researcher-url']['url-name'] + "<br />";
                                    }

                                } else {
                                    if (obj['researcher-urls']['researcher-url'][count]['url']) {
                                        if (obj['researcher-urls']['researcher-url'][count]['url'] != '')
                                            resStr += "URL : " + obj['researcher-urls']['researcher-url'][count]['url'] + "<br />";
                                    }
                                    if (obj['researcher-urls']['researcher-url'][count]['url-name']) {
                                        if (obj['researcher-urls']['researcher-url'][count]['url-name'] != '')
                                            resStr += "URL Name : " + obj['researcher-urls']['researcher-url'][count]['url-name'] + "<br />";
                                    }
                                }
                            }
                            resStr += "</div>";
                        }
                    element.context.innerHTML=resStr;
                }
            });
        },
       };
});

