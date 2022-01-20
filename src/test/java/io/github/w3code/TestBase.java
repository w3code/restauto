package io.github.w3code;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.github.w3code.config.AppConfig;
import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;

public class TestBase {
    public static AppConfig config = ConfigFactory.create(AppConfig.class, System.getProperties());

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = config.apiURL();
        Configuration.baseUrl = config.webURL();
    }
}
