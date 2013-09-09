# Note: Changes in this file will be translated to a "js" file by
#       the IDEA "CoffeeScript" File Watcher.

# Apps

myApp = angular.module('myApp', [])

# Factories

myApp.factory('Data', -> {message: "Hello"})

myApp.factory('UserData', ->
  {
    users : [
     {name: "Peter", surname: "Martins", age: 20},
     {name: "Martin", surname: "Gore", age: 50},
     {name: "Roxy", surname: "Raye", age: 25},
     {name: "Angel", surname: "Rivas", age: 25},
     {name: "Delta", surname: "White", age: 30}
    ]
  })

# Filters

myApp.filter('reverse', -> (text) -> reverseText(text))

# Directives

# Notes:
#  It seems you can't use capital letters or '-' with directives
#    (like enterHere or enter-here)
#  Directives requiere a refresh from the browser itself (no live reloading)

myApp.directive('hello', -> {
  restrict: "E" # (E)lement, (A)ttribute, (C)lass or co(M)ment
  template: "<p>Hello, I'm a directive template</p>"
})

myApp.directive('enter', -> {
  restrict: "A" # A is default
  link: (scope, element) -> # link is default
    element.bind("mouseenter", -> console.log("Inside!"))
})

myApp.directive('leave', ->
  (scope, element) -> # Using default "A" and default link function
    element.bind("mouseleave", -> console.log("Leaving!"))
)

myApp.directive('toggleclazz', ->
  (scope, element, attrs) ->
    element.bind("mouseenter", -> element.addClass(attrs.toggleclazz))
    element.bind("mouseleave", -> element.removeClass(attrs.toggleclazz))
)

# Call controller method (decide which method in the template)
myApp.directive('enterapply', ->
  (scope, element, attrs) ->
    element.bind("mouseenter", -> scope.$apply(attrs.enterapply))
)

# Controllers

window.MyController1 = ($scope, Data) ->
  $scope.data = Data

window.MyController2 = ($scope, Data) ->
  $scope.data = Data
  $scope.reverseMessage = (message) -> reverseText(message)

window.MyController3 = ($scope, Data) ->
  $scope.data = Data

window.MyController4 = ($scope) ->
  $scope.logSomething = -> console.log("Just something")

window.UsersController = ($scope, UserData) ->
  $scope.userData = UserData

# Auxiliary functions

reverseText = (text) -> text.split("").reverse().join("")
