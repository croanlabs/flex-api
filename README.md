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
