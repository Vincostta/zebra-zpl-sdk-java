package com.github.vincostta.zpl.builder;

import com.github.vincostta.zpl.core.ZplLabelBuilder;
import com.github.vincostta.zpl.element.Barcode128Element;

/**
 * Construtor fluente para códigos de barras (Padrão Code 128).
 * Essencial para logística e identificação.
 */
public class BarcodeBuilder {

    private final ZplLabelBuilder parent;
    private final String data;
    private final int heightDots;
    private int x = 0;
    private int y = 0;

    /**
     * O construtor agora recebe a altura vinda do Builder principal.
     */
    public BarcodeBuilder(ZplLabelBuilder parent, String data, int heightDots) {
        this.parent = parent;
        this.data = data;
        this.heightDots = heightDots;
    }

    /**
     * Posiciona o código de barras na etiqueta.
     * @param xMm Posição X em milímetros a partir da borda esquerda.
     * @param yMm Posição Y em milímetros a partir do topo.
     */
    public BarcodeBuilder at(int xMm, int yMm) {
        this.x = parent.mmToDots(xMm);
        this.y = parent.mmToDots(yMm);
        return this;
    }

    /**
     * Finaliza a configuração do código de barras e o adiciona ao Builder principal.
     * Graças à interface ZplElement, o método addElement validará se o código cabe na etiqueta.
     */
    public ZplLabelBuilder end() {
        parent.addElement(new Barcode128Element(x, y, data, heightDots));
        return parent;
    }
}