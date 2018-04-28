/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package currencyConversion;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tkardozo
 */
public class RestIT {

    @Test
    public void restMustReturnJson() throws IOException{
        HttpUriRequest request = new HttpGet( "http://localhost:8080/currency/rest" );
        HttpResponse response = HttpClientBuilder.create().build().execute( request );
        String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
        assertEquals( "application/json", mimeType );
    }

    @Test
    public void restMustReturnCoins() throws IOException, ParseException{
        HttpUriRequest request = new HttpGet( "http://localhost:8080/currency/rest" );
        HttpResponse response = HttpClientBuilder.create().build().execute( request );
        
        JSONParser jsonParser = new JSONParser();
        JSONObject result = (JSONObject)jsonParser.parse(
            new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        
        JSONArray coins = (JSONArray) result.get("coins");
        Iterator checkCoins = coins.iterator();
        while(checkCoins.hasNext()){
            assertTrue("Avalilable coins must be an array of strings", 
                        checkCoins.next() instanceof String);
        }
    }
    
    @Test
    public void restMustReturnAllRatesFromOneCoin() throws IOException, ParseException{
        HttpUriRequest request = new HttpGet( "http://localhost:8080/currency/rest/EUR" );
        HttpResponse response = HttpClientBuilder.create().build().execute( request );
        
        JSONParser jsonParser = new JSONParser();
        JSONObject result = (JSONObject)jsonParser.parse(
            new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        
        assertTrue("from must be the same as in the request", 
                   result.get("from").equals("EUR"));        
        
        JSONObject rates = (JSONObject) result.get("ratesTo");
        
        for(Object coin : rates.keySet()){
            System.out.println(coin);
            System.out.println(rates.get(coin).getClass());
            assertTrue("Avalilable goal coins must be String", 
                        coin instanceof String);
            assertTrue("Avalilable rates to coin must be double (or Long for 1)", 
                        rates.get(coin) instanceof Double || rates.get(coin) instanceof Long);
        }
    }
    
    @Test
    public void restMustReturnRateFromDesiredConversionType() throws IOException, ParseException{
        HttpUriRequest request = new HttpGet( "http://localhost:8080/currency/rest/EUR/USD" );
        HttpResponse response = HttpClientBuilder.create().build().execute( request );
        
        JSONParser jsonParser = new JSONParser();
        JSONObject result = (JSONObject)jsonParser.parse(
            new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        
        assertTrue("from must be the same as in the request", 
                   result.get("from").equals("EUR"));
        assertTrue("to must be the same as in the request", 
                   result.get("to").equals("USD"));
         assertTrue("Avalilable rates to coin must be double (or Long for 1)", 
                   result.get("rate") instanceof Double || result.get("rate") instanceof Long);
        
    }
    
    @Test
    public void restMustReturnResultFromDesiredConversion() throws IOException, ParseException{
        HttpUriRequest request = new HttpGet( "http://localhost:8080/currency/rest/EUR/USD/12.3" );
        HttpResponse response = HttpClientBuilder.create().build().execute( request );
        
        JSONParser jsonParser = new JSONParser();
        JSONObject result = (JSONObject)jsonParser.parse(
            new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        
        assertTrue("from must be the same as in the request", 
                   result.get("from").equals("EUR"));
        assertTrue("to must be the same as in the request", 
                   result.get("to").equals("USD"));
         assertTrue("Avalilable rates to coin must be double (or Long for 1)", 
                   result.get("value") instanceof Double || result.get("value") instanceof Long);
        
    }
    

    
}
