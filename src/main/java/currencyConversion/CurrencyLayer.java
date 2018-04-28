package currencyConversion;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 *
 * @author tkardozo
 */
public class CurrencyLayer implements LiveRates{
    private CloseableHttpClient httpClient;
    private String externalAPI;
  
    public CurrencyLayer(String externalAPI) {
        this.externalAPI = externalAPI;
        this.httpClient = HttpClients.createDefault();
    }
    
    public Map<String, Double> getUpdatedRates(){
        Map<String, Double> comparasionMap = new HashMap<>();
        try {
            HttpGet get = new HttpGet(this.externalAPI);
            CloseableHttpResponse response =  this.httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            
            JSONObject exchangeRates = new JSONObject(EntityUtils.toString(entity));
            
            String source = exchangeRates.getString("source");
            JSONObject quotes = exchangeRates.getJSONObject("quotes");
            Iterator<String> currencies = quotes.keys();
            
            while(currencies.hasNext()){
                String to = currencies.next().replaceFirst(source,"");
                comparasionMap.put(to, quotes.getDouble(source+to));
            }
            response.close();
        } catch (IOException ex) {
            Logger.getLogger(CurrencyLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return comparasionMap;
    }
    
}
