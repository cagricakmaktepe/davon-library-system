package com.devon.library.backend.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class UserResourceTest {

  @Test
  void list_shouldReturnArray() {
    given()
        .when().get("/api/users")
        .then()
        .statusCode(200)
        .body("size()", greaterThanOrEqualTo(0));
  }

  @Test
  void createAndGet_shouldReturnCreatedAndFetchable() {
    var req = new java.util.HashMap<String, Object>();
    req.put("name", "Alice");
    req.put("email", "alice@example.com");

    long id = given()
        .contentType("application/json")
        .body(req)
        .when().post("/api/users")
        .then()
        .statusCode(201)
        .body("name", equalTo("Alice"))
        .extract().jsonPath().getLong("id");

    given()
        .when().get("/api/users/" + id)
        .then()
        .statusCode(200)
        .body("email", equalTo("alice@example.com"));
  }
}



