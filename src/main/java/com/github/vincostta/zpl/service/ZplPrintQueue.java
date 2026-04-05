package com.github.vincostta.zpl.service;

import com.github.vincostta.zpl.util.ZplLogger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ZplPrintQueue {
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private final ZplPrinterService printerService;
    private boolean running = true;

    public ZplPrintQueue(ZplPrinterService printerService) {
        this.printerService = printerService;
        startWorker();
    }

    public void add(String zpl) {
        queue.add(zpl);
    }

    private void startWorker() {
        Thread worker = new Thread(() -> {
            while (running) {
                try {
                    // 1. Verifica o estado físico da impressora
                    PrinterStatus status = printerService.getStatus();

                    if (status.isReady()) {
                        // 2. Tenta tirar da fila (espera 1s se estiver vazia)
                        String zpl = queue.poll(1, TimeUnit.SECONDS);

                        if (zpl != null) {
                            try {
                                printerService.sendToPrinter(zpl);
                                ZplLogger.log(printerService.getIp(), "SUCESSO", "Etiqueta enviada com sucesso.");
                                Thread.sleep(300); // Fôlego para o buffer de hardware
                            } catch (Exception e) {
                                ZplLogger.log(printerService.getIp(), "ERRO_ENVIO", e.getMessage());
                            }
                        }
                    } else {
                        // 3. Impressora com erro (Sem papel, pausada, etc)
                        ZplLogger.log(printerService.getIp(), "RETIDO", status.getMessage());
                        System.err.println("⚠️ Impressão retida: " + status.getMessage());
                        Thread.sleep(5000); // Espera 5s antes de checar o status novamente
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    ZplLogger.log(printerService.getIp(), "ERRO_CRITICO", e.getMessage());
                    try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
                }
            }
        });
        worker.setDaemon(true); // Encerra a thread se o app principal fechar
        worker.start();
    }

    public void stop() {
        this.running = false;
    }
}