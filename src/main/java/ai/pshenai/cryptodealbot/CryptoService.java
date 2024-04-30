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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;

@Service
public class CryptoService {

    private static final Logger logger = LoggerFactory.getLogger(CryptodealbotApplication.class);
    private static final HashMap<String, String> pairPriceMap = new HashMap<>();

    /**
     * Once in a minute retrieves all available pairs via binance-java-sdk and pushes their prices into hashMap
     */
    @Async
    @Scheduled(cron = "* * * * *")
    public void getCryptoPairRates() {
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();

        UMFuturesClientImpl client = new UMFuturesClientImpl();
        try {
            String allPairs = client.market().markPrice(parameters);
            logger.info(allPairs);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(allPairs);

            if (rootNode.isArray()) {
                for (JsonNode node : rootNode) {
                    JsonNode markPriceNode = node.get("markPrice");
                    JsonNode markPricePairName = node.get("symbol");
                    if (markPriceNode != null) {
                        String markPrice = markPriceNode.asText();
                        String pairName = markPricePairName.asText();
                        pairPriceMap.put(pairName, markPrice);
                    }
                }
            } else {
                throw new IllegalArgumentException("Input JSON is not an array");
            }
        } catch (BinanceConnectorException | JsonProcessingException e) {
            logger.error("fullErrMessage: {}", e.getMessage(), e);
        } catch (BinanceClientException e) {
            logger.error("fullErrMessage: {} \nerrMessage: {} \nerrCode: {} \nHTTPStatusCode: {}",
                    e.getMessage(), e.getErrMsg(), e.getErrorCode(), e.getHttpStatusCode(), e);
        }
    }

    /**
     * Returns desired pair's rates via binance java sdk
     *
     * @param pair - name of a cryptocurrency pair, e.g. 'BTCUSDT'
     * @return - value of 'markPrice' field
     */
    public String getCryptoPairRates(String pair) {
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

        return result;
    }


    /**
     * Looks for the pair in the map, and, if absent, makes an explicit call to the API.
     *
     * @param pairName - name of a cryptocurrency pair, e.g. 'BTCUSDT'
     * @return - value of 'markPrice' field
     */
    public String getCryptoPairPrice(String pairName) {
        String price = pairPriceMap.get(pairName);

        if(price != null) {
            return price;
        } else {
            return getCryptoPairRates(pairName);
        }
    }
}
