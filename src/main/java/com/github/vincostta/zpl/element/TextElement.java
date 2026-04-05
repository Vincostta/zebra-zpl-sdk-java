package com.github.vincostta.zpl.element;

/**
 * Elemento de texto avançado com suporte a rotação, tamanho customizado, negrito e inversão de cor.
 * Implementa a interface expandida para permitir validação de limites.
 */
public class TextElement implements ZplElement {

    private final int x;
    private final int y;
    private final String text;

    private int height = 30;
    private int width = 30;
    private int rotation = 0;
    private boolean bold = false;
    private boolean reverseColor = false;

    public TextElement(int x, int y, String text) {
        this.x = x;
        this.y = y;
        this.text = text;
    }

    // ===== CONFIGURAÇÕES (Fluent API interna) =====

    public TextElement size(int height, int width) {
        this.height = height;
        this.width = width;
        return this;
    }

    public TextElement rotate(int degrees) {
        this.rotation = degrees;
        return this;
    }

    public TextElement bold() {
        this.bold = true;
        return this;
    }

    public TextElement reverse() {
        this.reverseColor = true;
        return this;
    }

    // ===== IMPLEMENTAÇÃO DA INTERFACE ZPLELEMENT (VALIDAÇÃO) =====

    @Override
    public int getX() { return x; }

    @Override
    public int getY() { return y; }

    /**
     * Estima a largura total ocupada pelo texto.
     * Se rotacionado em 90 ou 270, a largura passa a ser a altura da fonte.
     */
    @Override
    public int getWidth() {
        if (rotation == 90 || rotation == 270) {
            return height;
        }
        // Estimativa: largura de cada caractere multiplicada pelo total de caracteres
        return text.length() * width;
    }

    /**
     * Estima a altura total ocupada pelo texto.
     * Se rotacionado em 90 ou 270, a altura passa a ser a largura acumulada dos caracteres.
     */
    @Override
    public int getHeight() {
        if (rotation == 90 || rotation == 270) {
            return text.length() * width;
        }
        return height;
    }

    // ===== GERAÇÃO DO CÓDIGO ZPL =====

    @Override
    public String getZplCode() {
        String rot = switch (rotation) {
            case 90 -> "R";
            case 180 -> "I";
            case 270 -> "B";
            default -> "N";
        };

        StringBuilder zpl = new StringBuilder();

        // Comando para desenhar o texto
        zpl.append("^FO").append(x).append(",").append(y);
        if (reverseColor) zpl.append("^FR");
        zpl.append("^A0").append(rot).append(",").append(height).append(",").append(width)
                .append("^FD").append(text).append("^FS");

        // Simulação de Negrito (Bold)
        if (bold) {
            zpl.append("\n^FO").append(x + 1).append(",").append(y);
            if (reverseColor) zpl.append("^FR");
            zpl.append("^A0").append(rot).append(",").append(height).append(",").append(width)
                    .append("^FD").append(text).append("^FS");
        }

        return zpl.toString();
    }
}