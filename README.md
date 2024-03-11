## Building the app

```bash
./mvnw clean package
```
will compile the app, run the unit tests and produce an uber-jar in the target folder.

## Running with maven and testcontainers

The following command will run a version of the application with a mongodb as a testcontainer.
It will have sample entities created in the db, so that one can easily experiment with the api.

```bash
./mvnw spring-boot:test-run
```

## Running from jar
You will need a running mongodb instance that you can spin up with docker-compose.

```bash
docker compose -f docker/docker-compose.yml up -d
```

Once mongodb is up, run the app itself:

```bash
java -jar target/news-api-0.0.1-SNAPSHOT.jar
```

You may stop the instance once you're finished.
```bash
docker compose -f docker/docker-compose.yml down
```

## Available commands

The rest api services manages authors, news, their categories, and comments.
The following URL shows the Swagger / Open API documentation of the available end points. Here you may try out
individual requests. When updating and deleting news and comments you must also specify the userId as the request
parameter in the URL. It must equal the author id of the respective entity, otherwise you will get 403 Forbidden
response. This logic is implemented with Spring AOP.

http://localhost:8080/swagger-ui/index.html

The next URL shows the definition of the end points in json format. It can be imported into a Postman collection,
for example.

http://localhost:8080/v3/api-docs


## Configuration

The app reads its configuration from the `src/main/resources/application.yml` file.
You may override any of the parameters from the command line using the `-D` flag similar to the examples above.

The following configuration parameters govern the behavior of the app
