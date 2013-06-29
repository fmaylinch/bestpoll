var myApp = angular.module('myApp', []);

myApp.factory('Data', function() {
	return {message: "Hello"};
});

function MyController1($scope, Data)
{
	$scope.data = Data;
}

function MyController2($scope, Data) {
	$scope.data = Data;
	$scope.reverseMessage = function(message) {
		return message.split("").reverse().join("");
	};
}
