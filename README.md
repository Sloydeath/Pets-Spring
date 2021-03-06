## Pets Spring API

## Requirements
### For local deployment:
* Java 1.8
* Spring Boot 2.5.1
* Maven 3.8.1
* PostgreSQL 11.12

### For Cloud Foundry deployment:
* Java 1.8
* Spring Boot 2.5.1
* Maven 3.8.1
* H2 database
* CF version 7.2.0+be4a5ce2b.2020-12-10 (Cloud Foundry)

## Deployment

There are 3 profiles: dev, test, cloud.
* dev profile - uses local database PostgreSQL;
* cloud profile - uses PostgreSQL database in CF.
* test profile - uses local H2 database for test;

### Local deployment
1. Clone git repository:
    ```
    https://github.com/Sloydeath/Pets-Spring.git
    ```
2. Install PostgreSQL and create database pets_db.
3. Connect to database from Intellij.
4. Change spring.datasource.username and spring.datasource.password in file:
    ```
    src/main/resources/application-dev.properties
    ```
   P.S.: If you have changed post of db (by default 5432) 
   or name of database (pets_db), you should update 
   spring.datasource.url in the same file from point №3:
   ```
   spring.datasource.url=jdbc:postgresql://localhost:5432/pets_db
   ```
5. Build project with command:
    ```
    mvn -Pdev clean spring-boot:run
    ```
6. If you want to test app, you should install postman and import files:
    ```
    postman/Pets Local.postman_collection.json
    postman/URLS_FOR_PETS_API.postman_environment.json
    ```
7. You can run app with test profile if you want to use H2 database.
   
### Cloud Foundry deployment
1. Clone git repository:
    ```
    https://github.com/Sloydeath/Pets-Spring.git
    ```

2. Build project with command:
    ```
    mvn clean install
    ```

3. Install cf CLI on your computer.
4. Sign up on the site and create trial account:
   ```
   https://account.hanatrial.ondemand.com/
   ```
5. To log in to the cf CLI:
    * Run in cmd:
      ```
      cf login -a API-URL -u USERNAME -p PASSWORD -o ORG -s SPACE
      ```
      Where:
    * API-URL is your API endpoint, the URL of the Cloud Controller in your Cloud Foundry instance
    * USERNAME is your username.
    * PASSWORD is your password. Cloud Foundry discourages using the -p option, as it may record your password in your shell history.
    * ORG is the org where you want to deploy your apps.
    * SPACE is the space in the org where you want to deploy your apps.
    * More information you can find in CF documentation:
      ```
      https://docs.cloudfoundry.org/cf-cli/getting-started.html
      ```

6. Open cmd in the root folder of project and run:
   ```
   cf push
   ```
   
7. If you want to establish connection in intellij with cloud postgres, you should run this command in CMD:
   ```
   cf ssh -L 63306:postgres-a8cbc12a-eb4e-466b-9d1b-42f8d4edb141.ce4jcviyvogb.eu-central-1.rds.amazonaws.com:2439 pets-spring-leverx
   ```

8. If you want to test app, you should install postman and import files:
    ```
    postman/Pets_CF.postman_collection.json
    postman/URLS_FOR_PETS_API.postman_environment.json
    ```