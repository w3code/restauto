package io.github.w3code;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class ReqresTests {
    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "https://reqres.in/";
    }

    @Test
    void listUsersTest() {
        given()
                .when()
                .get("/api/users")
                .then()
                .statusCode(200)
                .body("total", is(12));
    }

    @Test
    void singleUserTest() {
        given()
                .when()
                .get("/api/users/2")
                .then()
                .statusCode(200)
                .body("data.id", is(2));
    }

}
