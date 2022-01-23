package io.github.w3code.api;

import io.qameta.allure.Step;

import static io.github.w3code.filters.CustomLogFilter.customLogFilter;
import static io.restassured.RestAssured.given;

public class AuthorizationAPI {
    @Step("Get authorization cookie")
    public static String getAuthorizationCookie(String login, String password) {

        return given()
                .filter(customLogFilter().withCustomTemplates())
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
