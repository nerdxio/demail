# demail

### Functional Requirement

* Compose message
* Send message
* View my messages
* Folder/Label like
   - Inbox
   - Send
   - User Folder
* Reply and reply all
* View single message

## Non Functional Requirement

- Authentication and Authorization
- High availability
- High Scalability
### Built With

* [Spring boot](https://spring.io/projects/spring-boot)
* [Spring Security](https://docs.spring.io/spring-security/reference/index.html)
* [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
* [Apache Cassandra](https://cassandra.apache.org/_/index.html)
* [Thymeleaf](https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html)
* [Lombok](https://projectlombok.org/)

### Installation

1. Clone the repo
   ```sh
   git clone https://github.com/hassanrefaat9/demail
   ```

2. Add Maven Dependency
   ```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-cassandra</artifactId>
        </dependency>
   
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
   
        <dependency>
            <groupId>org.thymeleaf.extras</groupId>
            <artifactId>thymeleaf-extras-springsecurity5</artifactId>
        </dependency>

   ``` 
   
3. Make Account on [Astra DB](https://auth.cloud.datastax.com/auth/realms/CloudUsers/protocol/openid-connect/auth?client_id=auth-proxy&redirect_uri=https%3A%2F%2Fgatekeeper.auth.cloud.datastax.com%2Fcallback&response_type=code&scope=openid+profile+email&state=dc4BcmiVSiT0QRHADMW8kz1wu3o%3D) and create demail database


4. Enter your App Config in `application.yml`
   ```yml
    spring:
      security:
       oauth2:
         client:
           registration:
             github:
               client-id: your-id
               client-secret: your-secret
   
      data:
        cassandra:
            keyspace-name: your-key-space-name
            username: your-username
            password: password
            schema-action: recreate_drop_unused
            request:
              timeout: 10s
            connection:
            connect-timeout: 10s
            init-query-timeout: 10s
   
    astra.db:
        id: database-id
        region: 
        keyspace: your-key-space
        application.token: your-token
    
    datastax.astra:
        secure-connect-bundle: your-secure-connect.zip
   ```
 
   
