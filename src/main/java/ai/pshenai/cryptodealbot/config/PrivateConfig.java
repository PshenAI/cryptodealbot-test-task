package ai.pshenai.cryptodealbot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class PrivateConfig {
    public static final String UM_BASE_URL = "https://fapi.binance.com";
    public static final String CM_BASE_URL = "https://dapi.binance.com";
    public static final String API_KEY = System.getenv("API_KEY");
    public static final String SECRET_KEY = System.getenv("SECRET_KEY");

    public PrivateConfig() {
    }
}
