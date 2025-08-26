package com.devon.library.backend.resource;

import com.devon.library.backend.model.Loan;
import com.devon.library.backend.service.LoanService;
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

  public static class CheckoutRequest {
    public Long userId;
    public Long bookId;
  }

  @POST
  public Response checkout(CheckoutRequest req) {
    Loan loan = loanService.checkout(req.userId, req.bookId);
    return Response.status(Response.Status.CREATED).entity(loan).build();
  }

  @POST
  @Path("/{id}/return")
  public Loan returnBook(@PathParam("id") Long id) {
    return loanService.returnBook(id);
  }

  @GET
  @Path("/user/{userId}")
  public List<Loan> byUser(@PathParam("userId") Long userId) {
    return loanService.loansByUser(userId);
  }
}


