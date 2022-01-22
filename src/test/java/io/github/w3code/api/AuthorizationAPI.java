package io.github.w3code.api;

import static io.restassured.RestAssured.given;

public class AuthorizationAPI {
    public static String getAuthorizationCookie(String login, String password) {

        return given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .formParam("Email", login)
                .formParam("Password", password)
                .when()
                .post("/login")
                .then()
                .statusCode(302)
                .extract()
                .cookie("NOPCOMMERCE.AUTH");
    }
}
