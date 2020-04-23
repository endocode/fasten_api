package org.example.openapiserver;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.api.RequestParameters;
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class OpenAPIServer extends AbstractVerticle {

        HttpServer server;
        Logger logger = LoggerFactory.getLogger("OpenAPI3RouterFactory");

        //    @Override
        //    public void start(Future<Void> future) {
        //        System.out.println("BasicVerticle started");
        //        OpenAPI3RouterFactory.create(this.vertx, "petstore.yaml", ar -> {
        //            if (ar.succeeded()) {
        //                OpenAPI3RouterFactory routerFactory = ar.result(); // (1)
        //            } else {
        //                // Something went wrong during router factory initialization
        //                future.fail(ar.cause()); // (2)
        //            }
        //        });
        //    }
        //
        //    @Override
        //    public void stop() throws Exception {
        //        System.out.println("BasicVerticle stopped");
        //    }

        final List<JsonObject> metadataResp = new ArrayList<>(Arrays.asList(
                new JsonObject().put("id", 1).put("name", "Fufi").put("tag", "ABC"),
                new JsonObject().put("id", 2).put("name", "Garfield").put("tag", "XYZ"),
                new JsonObject().put("id", 3).put("name", "Puffa")
        ));

        @Override
        public void start(Future<Void> future) {
            OpenAPI3RouterFactory.create(this.vertx, "openapispec.json", ar -> {
                if (ar.succeeded()) {
                    OpenAPI3RouterFactory routerFactory = ar.result();

                    // Add routes handlers


                    //@router.get("/{pkg_manager}/{product}/{version}")
                    //def get_metadata(pkg_manager: str, product: str, version: str):
                    //    """
                    //    Given a product and a version, retrieve all known metadata
                    //
                    //    Return: All known metadata for a revision
                    //
                    //    REST examples:
                    //
                    //        GET /api/mvn/org.slf4j:slf4j-api/1.7.29
                    //        GET fasten.eu/api/pypi/numpy/1.15.2
                    //    """
                    //    pass

                    routerFactory.addHandlerByOperationId("get_metadata_api__pkg_manager___product___version__get",
                            routingContext -> routingContext
                                    .response() // <1>
                                    .setStatusCode(200)
                                    //
                                    .putHeader(HttpHeaders.CONTENT_TYPE, "application/json") // <2>
                                    .end(new JsonArray(getMetadata()).encode()) // <3>
                    );

                    // routerFactory.addHandlerByOperationId("listPets", routingContext ->
                    //         // TODO: maybe the URL thing goes here
                    //         routingContext
                    //                 .response() // <1>
                    //                 .setStatusCode(200)
                    //                 .putHeader(HttpHeaders.CONTENT_TYPE, "application/json") // <2>
                    //                 .end(new JsonArray(getAllPets()).encode()) // <3>
                    // );

                    routerFactory.addHandlerByOperationId("createPets", routingContext -> {
                        RequestParameters params = routingContext.get("parsedParameters"); // <1>
                        JsonObject pet = params.body().getJsonObject(); // <2>
                        addPet(pet);
                        routingContext
                                .response()
                                .setStatusCode(200)
                                .end(); // <3>
                    });

                    routerFactory.addHandlerByOperationId("showPetById", routingContext -> {
                        RequestParameters params = routingContext.get("parsedParameters"); // <1>
                        Integer id = params.pathParameter("petId").getInteger(); // <2>
                        Optional<JsonObject> pet = getAllPets()
                                .stream()
                                .filter(p -> p.getInteger("id").equals(id))
                                .findFirst(); // <3>
                        if (pet.isPresent())
                            routingContext
                                    .response()
                                    .setStatusCode(200)
                                    .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                                    .end(pet.get().encode()); // <4>
                        else
                            routingContext.fail(404, new Exception("Pet not found")); // <5>
                    });

                    // Generate the router

                    Router router = routerFactory.getRouter(); // <1>
                    router.errorHandler(404, routingContext -> { // <2>
                        JsonObject errorObject = new JsonObject() // <3>
                                .put("code", 404)
                                .put("message",
                                        (routingContext.failure() != null) ?
                                                routingContext.failure().getMessage() :
                                                "Not Found"
                                );
                        routingContext
                                .response()
                                .setStatusCode(404)
                                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                                .end(errorObject.encode()); // <4>
                    });
                    router.errorHandler(400, routingContext -> {
                        JsonObject errorObject = new JsonObject()
                                .put("code", 400)
                                .put("message",
                                        (routingContext.failure() != null) ?
                                                routingContext.failure().getMessage() :
                                                "Validation Exception"
                                );
                        routingContext
                                .response()
                                .setStatusCode(400)
                                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                                .end(errorObject.encode());
                    });

                    server = vertx.createHttpServer(new HttpServerOptions().setPort(8080).setHost("localhost")); // <5>
                    server.requestHandler(router).listen(); // <6>

                    future.complete(); // Complete the verticle start
                } else {
                    future.fail(ar.cause()); // Fail the verticle start
                }
            });
        }

        @Override
        public void stop(){
            this.server.close();
        }

        public static void main(String[] args) {
            Vertx vertx = Vertx.vertx();
            vertx.deployVerticle(new OpenAPIServer());
        }

        private List<JsonObject> getMetadata() {
            return this.metadataResp;
        }

}