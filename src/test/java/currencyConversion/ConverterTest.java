/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package currencyConversion;

import java.util.HashMap;
import java.util.Set;
import org.easymock.EasyMock;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author tkardozo
 */
public class ConverterTest {
    
    private Converter cc;
    private LiveRates mock;
    
    public ConverterTest() {
    }
        
    @Before
    public void setUp() {
        this.mock = EasyMock.createMock(LiveRates.class);
        this.cc = new Converter();
        this.cc.setLiveCurrencyRates(this.mock);
        
        EasyMock.expect( this.mock.getUpdatedRates() ).andReturn(
            new HashMap<String, Double>() {{
                put("AED", 0.80);
                put("USD", 1.00);
                put("EUR", 2.00);
            }}
        );
        EasyMock.replay( this.mock );
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testCoins() {      
        Set<String> result = cc.getAvailableCoins();
         assertEquals("Should have 3 different coins available",
                3, result.size());
        assertTrue("Should contain AED in available coins",
                result.contains("AED"));
        assertTrue("Should contain USD in available coins",
                result.contains("USD"));
        assertTrue("Should contain EUR in available coins",
                result.contains("EUR"));
    }

    @Test
    public void testRates() throws Exception {
        assertEquals(new HashMap<String, Double>() {{
                        put("AED", 1.00);
                        put("USD", 1.25);
                        put("EUR", 2.50);
                    }}
                , this.cc.getRates("AED"));
        
        assertEquals(new HashMap<String, Double>() {{
                        put("AED", 0.80);
                        put("USD", 1.00);
                        put("EUR", 2.00);
                    }}
                , this.cc.getRates("USD"));
        
        assertEquals(new HashMap<String, Double>() {{
                        put("AED", 0.40);
                        put("USD", 0.50);
                        put("EUR", 1.00);
                    }}
                , this.cc.getRates("EUR"));
    }

    @Test(expected = Exception.class)
    public void testGetRatesExc() throws Exception {
        this.cc.getRates("NON");
    }
    
    @Test
    public void testGetRate() throws Exception {
        System.out.println("getRate");
        
        assertTrue("AED to AED should be 1",
                this.cc.getRate("AED", "AED")==1.00);
        assertTrue("AED to USD should be 1.25",
                this.cc.getRate("AED", "USD")==1.25);
        assertTrue("AED to EUR should be 2.5",
                this.cc.getRate("AED", "EUR")==2.5);
        
        assertTrue("USD to AED should be 0.8",
                this.cc.getRate("USD", "AED")==0.80);
        assertTrue("USD to USD should be 1",
                this.cc.getRate("USD", "USD")==1.00);
        assertTrue("USD to EUR should be 2",
                this.cc.getRate("USD", "EUR")==2.00);
        
        assertTrue("EUR to AED should be 0.4",
                this.cc.getRate("EUR", "AED")==0.40);
        assertTrue("EUR to USD should be 0.5",
                this.cc.getRate("EUR", "USD")==0.50);
        assertTrue("EUR to EUR should be 1",
                this.cc.getRate("EUR", "EUR")==1.00);
    }
    
    @Test(expected = Exception.class)
    public void testGetRateExc() throws Exception {
        this.cc.getRate("EUR", "NON");
    }
    
    @Test(expected = Exception.class)
    public void testGetRateExc1() throws Exception {
        this.cc.getRate("NON", "EUR");
    }

    @Test
    public void testConvert() throws Exception {
        System.out.println("convert");
        
        for(double i=0; i<10; i+=0.1){
            assertTrue("AED to AED should be " + 1.00*i,
                this.cc.convert("AED", "AED", i)==1.00*i);
        assertTrue("AED to USD should be " + 1.25*i,
                this.cc.convert("AED", "USD", i)==1.25*i);
        assertTrue("AED to EUR should be " + 2.5*i,
                this.cc.convert("AED", "EUR", i)==2.5*i);
        
        assertTrue("USD to AED should be " + 0.80*i,
                this.cc.convert("USD", "AED", i)==0.80*i);
        assertTrue("USD to USD should be " + 1*i,
                this.cc.convert("USD", "USD", i)==1.00*i);
        assertTrue("USD to EUR should be " + 2.00*i,
                this.cc.convert("USD", "EUR", i)==2.00*i);
        
        assertTrue("EUR to AED should be " + 0.40*i,
                this.cc.convert("EUR", "AED", i)==0.40*i);
        assertTrue("EUR to USD should be " + 0.50*i,
                this.cc.convert("EUR", "USD", i)==0.50*i);
        assertTrue("EUR to EUR should be " + 1*i,
                this.cc.convert("EUR", "EUR", i)==1.00*i);
        }

    }
    
}
