package ai.pshenai.cryptodealbot;

import com.binance.connector.futures.client.exceptions.BinanceClientException;
import com.binance.connector.futures.client.exceptions.BinanceConnectorException;
import com.binance.connector.futures.client.impl.UMFuturesClientImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;

@Service
public class CryptoService {

    private static final Logger logger = LoggerFactory.getLogger(CryptodealbotApplication.class);

    /**
     * Returns desired pair's rates via binance java sdk
     *
     * @param pair - name of a cryptocurrency pair, e.g. 'BTCUSDT'
     * @return - value of 'markPrice' field
     */
    @Async
    public CompletableFuture<String> getCryptoPairRates(String pair) {
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("symbol", pair);

        UMFuturesClientImpl client = new UMFuturesClientImpl();

        String result = "";

        try {
            String pairData = client.market().markPrice(parameters);
            logger.info(pairData);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(pairData);
            JsonNode markPriceNode = rootNode.get("markPrice");

            result = markPriceNode.asText();
        } catch (BinanceConnectorException | JsonProcessingException e) {
            logger.error("fullErrMessage: {}", e.getMessage(), e);
        } catch (BinanceClientException e) {
            logger.error("fullErrMessage: {} \nerrMessage: {} \nerrCode: {} \nHTTPStatusCode: {}",
                    e.getMessage(), e.getErrMsg(), e.getErrorCode(), e.getHttpStatusCode(), e);
        }

        return CompletableFuture.completedFuture(result);
    }
}
