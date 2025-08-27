package com.devon.library.backend.resource;

import com.devon.library.backend.model.User;
import com.devon.library.backend.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

  @Inject
  UserService userService;

  @GET
  public List<User> list() {
    return userService.listUsers();
  }

  @GET
  @Path("/{id}")
  public Response get(@PathParam("id") Long id) {
    return userService.getUser(id)
        .map(Response::ok)
        .orElse(Response.status(Response.Status.NOT_FOUND))
        .build();
  }

  public static class CreateUserRequest {
    public String name;
    public String email;
  }

  @POST
  public Response create(CreateUserRequest req) {
    User u = userService.createUser(req.name, req.email);
    return Response.status(Response.Status.CREATED).entity(u).build();
  }
}


