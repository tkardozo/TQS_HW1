package currencyConversion;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author tkardozo
 */
public class Converter {
    private Map<String, Map<String, Double>> rates;
    private long lastUpdate;
    private LiveRates liveCurrencyRates;
  
    public Converter() {
        this.rates = new HashMap<>();
        this.lastUpdate = -300000;
        this.liveCurrencyRates =  new CurrencyLayer("http://apilayer.net/api/live?access_key=f5aaad79ddf65b4754246704072b492a&format=1");
    }

    public void setLiveCurrencyRates(LiveRates liveCurrencyRates) {
        this.liveCurrencyRates = liveCurrencyRates;
    }
    
    
    //Mapping from rates from only 1 coin 
    protected Map<String, Map<String, Double>> renderAllRates(Map<String, Double> usdRates){
        Map<String, Map<String, Double>> currentRates = new HashMap<>();
        String[] availableCoins = usdRates.keySet().toArray(new String[usdRates.size()]);
        for (String origin : availableCoins) {
            double source2origin = usdRates.get(origin);
            Map<String, Double> tempMap = new HashMap<>();
            for (String goal : availableCoins) {
                double source2goal = usdRates.get(goal);
                double value = source2goal/source2origin;
                tempMap.put(goal, value);
            }
            currentRates.put(origin, tempMap);
        }
        return currentRates;
    }
    
    Set<String> getAvailableCoins() {
        //called for every request so good for cache maintenance
        Instant instant = Instant.now();
        if(this.lastUpdate+300000<instant.toEpochMilli()){
            Map<String, Double> usdRates = this.liveCurrencyRates.getUpdatedRates();
            this.rates.putAll( this.renderAllRates(usdRates) );
            this.lastUpdate = instant.toEpochMilli();
        }
        return this.rates.keySet();
    }

    Map<String, Double> getRates(String from) throws Exception {
        if(!this.getAvailableCoins().contains(from)){
            throw new Exception("Invalid currency");
        }
        return this.rates.get(from);
    }

    double getRate(String from, String to) throws Exception {
        if(!this.getAvailableCoins().contains(from)){
            throw new Exception("Invalid origin currency");
        }
        if(!this.getAvailableCoins().contains(to)){
            throw new Exception("Invalid goal currency");
        }
        return this.rates.get(from).get(to);
    }

    double convert(String from, String to, double value) throws Exception {
       return getRate(from, to) * value;
    }
}
