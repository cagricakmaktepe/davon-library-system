package com.devon.library.backend.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class LoanResourceTest {

  @Test
  void checkoutAndReturn_flowShouldWork() {
    var userReq = new java.util.HashMap<String, Object>();
    userReq.put("name", "Bob");
    userReq.put("email", "bob@example.com");
    long userId = given().contentType("application/json").body(userReq).when().post("/api/users").then().statusCode(201).extract().jsonPath().getLong("id");

    var bookReq = new java.util.HashMap<String, Object>();
    bookReq.put("title", "Loanable");
    bookReq.put("isbn", "L-1");
    bookReq.put("pageCount", 100);
    bookReq.put("totalCopies", 1);
    bookReq.put("authorName", "Auth");
    long bookId = given().contentType("application/json").body(bookReq).when().post("/api/books").then().statusCode(201).extract().jsonPath().getLong("id");

    var checkout = new java.util.HashMap<String, Object>();
    checkout.put("userId", userId);
    checkout.put("bookId", bookId);
    long loanId = given().contentType("application/json").body(checkout).when().post("/api/loans").then().statusCode(201).body("bookId", equalTo((int) bookId)).extract().jsonPath().getLong("id");

    given().contentType("application/json").body("{}").when().post("/api/loans/" + loanId + "/return").then().statusCode(200).body("returnDate", notNullValue());
  }
}



