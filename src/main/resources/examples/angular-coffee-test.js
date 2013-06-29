// Generated by CoffeeScript 1.6.2
(function() {
  var myApp;

  myApp = angular.module('myApp', []);

  myApp.factory('Data', function() {
    return {
      message: "Hello"
    };
  });

  myApp.factory('UserData', function() {
    return {
      users: [
        {
          name: "Peter",
          surname: "Martins",
          age: 20
        }, {
          name: "Martin",
          surname: "Gore",
          age: 50
        }, {
          name: "Roxy",
          surname: "Raye",
          age: 25
        }, {
          name: "Angel",
          surname: "Rivas",
          age: 25
        }, {
          name: "Delta",
          surname: "White",
          age: 30
        }
      ]
    };
  });

  myApp.filter('reverse', function(Data) {
    return function(text) {
      return text.split("").reverse().join("") + "-" + Data.message;
    };
  });

  myApp.directive('hello', function() {
    return {
      restrict: "E",
      template: "<p>Hello, I'm a directive template</p>"
    };
  });

  myApp.directive('enter', function() {
    return {
      restrict: "A",
      link: function(scope, element) {
        return element.bind("mouseenter", function() {
          return console.log("Inside!");
        });
      }
    };
  });

  myApp.directive('leave', function() {
    return function(scope, element) {
      return element.bind("mouseleave", function() {
        return console.log("Leaving!");
      });
    };
  });

  myApp.directive('toggleclazz', function() {
    return function(scope, element, attrs) {
      element.bind("mouseenter", function() {
        return element.addClass(attrs.toggleclazz);
      });
      return element.bind("mouseleave", function() {
        return element.removeClass(attrs.toggleclazz);
      });
    };
  });

  myApp.directive('enterapply', function() {
    return function(scope, element, attrs) {
      return element.bind("mouseenter", function() {
        return scope.$apply(attrs.enterapply);
      });
    };
  });

  window.MyController1 = function($scope, Data) {
    return $scope.data = Data;
  };

  window.MyController2 = function($scope, Data) {
    $scope.data = Data;
    return $scope.reverseMessage = function(message) {
      return message.split("").reverse().join("");
    };
  };

  window.MyController3 = function($scope, Data) {
    return $scope.data = Data;
  };

  window.MyController4 = function($scope) {
    return $scope.logSomething = function() {
      return console.log("Just something");
    };
  };

  window.UsersController = function($scope, UserData) {
    return $scope.userData = UserData;
  };

}).call(this);
