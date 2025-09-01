package com.devon.library.backend.resource;

import com.devon.library.backend.model.Loan;
import com.devon.library.backend.service.LoanService;
import com.devon.library.backend.service.UserService;
import com.devon.library.backend.model.Role;
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

@Path("/api/loans")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoanResource {

  @Inject
  LoanService loanService;

  @Inject
  UserService userService;

  public static class CheckoutRequest {
    public Long userId;
    public Long bookId;
  }

  @POST
  public Response checkout(CheckoutRequest req) {
    var actor = userService.getUser(req.userId).orElse(null);
    if (actor == null || actor.getRole() != Role.MEMBER) {
      return Response.status(Response.Status.FORBIDDEN).entity("Member role required to checkout").build();
    }
    Loan loan = loanService.checkout(req.userId, req.bookId);
    return Response.status(Response.Status.CREATED).entity(loan).build();
  }

  @POST
  @Path("/{id}/return")
  public Loan returnBook(@PathParam("id") Long id) {
    return loanService.returnBook(id);
  }

  @POST
  @Path("/{id}/renew")
  public Response renew(@PathParam("id") Long id) {
    // In a real system, verify the caller is the member who owns the loan.
    try {
      var updated = loanService.renew(id);
      return Response.ok(updated).build();
    } catch (IllegalStateException e) {
      return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
    }
  }

  @GET
  @Path("/user/{userId}")
  public List<Loan> byUser(@PathParam("userId") Long userId) {
    return loanService.loansByUser(userId);
  }
}


