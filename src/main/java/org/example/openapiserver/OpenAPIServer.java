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



        @Override
        public void start(Future<Void> future) {
                OpenAPI3RouterFactory.create(this.vertx, "openapispec.json", ar -> {
                        if (ar.succeeded()) {
                                OpenAPI3RouterFactory routerFactory = ar.result();

                                // Add routes handlerss
                                // "/api/{pkg_manager}/{product}/{version}"
                                // "api/pypi/numpy/1.12.3"
                                routerFactory.addHandlerByOperationId("get_metadata_api__pkg_manager___product___version__get", routingContext -> {
                                    RequestParameters params = routingContext.get("parserdParameters");
                                    String pkg_manager = params.pathParameter("pkg_manager").getString();
                                    String product = params.pathParameter("product").getString();
                                    String version = params.pathParameter("version").getString();
                                    // TODO: call a function to do the query to find the pkg_manager/product/version
                                    String reply = dummyFunction(pkg_manager, product, version);
                                    if (!reply.isEmpty())
                                        routingContext
                                            .response()
                                            .setStatusCode(200)
                                            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                                            .end();
                                    else
                                        routingContext.fail(404, new Exception("Query not found!"));
                                        // TODO: nice to have more specifc exceptions ex: pkg not found, product not found etc.
                                });

                                // Generate the router
                                Router router = routerFactory.getRouter();

                                // Handle errors
                                router.errorHandler(404, routingContext -> {
                                        JsonObject errorObject = new JsonObject()
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
                                                .end(errorObject.encode());
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

                                server = vertx.createHttpServer(new HttpServerOptions().setPort(8080).setHost("localhost"));
                                server.requestHandler(router).listen();

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

        private String dummyFunction(String pkg_manager, String product, String version) {
            return "It's working";
        }

        /* private List<JsonObject> getMetadata() {
            return this.metadataResp;
        } */

}
