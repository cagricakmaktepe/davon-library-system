package com.devon.library.backend.resource;

import com.devon.library.backend.model.Reservation;
import com.devon.library.backend.service.LoanService;
import com.devon.library.backend.service.ReservationService;
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

@Path("/api/reservations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReservationResource {

  @Inject
  ReservationService reservationService;

  @Inject
  LoanService loanService;

  public static class CreateReservationRequest {
    public Long userId;
    public Long bookId;
  }

  @POST
  public Response create(CreateReservationRequest req) {
    Reservation r = reservationService.createReservation(req.userId, req.bookId);
    return Response.status(Response.Status.CREATED).entity(r).build();
  }

  public static class ClaimRequest {
    public Long userId;
  }

  @POST
  @Path("/{id}/claim")
  public Response claim(@PathParam("id") Long id, ClaimRequest req) {
    // Minimal: reuse checkout while holding copy reserved by AVAILABLE reservation
    var loan = loanService.checkout(req.userId, reservationService.getReservation(id).orElseThrow().getBookId());
    // Mark reservation completed
    var res = reservationService.getReservation(id).orElseThrow();
    res.setStatus(com.devon.library.backend.model.ReservationStatus.COMPLETED);
    reservationService.cancel(id); // reuse cancel to renumber waiting after completion
    return Response.ok(loan).build();
  }

  @POST
  @Path("/{id}/cancel")
  public Reservation cancel(@PathParam("id") Long id) {
    return reservationService.cancel(id);
  }

  @GET
  @Path("/user/{userId}")
  public List<Reservation> byUser(@PathParam("userId") Long userId) {
    return reservationService.listByUser(userId);
  }

  @GET
  @Path("/book/{bookId}")
  public List<Reservation> byBook(@PathParam("bookId") Long bookId) {
    return reservationService.listByBook(bookId);
  }
}


