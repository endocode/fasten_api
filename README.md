Run the application from:

- your IDE, by running the main method from the `OpenAPIServer` class, or

- with Maven: `mvn compile exec:java`


Then you can test your API using any command-line tool like curl:
```
$ curl http://localhost:8080/pets
[{"id":1,"name":"Fufi","tag":"ABC"},{"id":2,"name":"Garfield","tag":"ABC"},{"id":3,"name":"Puffa","tag":"ABC"}]

$ curl http://localhost:8080/pets/3
{"id":3,"name":"Puffa","tag":"ABC"}

$ curl http://localhost:8080/pets/5
{"code":404,"message":"Pet not found"}

$ curl -X POST -H "Content-type: application/json" --data '{"id":4,"name":"Alan"}' http://localhost:8080/pets

$ curl -X POST -H "Content-type: application/json" --data '{"id":4}' http://localhost:8080/pets
{"code":400,"message":"$.name: is missing but it is required"}

$ curl http://localhost:8080/pets
[{"id":1,"name":"Fufi","tag":"ABC"},{"id":2,"name":"Garfield","tag":"ABC"},{"id":3,"name":"Puffa","tag":"ABC"},{"id":4,"name":"Alan"}]
```