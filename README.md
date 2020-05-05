Run the application from:

- your IDE, by running the main method from the `OpenAPIServer` class, or

- with Maven: `mvn clean install && mvn exec:java`


Then you can test your API using any command-line tool like curl:
```
curl -X GET "http://127.0.0.1:3000/api" -H "accept: application/json"
Welcome to FASTEN API

curl -X GET "http://127.0.0.1:3000/api/pypi/numpy/1.15.2" -H "accept: application/json"
{"message":"Package manager: pypi, Product: numpy, Version: 1.15.2."}
```