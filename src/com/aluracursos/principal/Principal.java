package com.aluracursos.principal;

import com.aluracursos.conversordemonedas.ConsultaMoneda;
import com.aluracursos.conversordemonedas.Monedas;

import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class Principal {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ConsultaMoneda consultaAPI = new ConsultaMoneda();

        // Chequea si la API key fue cargada en el constructor ConsultaMoneda
        if (System.getenv("EXCHANGE_RATE_API_KEY") == null || System.getenv("EXCHANGE_RATE_API_KEY").trim().isEmpty()) {
            System.out.println("Finalizando la aplicación debido a que la API Key no está configurada.");
            scanner.close();
            return; // Termina el programa si la API key no está configurada.
        }

        int opcion = 0;

        while (opcion != 7) {
            mostrarMenu();
            try {
                System.out.print("Elija una opción: ");
                opcion = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                if (opcion == 7) {
                    System.out.println("Saliendo del programa. ¡Gracias por usar el conversor!");
                    break;
                }

                if (opcion < 1 || opcion > 6) {
                    System.out.println("Opción no válida. Por favor, intente de nuevo.");
                    continue;
                }

                System.out.print("Ingrese la cantidad a convertir: ");
                double cantidad = scanner.nextDouble();
                scanner.nextLine(); // Consume newline

                String monedaOrigen = "";
                String monedaDestino = "";

                switch (opcion) {
                    case 1: // USD a ARS
                        monedaOrigen = "USD";
                        monedaDestino = "ARS";
                        break;
                    case 2: // ARS a USD
                        monedaOrigen = "ARS";
                        monedaDestino = "USD";
                        break;
                    case 3: // USD a BRL
                        monedaOrigen = "USD";
                        monedaDestino = "BRL";
                        break;
                    case 4: // BRL a USD
                        monedaOrigen = "BRL";
                        monedaDestino = "USD";
                        break;
                    case 5: // USD a CLP
                        monedaOrigen = "USD";
                        monedaDestino = "CLP";
                        break;
                    case 6: // CLP a USD
                        monedaOrigen = "CLP";
                        monedaDestino = "USD";
                        break;
                    default:
                        System.out.println("Opción desconocida.");
                        continue;
                }

                System.out.println("\nConvirtiendo " + cantidad + " " + monedaOrigen + " a " + monedaDestino + "...");
                Monedas tasas = consultaAPI.obtenerTasasDeCambio(monedaOrigen);

                if (tasas != null && "success".equalsIgnoreCase(tasas.result())) {
                    Map<String, Double> conversionRates = tasas.conversionRates();
                    if (conversionRates.containsKey(monedaDestino)) {
                        double tasaDeCambio = conversionRates.get(monedaDestino);
                        double cantidadConvertida = cantidad * tasaDeCambio;

                        System.out.printf("-----------------------------------------------------\n");
                        System.out.printf("%.2f %s equivale a %.2f %s\n",
                                cantidad, monedaOrigen, cantidadConvertida, monedaDestino);
                        System.out.printf("Tasa de cambio utilizada: 1 %s = %.4f %s\n",
                                monedaOrigen, tasaDeCambio, monedaDestino);
                        System.out.printf("Última actualización de tasas: %s\n", tasas.timeLastUpdateUtc());
                        System.out.printf("-----------------------------------------------------\n\n");
                    } else {
                        System.out.println("No se encontró la tasa de cambio para " + monedaDestino + " desde " + monedaOrigen + ".");
                    }
                } else {
                    System.out.println("No se pudieron obtener las tasas de cambio. Por favor, verifique la API key y su conexión.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número para la opción y para la cantidad.");
                scanner.nextLine();
            } catch (Exception e) {
                System.err.println("Ocurrió un error inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        }
        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n*****************************************************");
        System.out.println("Bienvenido al Conversor de Monedas");
        System.out.println("*****************************************************");
        System.out.println("1. Convertir de Dólar (USD) a Peso Argentino (ARS)");
        System.out.println("2. Convertir de Peso Argentino (ARS) a Dólar (USD)");
        System.out.println("3. Convertir de Dólar (USD) a Real Brasileño (BRL)");
        System.out.println("4. Convertir de Real Brasileño (BRL) a Dólar (USD)");
        System.out.println("5. Convertir de Dólar (USD) a Peso Chileno (CLP)");
        System.out.println("6. Convertir de Peso Chileno (CLP) a Dólar (USD)");
        System.out.println("7. Salir");
        System.out.println("*****************************************************");
    }
}