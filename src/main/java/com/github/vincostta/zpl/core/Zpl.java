package com.github.vincostta.zpl.core;

/**
 * Ponto de entrada simplificado para criar etiquetas diretamente em dots.
 */
public final class Zpl {

    // Construtor privado para impedir instanciação (Padrão Utility Class)
    private Zpl() {}

    /**
     * Cria uma nova etiqueta ZplLabel informando as dimensões em dots.
     * @param width Largura em dots.
     * @param height Altura em dots.
     * @return Uma instância de ZplLabel.
     */
    public static ZplLabel label(int width, int height) {
        return new ZplLabel(width, height);
    }
}