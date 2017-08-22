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
    
Then execute the docker login command which is produced from the output. Once you are logged in then execute the following

    ./bin/docker-publish-cloud.sh tag

If you wish to just publish to your local registry only then execute 

    ./bin/docker-publish-local.sh tag
