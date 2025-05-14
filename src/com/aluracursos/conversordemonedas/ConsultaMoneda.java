package com.aluracursos.conversordemonedas;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsultaMoneda {

    private static final String API_BASE_URL = "https://v6.exchangerate-api.com/v6/";
    private String apiKey;

    public ConsultaMoneda() {
        this.apiKey = System.getenv("EXCHANGE_RATE_API_KEY");
        if (this.apiKey == null || this.apiKey.trim().isEmpty()) {
            System.err.println("Error: La variable de entorno EXCHANGE_RATE_API_KEY no está configurada.");
            System.err.println("Por favor, configure la API key antes de ejecutar la aplicación.");
        }
    }

    public Monedas obtenerTasasDeCambio(String monedaBase) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            System.err.println("No se puede realizar la consulta: API key no configurada.");
            return null;
        }

        if (monedaBase == null || monedaBase.trim().isEmpty()) {
            System.err.println("Error: El código de moneda base no puede estar vacío.");
            return null;
        }

        String urlString = API_BASE_URL + apiKey + "/latest/" + monedaBase.toUpperCase();
        URI uri = URI.create(urlString);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Gson gson = new Gson();
                Monedas monedas = gson.fromJson(response.body(), Monedas.class);
                if (!"success".equalsIgnoreCase(monedas.result())) {
                    System.err.println("Error en la respuesta de la API: " + response.body());
                    return null;
                }
                return monedas;
            } else {
                System.err.println("Error en la solicitud HTTP: Código de estado " + response.statusCode());
                System.err.println("Respuesta: " + response.body());
                return null;
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error al conectar con la API de ExchangeRate: " + e.getMessage());
            Thread.currentThread().interrupt();
            return null;
        } catch (JsonSyntaxException e) {
            System.err.println("Error al parsear la respuesta JSON: " + e.getMessage());
            return null;
        }
    }
}