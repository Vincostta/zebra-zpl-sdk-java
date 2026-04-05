package com.github.vincostta.zpl.core;

import com.github.vincostta.zpl.element.ZplElement; // Importação essencial
import java.util.ArrayList;
import java.util.List;

/**
 * Representa o modelo final da etiqueta ZPL.
 * Armazena as dimensões e a lista de elementos que compõem a impressão.
 */
public class ZplLabel {

    private final int width;
    private final int height;
    private final List<ZplElement> elements = new ArrayList<>();

    /**
     * Construtor para definir as dimensões da etiqueta em dots.
     */
    public ZplLabel(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Adiciona um elemento (Texto, Barcode, etc) à etiqueta.
     */
    public ZplLabel addElement(ZplElement element) {
        if (element != null) {
            this.elements.add(element);
        }
        return this;
    }

    /**
     * Gera o código ZPL final formatado para a impressora.
     */
    public String build() {
        StringBuilder zpl = new StringBuilder();

        // ^XA: Início do formato
        zpl.append("^XA\n");

        // ^PW: Define a largura da etiqueta em dots (Print Width)
        zpl.append("^PW").append(width).append("\n");

        // ^LL: Define o comprimento da etiqueta em dots (Label Length)
        zpl.append("^LL").append(height).append("\n");

        // Itera sobre cada elemento injetando seu código específico
        for (ZplElement element : elements) {
            zpl.append(element.getZplCode()).append("\n");
        }

        // ^XZ: Fim do formato
        zpl.append("^XZ");

        return zpl.toString();
    }
}