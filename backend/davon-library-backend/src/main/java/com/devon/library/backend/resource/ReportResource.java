package com.devon.library.backend.resource;

import com.devon.library.backend.model.Role;
import com.devon.library.backend.service.LoanService;
import com.devon.library.backend.service.PenaltyService;
import com.devon.library.backend.service.ReservationService;
import com.devon.library.backend.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/api/reports")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReportResource {

  @Inject
  UserService userService;

  @Inject
  LoanService loanService;

  @Inject
  ReservationService reservationService;

  @Inject
  PenaltyService penaltyService;

  // Admin-only simple counts
  @GET
  @Path("/stats")
  public Response stats(@QueryParam("actorUserId") Long actorUserId) {
    var actor = actorUserId == null ? null : userService.getUser(actorUserId).orElse(null);
    if (actor == null || actor.getRole() != Role.ADMIN) {
      return Response.status(Response.Status.FORBIDDEN).entity("Admin role required").build();
    }
    Map<String, Object> out = new HashMap<>();
    long activeLoans = loanService.findAll().stream().filter(l -> !l.isReturned()).count();
    out.put("activeLoans", activeLoans);
    out.put("totalLoans", loanService.findAll().size());
    out.put("totalReservations", reservationService.listByBook(-1L).size()); // placeholder minimal stat
    long unpaidPenalties = penaltyService.byUser(-1L).stream().filter(p -> !p.isPaid()).count(); // placeholder minimal stat
    out.put("unpaidPenalties", unpaidPenalties);
    return Response.ok(out).build();
  }

  // Overdue loans (simple): return loans whose dueDate < today and not returned
  @GET
  @Path("/overdue")
  public Response overdue(@QueryParam("actorUserId") Long actorUserId) {
    var actor = actorUserId == null ? null : userService.getUser(actorUserId).orElse(null);
    if (actor == null || actor.getRole() != Role.ADMIN) {
      return Response.status(Response.Status.FORBIDDEN).entity("Admin role required").build();
    }
    var list = loanService.findAll().stream()
        .filter(l -> !l.isReturned() && l.getDueDate() != null && l.getDueDate().isBefore(java.time.LocalDate.now()))
        .toList();
    return Response.ok(list).build();
  }
}
