package ai.pshenai.cryptodealbot;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crypto")
public class CryptoController {

    private final CryptoService cryptoService;

    public CryptoController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @GetMapping("/{pairName}/price")
    public ResponseEntity<String> getCurrentPrice(@PathVariable String pairName) {
        return ResponseEntity.ok(cryptoService.getCryptoPairPrice(pairName));
    }
}
