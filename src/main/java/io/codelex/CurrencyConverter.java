package io.codelex.custom.currencyconverter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Scanner;

public class CurrencyConverter {
    private static final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final RatesResponse resp;

    static {
        try {
            resp = mapper.readValue(new URL("https://api.exchangerate.host/latest"), RatesResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);

        while (true) {
            System.out.println("CHOOSE:");
            System.out.println("type 'all' to see all rates.");
            System.out.println("type 'search usd' to search for currency and it's value in EUR.");
            System.out.println("Type 'convert from to amount' e.g. 'convert usd eur 100'");
            System.out.println("type 'exit' to exit the program.");
            switch (keyboard.next()) {
                case "all" -> displayRates();
                case "search" -> displayRates(validateCurrency(keyboard.next(), keyboard));
                case "convert" -> convert(keyboard.next(), keyboard.next(), keyboard.next(), keyboard);
                case "exit" -> {
                    System.out.println("Bye!");
                    System.exit(666);
                }
            }
        }
    }

    private static void convert(String currencyFrom, String currencyTo, String amountString, Scanner keyboard) {
        currencyFrom = validateCurrency(currencyFrom, keyboard);
        currencyTo = validateCurrency(currencyTo, keyboard);
        BigDecimal amount  = validateNumber(amountString, keyboard);
        System.out.println(currencyTo + ": " + resp.getConversion(currencyFrom, currencyTo, amount));
    }

    private static String validateCurrency(String currency, Scanner keyboard) {
        currency = currency.toUpperCase();
        while (!resp.doesRateExists(currency)) {
            System.out.print("There is no such currencies try (USD or usd format): ");
            currency = keyboard.next().toUpperCase().replace("\n", "");
        }
        return currency;
    }

    private static BigDecimal validateNumber(String amount, Scanner keyboard) {
        while (!amount.matches("\\d+(\\.\\d+)?")) {
            System.out.print("ERROR: Invalid amount, please enter valid number: ");
            amount = keyboard.next().replace("\n", "");
        }
        return new BigDecimal(amount);
    }

    public static void displayRates() {
        resp.getRates().forEach((k, v) -> System.out.println(k + ":" + v));
    }

    public static void displayRates(String currency) {
        resp.getRates().forEach((k, v) -> {
            if (currency.toUpperCase().equals(k)) {
                System.out.println(k + ":" + v);
            }
        });
    }
}