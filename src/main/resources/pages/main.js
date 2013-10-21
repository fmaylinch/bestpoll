// Generated by CoffeeScript 1.6.2
(function() {
  angular.module('findTheBestApp', ['facebook']).config([
    'FacebookProvider', '$httpProvider', function(FacebookProvider, $httpProvider) {
      FacebookProvider.init('163167117213822');
      return $httpProvider.responseInterceptors.push('errorLoggingInterceptor');
    }
  ]).factory('errorLoggingInterceptor', [
    '$log', '$q', function($log, $q) {
      return function(responsePromise) {
        return responsePromise.then(null, function(errResponse) {
          if (errResponse.status === 500) {
            $log.error('$http returned ' + errResponse.status + '. Data:');
            $log.error(errResponse.data);
          }
          return $q.reject(errResponse);
        });
      };
    }
  ]).factory('FacebookService', [
    '$rootScope', '$log', 'Facebook', function($rootScope, $log, Facebook) {
      var service;

      service = {
        login: function() {
          return Facebook.getLoginStatus(function(response) {
            if (response.status === 'connected') {
              return service.applyBroadcastUser();
            } else {
              return Facebook.login();
            }
          });
        },
        logout: function() {
          return Facebook.logout();
        },
        applyBroadcastUser: function() {
          return Facebook.api('/me', function(user) {
            $log.info("Broadcasting user with name: " + user.name);
            return $rootScope.$apply(function() {
              return $rootScope.$broadcast('FacebookService:user', user);
            });
          });
        },
        applyBroadcastNoUser: function() {
          $log.info("Broadcasting no-user");
          return $rootScope.$apply(function() {
            return $rootScope.$broadcast('FacebookService:noUser');
          });
        }
      };
      $rootScope.$on('Facebook:load', function() {
        $log.info("Facebook loaded. Checking login status.");
        return Facebook.getLoginStatus(function(response) {
          if (response.status === 'connected') {
            $log.info("User is already logged");
            return service.applyBroadcastUser();
          } else {
            $log.info("User not logged");
            return service.applyBroadcastNoUser();
          }
        });
      });
      $rootScope.$on('Facebook:login', function(ev, data) {
        if (data.status === 'connected') {
          $log.info("User has logged in");
          return service.applyBroadcastUser();
        }
      });
      $rootScope.$on('Facebook:logout', function() {
        return service.applyBroadcastNoUser();
      });
      return service;
    }
  ]).factory('FindTheBestApiService', [
    '$http', '$log', function($http, $log) {
      var service;

      service = {
        registerUser: function(user) {
          return $http.post('api/user', user);
        },
        findLatestQuestions: function() {
          return $http.get('api/question');
        },
        createQuestion: function(question) {
          return $http.post('api/question', question);
        },
        createAnswer: function(answer) {
          return $http.post('api/answer', answer);
        }
      };
      return service;
    }
  ]).controller('FindTheBestController', [
    '$scope', '$log', 'FacebookService', 'FindTheBestApiService', function($scope, $log, FacebookService, ftbApi) {
      $scope.user = {
        logged: null
      };
      $scope.login = function() {
        return FacebookService.login();
      };
      $scope.logout = function() {
        return FacebookService.logout();
      };
      $scope.userIsLogged = function() {
        return $scope.user.logged;
      };
      $scope.userIsNotLogged = function() {
        return $scope.user.logged !== null && !$scope.user.logged;
      };
      $scope.$on('FacebookService:user', function(ev, fbUser) {
        var userToRegister;

        $log.info("Facebook logged on Facebook: " + fbUser.name);
        userToRegister = {
          facebookId: fbUser.id,
          name: fbUser.name
        };
        return ftbApi.registerUser(userToRegister).success(function(userFromServer) {
          $log.info("User registered on FindTheBest API");
          $scope.user = userFromServer;
          $scope.user.logged = true;
          return $log.info($scope.user);
        });
      });
      $scope.$on('FacebookService:noUser', function() {
        $log.info("Facebook user is gone");
        $scope.user = {
          logged: false
        };
        return $log.info($scope.user);
      });
      return ftbApi.findLatestQuestions().success(function(questions) {
        $log.info("Questions found:");
        $log.info(questions);
        return $scope.questions = questions;
      });
    }
  ]).controller('NewQuestionController', [
    '$scope', '$log', 'FindTheBestApiService', function($scope, $log, ftbApi) {
      $scope.question = {};
      $scope.disableAddQuestion = function() {
        return !$scope.userIsLogged() || !$scope.question.message;
      };
      return $scope.addQuestion = function() {
        $scope.question.creator = {
          id: $scope.user.id
        };
        $log.info($scope.question);
        return ftbApi.createQuestion($scope.question).success(function(questionCreated) {
          $log.info("Question created!");
          $log.info(questionCreated);
          $scope.question = {};
          if (!questionCreated.answers) {
            questionCreated.answers = [];
          }
          return $scope.questions.unshift(questionCreated);
        });
      };
    }
  ]).controller('QuestionListController', [
    '$scope', '$log', 'FindTheBestApiService', function($scope, $log, ftbApi) {
      $scope.disableAddAnswer = function(question) {
        return !$scope.userIsLogged() || !question.newAnswer;
      };
      return $scope.addNewAnswer = function(question) {
        var answer;

        answer = {
          text: question.newAnswer,
          creator: {
            id: $scope.user.id
          },
          question: {
            id: question.id
          }
        };
        $log.info(answer);
        return ftbApi.createAnswer(answer).success(function(answerCreated) {
          $log.info("Answer created!");
          $log.info(answerCreated);
          question.newAnswer = "";
          return question.answers.push(answerCreated);
        });
      };
    }
  ]);

}).call(this);
