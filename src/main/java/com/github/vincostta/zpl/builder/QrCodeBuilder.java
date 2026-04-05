package com.github.vincostta.zpl.builder;

import com.github.vincostta.zpl.core.ZplLabelBuilder;
import com.github.vincostta.zpl.element.QrCodeElement;

/**
 * Construtor fluente para QR Codes.
 * Suporta alta densidade de informação para prontuários, URLs e rastreabilidade.
 */
public class QrCodeBuilder {

    private final ZplLabelBuilder parent;
    private final String data;
    private final int size; // Agora final, vindo do método inicial do Builder principal
    private int x = 0;
    private int y = 0;

    /**
     * O construtor recebe os dados básicos e a magnificação.
     */
    public QrCodeBuilder(ZplLabelBuilder parent, String data, int size) {
        this.parent = parent;
        this.data = data;
        // Garante que o size esteja no range permitido pelo ZPL (1 a 10)
        this.size = Math.max(1, Math.min(size, 10));
    }

    /**
     * Posiciona o QR Code na etiqueta.
     * @param xMm Posição X em milímetros.
     * @param yMm Posição Y em milímetros.
     */
    public QrCodeBuilder at(int xMm, int yMm) {
        this.x = parent.mmToDots(xMm);
        this.y = parent.mmToDots(yMm);
        return this;
    }

    /**
     * Finaliza a configuração do QR Code e retorna ao Builder principal.
     * O elemento passará pela validação de largura/altura no addElement.
     */
    public ZplLabelBuilder end() {
        parent.addElement(new QrCodeElement(x, y, data, size));
        return parent;
    }
}