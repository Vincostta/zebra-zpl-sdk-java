import com.github.vincostta.zpl.core.ZplLabelBuilder;
import com.github.vincostta.zpl.service.ZplPrintQueue;
import com.github.vincostta.zpl.service.ZplPrinterService;
import com.github.vincostta.zpl.util.ZplTemplateEngine;

import java.util.Map;

public class LabelPrintController {

    private final ZplPrintQueue printQueue;

    public LabelPrintController() {
        // Inicializa o serviço com o IP fixo da impressora
        ZplPrinterService printerService = new ZplPrinterService("192.168.1.100", 9100);

        // A fila gerencia o worker que checa status e imprime
        this.printQueue = new ZplPrintQueue(printerService);
    }

    public void imprimirEtiquetaPedido(Map<String, String> dados) {
        // 1. O Designer define o layout
        ZplLabelBuilder builder = new ZplLabelBuilder(100, 50, 203)
                .withBorder()
                .text("PEDIDO: {{ID}}").at(10, 5).bold().end()
                .text("CLIENTE: {{NOME}}").at(10, 15).end()
                .qrCode("https://rastreio.com/{{TRACKING}}", 5).at(70, 10).end()
                .barcode("{{TRACKING}}", 15).at(10, 30).end();

        // 2. O Engine processa os dados dinâmicos
        String zplFinal = ZplTemplateEngine.process(builder.build(), dados);

        // 3. A Fila assume o trabalho pesado
        printQueue.add(zplFinal);

        System.out.println("Solicitação de impressão enviada para a fila.");
    }
}