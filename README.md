bestpoll
========

Best Poll (first steps).

# To run it

* Start a MongoDB server. A simple `mongod` process will do.
* Start `ApplicationService.main()`. Use `server target/classes/profiles/local/application.yml` as program arguments.
* See a [trick to use Facebook login locall](http://stackoverflow.com/questions/2459728/how-to-test-facebook-connect-locally).
* Open [http://localhost:8080/index.html](http://localhost:8080/index.html). In fact, you'll open the fake host configured in the previous step.
