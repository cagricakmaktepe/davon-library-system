package com.devon.library.backend.resource;

import com.devon.library.backend.model.Book;
import com.devon.library.backend.service.BookService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

  @Inject
  BookService bookService;

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
  }

  @POST
  public Book create(CreateBookRequest req) {
    return bookService.addBook(req.title, req.isbn, req.pageCount, req.totalCopies, req.authorName);
  }
}


