package com.devon.library.backend.resource;

import com.devon.library.backend.model.Book;
import com.devon.library.backend.service.BookService;
import com.devon.library.backend.service.UserService;
import com.devon.library.backend.model.Role;
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

@Path("/api/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

  @Inject
  BookService bookService;

  @Inject
  UserService userService;

  @GET
  public List<Book> list(@QueryParam("q") String q) {
    if (q != null && !q.isBlank()) {
      return bookService.search(q);
    }
    return bookService.listBooks();
  }

  @GET
  @Path("/{id}")
  public Book get(@PathParam("id") Long id) {
    return bookService.getBook(id).orElse(null);
  }

  public static class CreateBookRequest {
    public String title;
    public String isbn;
    public int pageCount;
    public int totalCopies;
    public String authorName;
    public Long actorUserId; // who performs the action
  }

  @POST
  public Response create(CreateBookRequest req) {
    if (req.actorUserId == null) {
      return Response.status(Response.Status.FORBIDDEN).entity("actorUserId is required").build();
    }
    var actor = userService.getUser(req.actorUserId).orElse(null);
    if (actor == null || actor.getRole() != Role.ADMIN) {
      return Response.status(Response.Status.FORBIDDEN).entity("Admin role required").build();
    }
    Book b = bookService.addBook(req.title, req.isbn, req.pageCount, req.totalCopies, req.authorName);
    return Response.status(Response.Status.CREATED).entity(b).build();
  }

  public static class UpdateBookRequest {
    public String title;
    public String isbn;
    public Integer pageCount;
    public Integer totalCopies;
    public String authorName;
    public Long actorUserId;
  }

  @POST
  @Path("/{id}/update")
  public Response update(@PathParam("id") Long id, UpdateBookRequest req) {
    if (req.actorUserId == null) {
      return Response.status(Response.Status.FORBIDDEN).entity("actorUserId is required").build();
    }
    var actor = userService.getUser(req.actorUserId).orElse(null);
    if (actor == null || actor.getRole() != Role.ADMIN) {
      return Response.status(Response.Status.FORBIDDEN).entity("Admin role required").build();
    }
    Book b = bookService.updateBook(id, req.title, req.isbn, req.pageCount, req.totalCopies, req.authorName);
    return Response.ok(b).build();
  }

  public static class SetCopiesRequest {
    public int totalCopies;
    public Long actorUserId;
  }

  @POST
  @Path("/{id}/copies")
  public Response setCopies(@PathParam("id") Long id, SetCopiesRequest req) {
    if (req.actorUserId == null) {
      return Response.status(Response.Status.FORBIDDEN).entity("actorUserId is required").build();
    }
    var actor = userService.getUser(req.actorUserId).orElse(null);
    if (actor == null || actor.getRole() != Role.ADMIN) {
      return Response.status(Response.Status.FORBIDDEN).entity("Admin role required").build();
    }
    Book b = bookService.setCopyCount(id, req.totalCopies);
    return Response.ok(b).build();
  }

  public static class AdminActorRequest {
    public Long actorUserId;
  }

  @POST
  @Path("/{id}/delete")
  public Response delete(@PathParam("id") Long id, AdminActorRequest req) {
    if (req == null || req.actorUserId == null) {
      return Response.status(Response.Status.FORBIDDEN).entity("actorUserId is required").build();
    }
    var actor = userService.getUser(req.actorUserId).orElse(null);
    if (actor == null || actor.getRole() != Role.ADMIN) {
      return Response.status(Response.Status.FORBIDDEN).entity("Admin role required").build();
    }
    bookService.deleteBook(id);
    return Response.noContent().build();
  }
}


