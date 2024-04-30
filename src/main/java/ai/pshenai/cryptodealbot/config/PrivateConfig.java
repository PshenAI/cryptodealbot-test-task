package ai.pshenai.cryptodealbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class PrivateConfig {
    public final String UM_BASE_URL = "https://fapi.binance.com";
    public final String CM_BASE_URL = "https://dapi.binance.com";
    @Value("binance.api.key")
    public String API_KEY;
    @Value("binance.secret.key")
    public String SECRET_KEY;

    public PrivateConfig() {
    }
}
