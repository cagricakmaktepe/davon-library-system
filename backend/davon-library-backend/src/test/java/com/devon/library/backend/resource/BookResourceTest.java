package com.devon.library.backend.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class BookResourceTest {

  @Test
  void list_shouldReturnArray() {
    given()
        .when().get("/api/books")
        .then()
        .statusCode(200)
        .body("size()", greaterThanOrEqualTo(0));
  }

  @Test
  void create_shouldPersistAndReturn201() {
    var adminReq = new java.util.HashMap<String, Object>();
    adminReq.put("name", "Admin1");
    adminReq.put("email", "admin1@example.com");
    adminReq.put("role", "ADMIN");
    long adminId = given().contentType("application/json").body(adminReq).when().post("/api/users").then().statusCode(201).extract().jsonPath().getLong("id");

    var request = new java.util.HashMap<String, Object>();
    request.put("title", "Test Book");
    request.put("isbn", "ISBN-TEST");
    request.put("pageCount", 123);
    request.put("totalCopies", 2);
    request.put("authorName", "Author");
    request.put("actorUserId", adminId);

    given()
        .contentType("application/json")
        .body(request)
        .when().post("/api/books")
        .then()
        .statusCode(201)
        .body("title", equalTo("Test Book"))
        .body("availableCopies", equalTo(2));
  }
}




