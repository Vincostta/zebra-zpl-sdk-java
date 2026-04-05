package com.github.vincostta.zpl.element;

/**
 * Elemento para desenho de elipses (ovais) na etiqueta ZPL.
 * Implementa a interface expandida para suporte a validação de layout.
 */
public class EllipseElement implements ZplElement {

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final int thickness;

    /**
     * @param x Posição X (dots)
     * @param y Posição Y (dots)
     * @param width Largura da elipse (dots)
     * @param height Altura da elipse (dots)
     * @param thickness Espessura da linha (dots)
     */
    public EllipseElement(int x, int y, int width, int height, int thickness) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.thickness = thickness;
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
        // ^GE: Graphic Ellipse (Width, Height, Thickness, Color)
        // O parâmetro 'B' indica cor Preta (Black) para a linha.
        return String.format("^FO%d,%d^GE%d,%d,%d,B^FS", x, y, width, height, thickness);
    }
}