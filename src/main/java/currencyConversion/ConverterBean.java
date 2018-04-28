package currencyConversion;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 *
 * @author tkardozo
 */
@Named(value = "converterBean")
@ApplicationScoped

public class ConverterBean {

    private static Converter cc = new Converter();
    private double value, result;
    private String fromCoin, toCoin;

    public void setValue(double value) {
        this.value = value;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public void setFromCoin(String fromCoin) {
        this.fromCoin = fromCoin;
    }

    public void setToCoin(String toCoin) {
        this.toCoin = toCoin;
    }

    public double getValue() {
        return value;
    }

    public double getResult() {
        return result;
    }

    public String getFromCoin() {
        return fromCoin;
    }

    public String getToCoin() {
        return toCoin;
    }
    
    /**
     * Creates a new instance of converterBean
     */
    public ConverterBean() {
    }
    
    public Set<String> coins() {
        return cc.getAvailableCoins();
    }
    
    public String convert() throws Exception {
        this.result = cc.convert(this.fromCoin, this.toCoin, this.value);
        return "index.xhtml";
    }
    
}
