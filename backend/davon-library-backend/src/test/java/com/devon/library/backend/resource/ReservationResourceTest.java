package com.devon.library.backend.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class ReservationResourceTest {

  @Test
  void createAndCancel_shouldRenumberQueue() {
    var userReq = new java.util.HashMap<String, Object>();
    userReq.put("name", "Carol");
    userReq.put("email", "carol@example.com");
    long userId = given().contentType("application/json").body(userReq).when().post("/api/users").then().statusCode(201).extract().jsonPath().getLong("id");

    var bookReq = new java.util.HashMap<String, Object>();
    bookReq.put("title", "Reserved");
    bookReq.put("isbn", "R-1");
    bookReq.put("pageCount", 100);
    bookReq.put("totalCopies", 1);
    bookReq.put("authorName", "Auth");
    long bookId = given().contentType("application/json").body(bookReq).when().post("/api/books").then().statusCode(201).extract().jsonPath().getLong("id");

    // Make the book unavailable by checking it out
    var checkout = new java.util.HashMap<String, Object>();
    checkout.put("userId", userId);
    checkout.put("bookId", bookId);
    given().contentType("application/json").body(checkout).when().post("/api/loans").then().statusCode(201);

    var rreq1 = new java.util.HashMap<String, Object>();
    rreq1.put("userId", userId);
    rreq1.put("bookId", bookId);
    long r1 = given().contentType("application/json").body(rreq1).when().post("/api/reservations").then().statusCode(201).body("queuePosition", equalTo(1)).extract().jsonPath().getLong("id");

    var rreq2 = new java.util.HashMap<String, Object>();
    rreq2.put("userId", userId + 1);
    rreq2.put("bookId", bookId);
    long r2 = given().contentType("application/json").body(rreq2).when().post("/api/reservations").then().statusCode(201).body("queuePosition", equalTo(2)).extract().jsonPath().getLong("id");

    given().contentType("application/json").body("{}").when().post("/api/reservations/" + r1 + "/cancel").then().statusCode(200);

    given().when().get("/api/reservations/book/" + bookId).then().statusCode(200).body("find { it.id == " + r2 + " }.queuePosition", equalTo(1));
  }
}




