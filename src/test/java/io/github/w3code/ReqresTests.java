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

    @Test
    void singleUserNotFoundTest() {
        given()
                .when()
                .get("/api/users/23")
                .then()
                .statusCode(404);
    }

    @Test
    void createUserTest() {
        String data = "{\n    \"name\": \"morpheus\",\n    \"job\": \"leader\"\n}";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .body("name", is("morpheus"))
                .body("job", is("leader"));
    }

    @Test
    void updateUserTest() {
        String data = "{\n    \"name\": \"morpheus\",\n    \"job\": \"zion resident\"\n}";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .put("/api/users/2")
                .then()
                .statusCode(200)
                .body("name", is("morpheus"))
                .body("job", is("zion resident"));
    }

}
