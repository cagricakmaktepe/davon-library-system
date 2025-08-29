package com.devon.library.backend.resource;

import com.devon.library.backend.model.Notification;
import com.devon.library.backend.model.Role;
import com.devon.library.backend.service.NotificationService;
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

@Path("/api/notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificationResource {

  @Inject
  NotificationService notificationService;

  @Inject
  UserService userService;

  public static class SendNotificationRequest {
    public Long actorUserId;
    public Long userId;
    public String type;
    public String title;
    public String message;
  }

  @POST
  public Response send(SendNotificationRequest req) {
    var actor = req.actorUserId == null ? null : userService.getUser(req.actorUserId).orElse(null);
    if (actor == null || actor.getRole() != Role.ADMIN) {
      return Response.status(Response.Status.FORBIDDEN).entity("Admin role required").build();
    }
    Notification n = notificationService.notifyUser(req.userId, req.type, req.title, req.message);
    return Response.status(Response.Status.CREATED).entity(n).build();
  }

  @GET
  @Path("/user/{userId}")
  public List<Notification> forUser(@PathParam("userId") Long userId) {
    return notificationService.forUser(userId);
  }
}


