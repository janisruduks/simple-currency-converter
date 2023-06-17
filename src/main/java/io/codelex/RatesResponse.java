package io.codelex.custom.currencyconverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class RatesResponse {
    private Map<String, BigDecimal> rates;

    public RatesResponse(Map<String, BigDecimal> rates) {
        this.rates = rates;
    }

    public RatesResponse(){} // do not remove or Jackson won't like it

    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    public BigDecimal getConversion(String currencyFrom, String currencyTo, BigDecimal amount) {
        BigDecimal valueFrom = rates.get(currencyFrom);
        BigDecimal valueTo = rates.get(currencyTo);
        return amount.divide(valueFrom, 10, RoundingMode.HALF_EVEN).multiply(valueTo).setScale(2, RoundingMode.HALF_EVEN);
    }

   public boolean doesRateExists(String currency) {
       return rates.containsKey(currency);
   }
}