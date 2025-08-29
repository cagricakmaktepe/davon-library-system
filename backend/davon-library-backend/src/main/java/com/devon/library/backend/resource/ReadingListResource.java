package com.devon.library.backend.resource;

import com.devon.library.backend.model.ReadingListItem;
import com.devon.library.backend.service.ReadingListService;
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

@Path("/api/reading-list")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReadingListResource {

  @Inject
  ReadingListService service;

  public static class AddRequest {
    public Long userId;
    public Long bookId;
    public Integer priority;
  }

  @POST
  public Response add(AddRequest req) {
    ReadingListItem item = service.add(req.userId, req.bookId, req.priority);
    return Response.status(Response.Status.CREATED).entity(item).build();
  }

  @GET
  @Path("/user/{userId}")
  public List<ReadingListItem> list(@PathParam("userId") Long userId) {
    return service.list(userId);
  }

  @POST
  @Path("/remove")
  public Response remove(AddRequest req) {
    service.remove(req.userId, req.bookId);
    return Response.noContent().build();
  }
}


