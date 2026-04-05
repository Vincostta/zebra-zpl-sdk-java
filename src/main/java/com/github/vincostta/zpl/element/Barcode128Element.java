package com.github.vincostta.zpl.element;

/**
 * Elemento para desenho de códigos de barras Code 128.
 * Implementa a interface expandida para suporte a validação de layout.
 */
public class Barcode128Element implements ZplElement {
    private final int x;
    private final int y;
    private final int height;
    private final String data;

    /**
     * @param x Posição horizontal (dots)
     * @param y Posição vertical (dots)
     * @param data Conteúdo do código de barras
     * @param height Altura das barras (dots)
     */
    public Barcode128Element(int x, int y, String data, int height) {
        this.x = x;
        this.y = y;
        this.data = data;
        this.height = height;
    }

    // ===== IMPLEMENTAÇÃO DA INTERFACE ZPLELEMENT =====

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    /**
     * Estima a largura do código de barras.
     * O Code 128 varia, mas uma estimativa segura é multiplicar o tamanho dos dados
     * por um fator médio (ex: 11 dots por caractere + quiet zones).
     */
    @Override
    public int getWidth() {
        if (data == null) return 0;
        // Estimativa conservadora para evitar cortes na impressão
        return (data.length() * 11) + 22;
    }

    @Override
    public int getHeight() {
        // A altura real inclui as barras + o texto interpretativo (Human Readable)
        // Adicionamos uma margem de ~25% para o texto abaixo das barras
        return (int) (height * 1.25);
    }

    @Override
    public String getZplCode() {
        // ^BC: Code 128
        // N: Orientação normal
        // height: Altura em dots
        // Y: Imprime a interpretação do texto (Human Readable) abaixo das barras
        // N: Não imprime o texto acima
        // N: Não imprime o dígito de controle (o Code 128 calcula internamente)
        return String.format("^FO%d,%d^BCN,%d,Y,N,N^FD%s^FS", x, y, height, data);
    }
}