# sampl

SAMPLE APP UNDER DEVELOPMENT

How to start the sampl application
---

1. Run `gradle build` to build your application
1. Start application with `java -jar build/lib/sample-0.1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Prepare the db
---

1. create database sample;
2. CREATE USER 'sample'@'localhost' IDENTIFIED BY 'sample';
3. GRANT ALL PRIVILEGES ON * . * TO 'sample'@'localhost';


Features
---

* Standalone jar: gradle shadowJar
* Hibernate 
* Migrations
* Simple authentication/authorization: admin,admin for write, read, delete and user,user for read only
* Healthcheck: To see your applications health enter url `http://localhost:8081/healthcheck`
* Filter user actions to log file