package io.github.w3code;

import com.codeborne.selenide.Configuration;
import io.github.w3code.config.AppConfig;
import io.github.w3code.config.CredentialsConfig;
import io.github.w3code.helpers.Attach;
import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.remote.DesiredCapabilities;

import static java.lang.String.format;

public class TestBase {
    public static AppConfig config = ConfigFactory.create(AppConfig.class, System.getProperties());

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = config.apiURL();
        Configuration.baseUrl = config.webURL();

        CredentialsConfig credentials =
                ConfigFactory.create(CredentialsConfig.class);
        String login = credentials.login();
        String password = credentials.password();

        Configuration.browserSize = "1920x1080";
        Configuration.remote = format("https://%s:%s@selenoid.autotests.cloud/wd/hub/", login, password);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", true);

        Configuration.browserCapabilities = capabilities;
    }

    @AfterEach
    public void tearDown() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();
    }
}
