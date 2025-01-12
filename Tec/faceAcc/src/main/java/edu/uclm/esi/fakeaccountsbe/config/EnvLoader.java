package edu.uclm.esi.fakeaccountsbe.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class EnvLoader {

    // Método para cargar las variables del archivo .env
    public static void loadEnv(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.lines()
                    .filter(line -> line.contains("=") && !line.startsWith("#"))
                    .forEach(line -> {
                        String[] parts = line.split("=", 2);
                        String key = parts[0].trim();
                        String value = parts[1].trim();
                        // Establecer la variable de entorno en el sistema
                        System.setProperty(key, value);
                    });
        } catch (IOException e) {
            System.err.println("Error al leer el archivo .env: " + e.getMessage());
        }
    }
}