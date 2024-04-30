package ai.pshenai.cryptodealbot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CryptodealbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptodealbotApplication.class, args);
	}

	@Bean
	public CommandLineRunner onStart(final CryptoService cryptoService) {
		return strings -> cryptoService.getCryptoPairRates();

	}
}
