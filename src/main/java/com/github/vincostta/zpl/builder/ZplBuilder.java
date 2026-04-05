package com.github.vincostta.zpl.builder;

import com.github.vincostta.zpl.core.ZplLabelBuilder;

/**
 * Ponto de entrada principal para a criação de etiquetas ZPL.
 * Esta classe facilita o início da construção de uma etiqueta com DPI configurável.
 */
public class ZplBuilder {

    /**
     * Inicia a construção de uma etiqueta definindo dimensões e DPI.
     *
     * @param widthMm  Largura da etiqueta em milímetros.
     * @param heightMm Altura da etiqueta em milímetros.
     * @param dpi      Densidade de pontos (ex: 203 para impressoras desktop comuns).
     * @return Um ZplLabelBuilder pronto para receber elementos.
     */
    public static ZplLabelBuilder label(int widthMm, int heightMm, int dpi) {
        return new ZplLabelBuilder(widthMm, heightMm, dpi);
    }

    /**
     * Atalho para etiquetas padrão de 203 DPI (8 dots/mm).
     */
    public static ZplLabelBuilder label(int widthMm, int heightMm) {
        return new ZplLabelBuilder(widthMm, heightMm, 203);
    }
}