# flex-api
Flex API Repository


## Build and Run

### Build

    ./mvnw clean install

### Test

    ./mvnw clean test
    
### Run
   
Run in Development Mode:
   
    ./mvnw spring-boot:run
       
Run in Production Mode:
       
    ./mvnw spring-boot:run -Drun.profiles=prod
       
Try http://localhost:8080/management/env for environment information.

### Integration Tests

Integration tests are not run with the default developer profile. You can run them by executing by using the integration test profile

     mvn clean verify -P integration-test

### View Database

When in development mode you can view the database simply by going to http://localhost:8080/h2-console

Give in the jdbc url of "jdbc:h2:file:./target/h2db/db/flexApiDb;DB_CLOSE_DELAY=-1" and you will connect.


### DB Changelog

To generate a diff between the database and the current JPA model, run

    mvn -X compile liquibase:update -> Makes sure the DB is up to the latest changelog
    mvn compile liquibase:diff -> will generate the change log differences
    
The suggested development workflow is as follows:

- Modify a JPA entity
- Run `mvn compile liquibase:diff`
- A new change log is created in `/target/db_changelog_diff.xml`
- Review and add changelog entries to `src/main/resources/db/changelog/db-changelog-master.xml`

## Docker Image Publish

Assuming you have docker already installed on your client machine. 
To create and publish a docker image in amazon you need to login into amazon on the command line. This can be done by
executing the following command if the account is your default profile
    
    aws ecr get-login --region eu-central-1
    
The flex API uses the [spotify plugin](https://github.com/spotify/docker-maven-plugin) to push images to AWS.
If you wish to just publish to your local registry only then execute 

    ./mvnw -P docker clean package docker:build

If you want to push to the AWS registry then execute 

    ./mvnw -P docker clean package docker:build -DpushImage
