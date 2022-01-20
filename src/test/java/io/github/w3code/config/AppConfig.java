package io.github.w3code.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:config/demowebshop.properties"
})
public interface AppConfig extends Config {

    @Key("apiURL")
    String apiURL();

    @Key("webURL")
    String webURL();

    @Key("userLogin")
    String userLogin();

    @Key("userPassword")
    String userPassword();
}
