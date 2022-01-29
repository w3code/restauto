package io.github.w3code;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;

public class Spec {
    public static RequestSpecification requestSpec = with()
            .baseUri("https://reqres.in")
            .basePath("/api")
            .contentType(ContentType.JSON);

    public static ResponseSpecification responseSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .build();
}
