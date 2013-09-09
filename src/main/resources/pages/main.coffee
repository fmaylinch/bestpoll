# Note: Changes in this file will be translated to a "js" file by
#       the IDEA "CoffeeScript" File Watcher.

# Apps

angular.module('findTheBestApp', ['Facebook'])


  .config([
    'FacebookProvider',
    (FacebookProvider) ->

      # Define your routes through $routeProvider and else...
      # Note: There was a problem injecting $routeProvider just in case you add it...
      #       I think $routeProvider is not bundled by default so you need another js.

      FacebookProvider.init('163167117213822') # Facebook app ID, available at https://developers.facebook.com/apps
  ])

   # To use this fake service I you must remove the FacebookProvider
  .factory('FakeFacebookService', [
    '$rootScope'
    ($rootScope) ->

      service = {

        # Login if not already logged and broadcasts 'FacebookService:me' with user
        loginIfNecessary: ->
          service.login()

        # Login and broadcasts 'FacebookService:me' with user
        login: ->
          service.applyBroadcastUser()

        # Logout and broadcasts 'FacebookService.logout'
        logout: ->
          $rootScope.$broadcast('FacebookService:noUser')

        # Broadcasts 'FacebookService:me' with user
        applyBroadcastUser: ->
          user = { id:'1359270259', name:'Ferran May' }
          $rootScope.$broadcast('FacebookService:me', user)
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

  .factory('FacebookService', [
    '$rootScope', 'Facebook'
    ($rootScope,   Facebook) ->

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
            console.log("Broadcasting user with name: " + user.name)
            $rootScope.$apply( ->
              $rootScope.$broadcast('FacebookService:user', user)
            )
          )

        # $broadcast('FacebookService:noUser') (inside angular with $apply)
        applyBroadcastLogout: ->
          console.log("Broadcasting logout")
          $rootScope.$apply( ->
            $rootScope.$broadcast('FacebookService:noUser')
          )
      }

      # 'Facebook:load' is broadcasted by FacebookProvider (facebook.js) when Facebook is ready
      $rootScope.$on('Facebook:load', ->
        console.log("Facebook loaded. Checking login status.")
        Facebook.getLoginStatus(
          (response) ->
            # For convenience, if a user is already logged, the user will be broadcasted
            # and if user is not already logged, a logout will be broadcasted.
            if response.status == 'connected'
              console.log("User is already logged")
              service.applyBroadcastUser()
            else
              console.log("User not logged")
              service.applyBroadcastLogout()
        )
      )

      # For convenience, when a user is logged, the user will be broadcasted (with $apply)
      $rootScope.$on('Facebook:login', (ev, data) ->
        if data.status == 'connected'
          console.log("User has logged in")
          service.applyBroadcastUser()
      )

      # For convenience, when a user is logged out, an event will be broadcasted (with $apply)
      $rootScope.$on('Facebook:logout', ->
        service.applyBroadcastLogout()
      )

      service
  ])

  .factory('FindTheBestApiService', [
    '$http'
    ($http) ->

      service = {

        # Registers user on API.
        # user: must contain 'facebookId' and 'name' properties.
        # successCallback: called with the user as argument if user is successfully registered
        # ----------
        registerUser: (user, successCallback) ->
          $http.post('api/user', user)
            .success((data, status, headers, config) ->
              console.log("Login on server OK")
              successCallback(data);
            )
            .error((data, status, headers, config) ->
              console.log("Login on server ERROR")
              console.log(status)
              console.log(data)
            )

      }

      service
  ])

      # This controller uses my FacebookService so it's simpler
  .controller('FindTheBestController', [
    '$scope', 'FacebookService', 'FindTheBestApiService',
    ($scope,   FacebookService,   ftbApi) ->

      $scope.questions = [
        {text:'Shawarma', place:'Barcelona', answers: [
          {text:'Sannin', points:86, your_vote:1},
          {text:'Urgarit', points:56, your_vote:1, url:'http://www.ugarit.es'},
          {text:'Equinox', points:48, your_vote:1, url:'http://www.equinoxverdi.com/'},
          {text:'Sundown', points:13, your_vote:0},
          {text:'Petra', points:28, your_vote:-1},
        ]},
        {text:'Brand of mountain bikes', answers: [
          {text:'Specialized', points:235, your_vote:0, url:'http://www.specialized.com'},
          {text:'Giant', points:186, your_vote:0, url:'http://www.giant-bicycles.com'},
          {text:'Cannondale', points:146, your_vote:0, url:'http://www.cannondale.com'},
          {text:'Mondraker', points:128, your_vote:0, url:'http://www.mondraker.com'},
          {text:'Lapierre', points:107, your_vote:0, url:'http://lapierrebikes.com/'},
        ]},
        {text:'Pizza restaurant', place:'Italy', answers: [
          {text:'Pizzalia 1', points:10, your_vote:0},
          {text:'Pizzalia 2', points:20, your_vote:0},
          {text:'Pizzalia 3', points:30, your_vote:0},
        ]}
      ]

      $scope.user = null  # user will be null until we know whether the user is logged or not

      $scope.login = ->
        FacebookService.login()

      $scope.logout = ->
        FacebookService.logout()

      $scope.$on('FacebookService:user', (ev, fbUser) ->
        console.log("Facebook user detected: " + fbUser.name)
        userToRegister = { facebookId: fbUser.id, name: fbUser.name } # Corresponds to User class
        ftbApi.registerUser(userToRegister, (userFromServer) ->
          console.log("User successfully logged on facebook and registered on our API")
          $scope.user = userFromServer  # the user is logged
        )
      )

      $scope.$on('FacebookService:noUser', ->
        console.log("Facebook user is gone")
        $scope.user = {}  # empty object when we know the user is not logged
      )

      $
  ])
