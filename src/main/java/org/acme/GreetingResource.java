package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
@Produces(MediaType.APPLICATION_JSON) //indica l'output type di questo controller .
public class GreetingResource {

    @GET
    public String hello() {
        return "Hello from Quarkus REST";

    }
}
