/**
 * controller file for punch website
 */
var app = angular.module('ThePunchAppModule',['ngCookies']);

app.service('authInterceptor', function($q) {
    var service = this;

    service.responseError = function(response) {
        if (response.status == 401){
            window.location = "/ThePunchApp/login.html";
        }
        return $q.reject(response);
    };
}).config(['$httpProvider', function($httpProvider) {
    $httpProvider.interceptors.push('authInterceptor');
}])

app.controller('ThePunchAppController', ['$scope', '$http', '$cookies', function($scope, $http, $cookies, $timeout) {
		
	$scope.firstName = "Meet";
	$scope.lastName = "Anadkat";
	$scope.name = 'okay';
	$scope.user = {}
	$scope.urlLogin = '/ThePunchApp/api/auth/login';
	$scope.urlLogout = '/ThePunchApp/api/auth/logout';
		
	$scope.login = function(){
		var dataObj = {
			email : 'deeppandya91@gmail.com',
			password : 'Parth9638'
		};
		$http({
		    method: 'POST',
		    url: $scope.urlLogin,
		    data: dataObj
		}).success(function(data, status, headers, config) {
			$scope.user = data;
			console.log("user is: "+$scope.user.email);
		}).error(function(data, status, headers, config) {
			console.log("error:"+status);
			//alert( "failure message: " + JSON.stringify({data: data}));
		});
	}
	
	$scope.next = function(){
		console.log($cookies.getAll());
		//console.log($cookies.get("sessionId"));
		//$cookies.remove("sessionId");
		//console.log($cookies.getAll());
		var url = '/ThePunchApp/api/hello/abc';
		$http.get(url, {withCredentials: true}).success(function(data, status, headers, config) {
			console.log("abc success");
		}).error(function(data, status, headers, config) {
			//alert( "failure message: " + JSON.stringify({data: data}));
		});
	}
	
	$scope.logout = function(){
		console.log("user is: "+$scope.user.email);
		
		var dataObj = {
			email : $scope.user.email,
		};
		$http({
		    method: 'POST',
		    url: $scope.urlLogout,
		    data: dataObj
		}).success(function(data, status, headers, config) {
			$scope.user = {};
			console.log("logout success");
		}).error(function(data, status, headers, config) {
			console.log("error:"+status);
		});
	}
	
}]);