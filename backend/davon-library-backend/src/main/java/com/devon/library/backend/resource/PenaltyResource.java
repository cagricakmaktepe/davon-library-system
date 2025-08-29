package com.devon.library.backend.resource;

import com.devon.library.backend.model.Penalty;
import com.devon.library.backend.model.Role;
import com.devon.library.backend.service.PenaltyService;
import com.devon.library.backend.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/penalties")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PenaltyResource {

  @Inject
  PenaltyService penaltyService;

  @Inject
  UserService userService;

  @GET
  @Path("/user/{userId}")
  public List<Penalty> byUser(@PathParam("userId") Long userId) {
    return penaltyService.byUser(userId);
  }

  public static class AdminActorRequest { public Long actorUserId; }

  // Admin: mark penalty as paid
  @POST
  @Path("/{penaltyId}/paid")
  public Response markPaid(@PathParam("penaltyId") Long penaltyId, AdminActorRequest req) {
    var actor = req == null ? null : userService.getUser(req.actorUserId).orElse(null);
    if (actor == null || actor.getRole() != Role.ADMIN) {
      return Response.status(Response.Status.FORBIDDEN).entity("Admin role required").build();
    }
    return Response.ok(penaltyService.markPaid(penaltyId)).build();
  }

  // Admin: ensure a penalty is generated/updated for a loan (e.g., nightly job)
  @POST
  @Path("/ensure-for-loan/{loanId}")
  public Response ensureForLoan(@PathParam("loanId") Long loanId, @QueryParam("actorUserId") Long actorUserId) {
    var actor = actorUserId == null ? null : userService.getUser(actorUserId).orElse(null);
    if (actor == null || actor.getRole() != Role.ADMIN) {
      return Response.status(Response.Status.FORBIDDEN).entity("Admin role required").build();
    }
    Penalty p = penaltyService.ensurePenaltyForLoan(loanId);
    return Response.ok(p).build();
  }
}


