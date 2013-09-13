# Note: Changes in this file will be translated to a "js" file by the IDEA "CoffeeScript" File Watcher.
#       Now it is configured to perform the translation on save (Cmd+S), focus lost or also when restarting the app.


# Module for FindTheBest
# ----------
angular.module('findTheBestApp', ['Facebook'])

  .config([
    'FacebookProvider', '$httpProvider'
    (FacebookProvider,   $httpProvider) ->

      # Init Facebook
      FacebookProvider.init('163167117213822') # Facebook app ID, available at https://developers.facebook.com/apps

      # Log $http errors
      $httpProvider.responseInterceptors.push('errorLoggingInterceptor');
  ])

  # Interceptor for $http that logs an error when a $http call returns a 500 error.
  # It saves us from having to call error() on every $http promise.
  # ----------
  .factory('errorLoggingInterceptor', [
    '$log', '$q',
    ($log,   $q) ->
      (responsePromise) ->
        responsePromise.then(null, (errResponse) ->
          if errResponse.status == 500
            $log.error('$http returned ' + errResponse.status + '. Data:')
            $log.error(errResponse.data)
          $q.reject(errResponse) # "Re-throw" the error; someone might want to do something with it
        )
  ])

  # To use this fake service you must remove the FacebookProvider in config()
  # ----------
  .factory('FakeFacebookService', [
    '$rootScope'
    ($rootScope) ->

      service = {

        # Login if not already logged and broadcasts 'FacebookService:user' with user
        loginIfNecessary: ->
          service.login()

        # Login and broadcasts 'FacebookService:user' with user
        login: ->
          service.applyBroadcastUser()

        # Logout and broadcasts 'FacebookService.noUser'
        logout: ->
          $rootScope.$broadcast('FacebookService:noUser')

        # Broadcasts 'FacebookService:user' with user
        applyBroadcastUser: ->
          user = { id:'1359270259', name:'Ferran May' }
          $rootScope.$broadcast('FacebookService:user', user)
      }

      # Pretend the user is already logged in
      service.applyBroadcastUser()

      service
  ])


  # FacebookService will:
  #   $broadcast('FacebookService:user', user)
  #     when it is aware of a new user. At the beginning and each time a user logs in.
  #   $broadcast('FacebookService:noUser')
  #     when it is aware that there is no user. At the beginning and each time the user logs out.
  # ----------
  .factory('FacebookService', [
    '$rootScope', '$log', 'Facebook',
    ($rootScope,   $log,  Facebook) ->

      service = {

        # Login (if not already logged). Will $broadcast('FacebookService:user', user).
        login: ->
          Facebook.getLoginStatus(
            (response) ->
              if response.status == 'connected'
                service.applyBroadcastUser()
              else
                Facebook.login() # Will indirectly $broadcast('FacebookService:user', user).
          )

        # Logout. Will indirectly $broadcast('FacebookService:noUser').
        logout: ->
          Facebook.logout()

        # $broadcast('FacebookService:user', user) (inside angular with $apply)
        applyBroadcastUser: ->
          Facebook.api('/me', (user) ->
            $log.info("Broadcasting user with name: " + user.name)
            $rootScope.$apply( ->
              $rootScope.$broadcast('FacebookService:user', user)
            )
          )

        # $broadcast('FacebookService:noUser') (inside angular with $apply)
        applyBroadcastLogout: ->
          $log.info("Broadcasting logout")
          $rootScope.$apply( ->
            $rootScope.$broadcast('FacebookService:noUser')
          )
      }

      # 'Facebook:load' is broadcasted by FacebookProvider (facebook.js) when Facebook is ready
      $rootScope.$on('Facebook:load', ->
        $log.info("Facebook loaded. Checking login status.")
        Facebook.getLoginStatus(
          (response) ->
            # For convenience, if a user is already logged, the user will be broadcasted
            # and if user is not already logged, a logout will be broadcasted.
            if response.status == 'connected'
              $log.info("User is already logged")
              service.applyBroadcastUser()
            else
              $log.info("User not logged")
              service.applyBroadcastLogout()
        )
      )

      # For convenience, when a user is logged, the user will be broadcasted (with $apply)
      $rootScope.$on('Facebook:login', (ev, data) ->
        if data.status == 'connected'
          $log.info("User has logged in")
          service.applyBroadcastUser()
      )

      # For convenience, when a user is logged out, an event will be broadcasted (with $apply)
      $rootScope.$on('Facebook:logout', ->
        service.applyBroadcastLogout()
      )

      service
  ])


  # Service to access the FindTheBest API
  # ----------
  .factory('FindTheBestApiService', [
    '$http', '$log'
    ($http,   $log) ->

#      TODO: return the promise directly and call success from the other services?

      service = {

        # Registers a user
        # user: must contain 'facebookId' and 'name' properties.
        # successCallback: called with the user as argument (it will also have the 'id' property)
        # ----------
        registerUser: (user, successCallback) ->
          $http.post('api/user', user)
            .success((userCreated, status, headers, config) ->
              $log.info("API: Register user OK")
              successCallback(userCreated);
            )

        # Finds latest questions, returns a $http promise
        # TODO: offset and limit params
        # ----------
        findLatestQuestions: ->
          $http.get('api/question')

        # Creates a question
        # question: must contain 'message' and 'creator.id'; 'location' is optional.
        # successCallback: called with the created question as argument (it will have also the 'id' property)
        # ----------
        createQuestion: (question, successCallback) ->
          $http.post('api/question', question)
            .success((questionCreated, status, headers, config) ->
              $log.info("API: Question create OK")
              successCallback(questionCreated);
            )

        # Adds an answer to a question
        # answer: must contain 'text', 'creator.id' and 'question.id'; 'url' is optional.
        # successCallback: called with the created answer as argument (it will have also the 'id' property)
        # ----------
        createAnswer: (answer, successCallback) ->
          $http.post('api/answer', answer)
            .success((answerCreated, status, headers, config) ->
              $log.info("API: Answer create OK")
              successCallback(answerCreated);
            )
      }

      service
  ])


  # Main controller for the FindTheBest angular app
  # ----------
  .controller('FindTheBestController', [
    '$scope', '$log', 'FacebookService', 'FindTheBestApiService',
    ($scope,  $log,    FacebookService,   ftbApi) ->

      $scope.user = { logged: null }  # logged will be null until we know whether the user is logged or not

      $scope.login = ->
        FacebookService.login()

      $scope.logout = ->
        FacebookService.logout()

      $scope.$on('FacebookService:user', (ev, fbUser) ->
        $log.info("Facebook user detected: " + fbUser.name)
        userToRegister = { facebookId: fbUser.id, name: fbUser.name } # Corresponds to User class
        ftbApi.registerUser(userToRegister, (userFromServer) ->
          $log.info("User successfully logged on facebook and registered on our API")
          userFromServer.logged = true  # the user is logged
          $scope.user = userFromServer
        )
      )

      $scope.$on('FacebookService:noUser', ->
        $log.info("Facebook user is gone")
        $scope.user = { logged: false }  # assign object when we know the user is not logged
      )

      ftbApi.findLatestQuestions().success((questions) ->
        $log.info("Questions found:")
        $log.info(questions)
        $scope.questions = questions
      )
  ])


  # Controller for the form to add questions
  # ----------
  .controller('NewQuestionController', [
    '$scope', '$log', 'FindTheBestApiService',
    ($scope,   $log,   ftbApi) ->

      $scope.question = {}

      $scope.addQuestion = ->
        $scope.question.creator = {id:$scope.user.id}
        $log.info($scope.question)
        ftbApi.createQuestion($scope.question, (questionCreated) ->
          $log.info("Question created!")
          $log.info(questionCreated)
          # TODO: Show message to user
          # TODO: Add question to results? Depending on filter (although now there is no filter)
          $scope.question = {}
        )
  ])

  # Controller for the question list, where answers can be added and voted
  # ----------
  .controller('QuestionsController', [
    '$scope', '$log', 'FindTheBestApiService',
    ($scope,   $log,   ftbApi) ->

      $scope.addNewAnswer = (question) ->
        answer = { text: question.newAnswer, creator: {id:$scope.user.id}, question: {id: question.id} }
        $log.info(answer)
        ftbApi.createAnswer(answer, (answerCreated) ->
          $log.info("Answer created!")
          $log.info(answerCreated)
          # TODO: Show message to user
          # TODO: Add answer to question in the view
          question.newAnswer = ""
        )
  ])
