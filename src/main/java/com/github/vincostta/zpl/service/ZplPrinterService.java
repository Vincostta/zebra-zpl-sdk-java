package com.github.vincostta.zpl.service;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ZplPrinterService {
    private final String ip;
    private final int port;
    private static final int STATUS_TIMEOUT = 2000;
    private static final int WRITE_TIMEOUT = 5000;

    public ZplPrinterService(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    // Getter necessário para o ZplLogger
    public String getIp() {
        return this.ip;
    }

    /**
     * Envia o comando ZPL para a impressora via TCP/IP.
     */
    public void sendToPrinter(String zpl) throws Exception {
        if (zpl == null || zpl.trim().isEmpty()) return;

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(ip, port), STATUS_TIMEOUT);
            socket.setSoTimeout(WRITE_TIMEOUT);

            try (DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
                // UTF-8 para suportar acentos via ^CI28 no Builder
                byte[] data = zpl.getBytes(StandardCharsets.UTF_8);
                out.write(data);
                out.flush();
            }
        }
    }

    // --- Comandos de Suporte e Manutenção ---

    /**
     * Força a impressora a calibrar os sensores de papel e ribbon.
     */
    public void calibrate() throws Exception {
        sendToPrinter("~JC^XA^JUS^XZ");
    }

    /**
     * Imprime a página de teste interna com as configurações atuais.
     */
    public void printTestPage() throws Exception {
        sendToPrinter("~WC");
    }

    /**
     * Limpa o buffer de memória da impressora.
     */
    public void clearBuffer() throws Exception {
        sendToPrinter("~JA");
    }

    // --- Monitoramento de Status ---

    /**
     * Verifica o estado físico da impressora (Papel, Ribbon, Pausa).
     */
    public PrinterStatus getStatus() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(ip, port), STATUS_TIMEOUT);
            socket.setSoTimeout(STATUS_TIMEOUT);

            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            // ~HS (Host Status) solicita 3 linhas de dados hex/decimais
            out.write("~HS".getBytes(StandardCharsets.US_ASCII));
            out.flush();

            byte[] buffer = new byte[1024];
            int read = in.read(buffer);

            if (read > 0) {
                String response = new String(buffer, 0, read, StandardCharsets.US_ASCII);
                return parseStatus(response);
            }
        } catch (Exception e) {
            return new PrinterStatus(false, "Offline: " + e.getMessage());
        }
        return new PrinterStatus(false, "Sem Resposta");
    }

    private PrinterStatus parseStatus(String raw) {
        try {
            // Limpa caracteres de controle ASCII STX e ETX
            String clean = raw.replaceAll("[\\u0002\\u0003]", "");
            String[] lines = clean.split("[\\r\\n]+");

            if (lines.length < 1) return new PrinterStatus(false, "Resposta Inválida");

            // Primeira linha contém Paper Out e Pause
            String[] parts = lines[0].split(",");
            boolean paperOut = "1".equals(parts[1].trim());
            boolean pause    = "1".equals(parts[2].trim());

            // Segunda linha contém Ribbon Out (primeiro campo)
            boolean ribbonOut = false;
            if (lines.length > 1) {
                String[] parts2 = lines[1].split(",");
                ribbonOut = "1".equals(parts2[0].trim());
            }

            if (paperOut) return new PrinterStatus(true, "Erro: Sem Papel");
            if (ribbonOut) return new PrinterStatus(true, "Erro: Sem Ribbon");
            if (pause) return new PrinterStatus(true, "Pausada");

            return new PrinterStatus(true, "Pronta");
        } catch (Exception e) {
            return new PrinterStatus(true, "Erro ao interpretar status");
        }
    }
}