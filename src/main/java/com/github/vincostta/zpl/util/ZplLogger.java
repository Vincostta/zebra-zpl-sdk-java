package com.github.vincostta.zpl.util;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ZplLogger {
    private static final String LOG_FILE = "print_history.csv";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Registra um evento de impressão no arquivo CSV.
     */
    public static synchronized void log(String ip, String status, String info) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {

            String timestamp = LocalDateTime.now().format(formatter);
            // Formato: Data/Hora ; IP ; Status ; Detalhes
            pw.printf("%s;%s;%s;%s%n", timestamp, ip, status, info);

        } catch (Exception e) {
            System.err.println("❌ Erro ao gravar log de impressão: " + e.getMessage());
        }
    }
}