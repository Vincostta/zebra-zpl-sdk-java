package com.github.vincostta.zpl;

import com.github.vincostta.zpl.core.ZplLabelBuilder;
import com.github.vincostta.zpl.service.ZplPreviewer;
import com.github.vincostta.zpl.service.ZplPrinterService;
import com.github.vincostta.zpl.service.ZplPrintQueue;
import com.github.vincostta.zpl.util.ZplTemplateEngine;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            // 1. Definição do Layout (Template)
            // Etiqueta 100x50mm @ 203 DPI
            ZplLabelBuilder builder = new ZplLabelBuilder(100, 50, 203)
                    .withBorder()
                    .text("DESTINATARIO: {{NOME}}")
                    .at(10, 5)
                    .size(35, 30) // Fonte um pouco maior para o nome
                    .bold()
                    .end()
                    .text("CIDADE: {{CIDADE}}")
                    .at(10, 15)
                    .end()
                    .barcode("{{TRACKING_CODE}}", 15) // Altura de 15mm
                    .at(10, 25)
                    .end();

            String rawZpl = builder.build();

            // 2. Dados Dinâmicos (Simulação de uma consulta ao banco/API)
            Map<String, String> pedido = Map.of(
                    "NOME", "VINICIUS COSTA",
                    "CIDADE", "MANAUS - AM",
                    "TRACKING_CODE", "BR123456789"
            );

            // 3. Processamento do Template
            String finalZpl = ZplTemplateEngine.process(rawZpl, pedido);

            // 4. Debug e Preview
            System.out.println("--- ZPL FINAL (UTF-8) ---");
            System.out.println(finalZpl);

            // Abre o preview no navegador via Labelary API
            ZplPreviewer.openInBrowser(finalZpl);

            // 5. Fluxo de Impressão Profissional
            // Configura o serviço com IP da impressora e porta padrão 9100
            ZplPrinterService printerService = new ZplPrinterService("192.168.1.100", 9100);

            // Usamos a Fila para não travar a execução do programa
            ZplPrintQueue printQueue = new ZplPrintQueue(printerService);

            // Envia para a fila de background
            printQueue.add(finalZpl);

            System.out.println("✅ Etiqueta enviada para a fila de impressão.");

        } catch (Exception e) {
            System.err.println("❌ Ocorreu um erro no fluxo de etiqueta: " + e.getMessage());
            e.printStackTrace();
        }
    }
}