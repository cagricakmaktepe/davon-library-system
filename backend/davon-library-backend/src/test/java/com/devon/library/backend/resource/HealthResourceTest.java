package com.devon.library.backend.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class HealthResourceTest {

  @Test
  void health_shouldReturnOK() {
    given()
        .when().get("/api/health")
        .then()
        .statusCode(200)
        .body(is("OK"));
  }
}




