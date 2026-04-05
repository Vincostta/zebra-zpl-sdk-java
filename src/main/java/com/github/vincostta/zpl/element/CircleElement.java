package com.github.vincostta.zpl.element;

/**
 * Elemento para desenho de círculos na etiqueta ZPL.
 * Implementa a interface expandida para suporte a validação de layout.
 */
public class CircleElement implements ZplElement {

    private final int x;
    private final int y;
    private final int diameter;
    private final int thickness;

    /**
     * @param x Posição X (dots)
     * @param y Posição Y (dots)
     * @param diameter Diâmetro do círculo (dots)
     * @param thickness Espessura da linha (dots)
     */
    public CircleElement(int x, int y, int diameter, int thickness) {
        this.x = x;
        this.y = y;
        this.diameter = diameter;
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
        // Num círculo, a largura é o próprio diâmetro
        return diameter;
    }

    @Override
    public int getHeight() {
        // Num círculo, a altura é o próprio diâmetro
        return diameter;
    }

    @Override
    public String getZplCode() {
        // ^GC: Graphic Circle (Diâmetro, Espessura, Cor da borda)
        // O parâmetro 'B' indica cor Preta (Black)
        return String.format("^FO%d,%d^GC%d,%d,B^FS", x, y, diameter, thickness);
    }
}