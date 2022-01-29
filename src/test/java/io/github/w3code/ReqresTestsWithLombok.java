package io.github.w3code;

import com.github.javafaker.Faker;
import io.github.w3code.models.UserCreation;
import io.github.w3code.models.UserData;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static io.github.w3code.Spec.requestSpec;
import static io.github.w3code.Spec.responseSpec;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class ReqresTestsWithLombok {
    @BeforeAll
    static void setUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    void usersAvatarsTest() {
        ArrayList<String> avatars =
                given()
                        .spec(requestSpec)
                        .when()
                        .get("/users")
                        .then()
                        .spec(responseSpec)
                        .log().all()
                        .extract()
                        .path("data.findAll{it.id}.avatar");

        for (String url : avatars) {
            assertThat(url).endsWith("jpg");
        }
    }

    @Test
    void singleUserTest() {
        UserData data = given()
                .spec(requestSpec)
                .when()
                .get("/users/2")
                .then()
                .spec(responseSpec)
                .extract().as(UserData.class);

        assertThat(data.getUser().getFirstName()).isEqualTo("Janet");
        assertThat(data.getUser().getLastName()).isEqualTo("Weaver");

    }

    @Test
    void singleUserNotFoundTest() {
        given()
                .spec(requestSpec)
                .when()
                .get("/users/23")
                .then()
                .statusCode(404);
    }

    @Test
    void createUserTest() {
        Faker faker = new Faker();
        String name = faker.name().firstName();
        String job = faker.job().position();

        UserCreation userInfo = new UserCreation();
        userInfo.setName(name);
        userInfo.setJob(job);

        UserCreation data =
                given()
                        .spec(requestSpec)
                        .body(userInfo)
                        .when()
                        .post("/users")
                        .then()
                        .statusCode(201)
                        .extract().as(UserCreation.class);

        assertThat(data.getName()).isEqualTo(name);
        assertThat(data.getJob()).isEqualTo(job);
    }

    @Test
    void updateUserTest() {
        Faker faker = new Faker();
        String name = faker.name().firstName();
        String job = faker.job().position();

        UserCreation userInfo = new UserCreation();
        userInfo.setName(name);
        userInfo.setJob(job);

        UserCreation data =
                given()
                        .spec(requestSpec)
                        .body(userInfo)
                        .when()
                        .put("/users/2")
                        .then()
                        .spec(responseSpec)
                        .extract().as(UserCreation.class);

        assertThat(data.getName()).isEqualTo(name);
        assertThat(data.getJob()).isEqualTo(job);
    }
}
