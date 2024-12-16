package com.example.currencyconverter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class CurrencyConverter {

    private static final String API_KEY = "a081183f7b3403f9cc78f2f0"; // Reemplaza con tu API Key
    private static final String BASE_URL = "https://api.exchangerate-api.com/v4/latest/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("Bienvenido al conversor de moneda, elija una de las siguientes opciones:");

        while (running) {
            showMenu();
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    convertCurrency("USD", "ARS", scanner);
                    break;
                case 2:
                    convertCurrency("ARS", "USD", scanner);
                    break;
                case 3:
                    convertCurrency("USD", "BRL", scanner);
                    break;
                case 4:
                    convertCurrency("BRL", "USD", scanner);
                    break;
                case 5:
                    convertCurrency("USD", "COP", scanner);
                    break;
                case 6:
                    convertCurrency("COP", "USD", scanner);
                    break;
                case 7:
                    System.out.println("Gracias por usar el conversor de moneda. ¡Hasta luego!");
                    running = false;
                    break;
                default:
                    System.out.println("Opción no válida, por favor intente de nuevo.");
            }
        }

        scanner.close();
    }

    private static void showMenu() {
        System.out.println("\n1- Dólar a Peso argentino");
        System.out.println("2- Peso argentino a Dólar");
        System.out.println("3- Dólar a Real brasileño");
        System.out.println("4- Real brasileño a Dólar");
        System.out.println("5- Dólar a Peso colombiano");
        System.out.println("6- Peso colombiano a Dólar");
        System.out.println("7- Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static void convertCurrency(String from, String to, Scanner scanner) {
        System.out.printf("Ingrese el valor en %s que desea convertir: ", from);
        double amount = scanner.nextDouble();

        double rate = getExchangeRate(from, to);
        if (rate == -1) {
            System.out.println("No se pudo obtener la tasa de cambio. Por favor, intente más tarde.");
            return;
        }

        double convertedAmount = amount * rate;
        System.out.printf("El valor de %.2f %s corresponde a %.2f %s.\n", amount, from, convertedAmount, to);
    }

    private static double getExchangeRate(String from, String to) {
        try {
            String url = BASE_URL + from;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("apikey", API_KEY)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
                JsonObject rates = jsonObject.getAsJsonObject("rates");
                return rates.get(to).getAsDouble();
            } else {
                System.out.println("Error en la API: " + response.statusCode());
                return -1;
            }
        } catch (Exception e) {
            System.out.println("Error al conectar con la API: " + e.getMessage());
            return -1;
        }
    }
}
