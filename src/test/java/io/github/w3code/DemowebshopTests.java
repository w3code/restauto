package io.github.w3code;

import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.github.w3code.api.AuthorizationAPI.getAuthorizationCookie;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

public class DemowebshopTests extends TestBase {

    @Test
    @DisplayName("User login test")
    void userLoginTest() {
        String authorizationCookie = getAuthorizationCookie(config.userLogin(), config.userPassword());

        open("/Themes/DefaultClean/Content/images/logo.png");

        getWebDriver().manage().addCookie(
                new Cookie("NOPCOMMERCE.AUTH", authorizationCookie));


        step("Open main page", () ->
                open(config.webURL()));

        step("Verify successful authorization", () ->
                $(".account").shouldHave(text(config.userLogin())));
    }

    @Test
    @DisplayName("Modify firstname and lastname in profile test")
    void modifyProfileTest() {
        //Get cookie by api and set it to browser
        String authorizationCookie = getAuthorizationCookie(config.userLogin(), config.userPassword());

        open("/Themes/DefaultClean/Content/images/logo.png");

        getWebDriver().manage().addCookie(
                new Cookie("NOPCOMMERCE.AUTH", authorizationCookie));

        //Generate fake firstname and lastname
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();

        step("Modify firstname and lastname in profile", () -> {
            //Get response from info page
            Response response =
                    (Response) given()
                            .cookie("NOPCOMMERCE.AUTH", authorizationCookie)
                            .when()
                            .get("/customer/info")
                            .then()
                            .extract();

            //Parse response as HTML page
            Document getHTML = Jsoup.parse(response.asString());

            //Get verification tokens
            String formVerificationToken = getHTML.select("[name=__RequestVerificationToken]").first().attr("value");
            String cookieVerificationToken = response.cookie("__RequestVerificationToken");

            //Send request for firstname and lastname change
            given()
                    .contentType("application/x-www-form-urlencoded;")
                    .cookies("NOPCOMMERCE.AUTH", authorizationCookie, "__RequestVerificationToken", cookieVerificationToken)
                    .body("__RequestVerificationToken=" + formVerificationToken +
                            "&Gender=M" +
                            "&FirstName=" + firstName +
                            "&LastName=" + lastName +
                            "&Email=webshopuser%40email365.com" +
                            "&save-info-button=Save")
                    .when()
                    .post("/customer/info")
                    .then()
                    .statusCode(302)
                    .assertThat()
                    .header("Location", "/customer/info");
        });

        step("Open info page in browser", () ->
                open(config.webURL() + "/customer/info"));

        step("Validate firstname and lastname changes", () -> {
            $("#FirstName").shouldHave(attribute("value", firstName));
            $("#LastName").shouldHave(attribute("value", lastName));
        });
    }
}
