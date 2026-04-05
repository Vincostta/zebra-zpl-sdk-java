package com.github.vincostta.zpl.element;

/**
 * Elemento para desenho de formas geométricas (caixas, linhas e bordas) com suporte a inversão de cor.
 * Implementa a interface expandida para suporte a validação de layout.
 */
public class GraphicElement implements ZplElement {
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final int thickness;
    private boolean reverseColor = false;

    /**
     * @param x Posição horizontal (dots)
     * @param y Posição vertical (dots)
     * @param width Largura da caixa (dots)
     * @param height Altura da caixa (dots)
     * @param thickness Espessura da linha (dots)
     */
    public GraphicElement(int x, int y, int width, int height, int thickness) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.thickness = thickness;
    }

    /**
     * Ativa a inversão de cor (imprime branco sobre fundo preto).
     */
    public GraphicElement reverse() {
        this.reverseColor = true;
        return this;
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

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public String getZplCode() {
        // ^FO: Field Origin
        // ^FR: Field Reverse print (inverte a cor do campo)
        // ^GB: Graphic Box (Width, Height, Thickness, Color, Rounding)

        String frCommand = reverseColor ? "^FR" : "";

        return String.format("^FO%d,%d%s^GB%d,%d,%d^FS", x, y, frCommand, width, height, thickness);
    }
}