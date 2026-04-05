package com.github.vincostta.zpl.builder;

import com.github.vincostta.zpl.core.ZplLabelBuilder;
import com.github.vincostta.zpl.element.TextElement;

/**
 * Construtor fluente para elementos de texto.
 * Permite posicionar e configurar textos na etiqueta usando milímetros para coordenadas.
 */
public class TextBuilder {

    private final ZplLabelBuilder parent;
    private final String texto;
    private int x = 0, y = 0;
    private int height = 30; // Padrão em dots
    private int width = 30;  // Padrão em dots
    private int rotation = 0;
    private boolean bold = false;
    private boolean reverseColor = false;

    public TextBuilder(ZplLabelBuilder parent, String texto) {
        this.parent = parent;
        this.texto = texto;
    }

    /**
     * Define a posição do texto na etiqueta.
     * @param xMm Posição X em milímetros.
     * @param yMm Posição Y em milímetros.
     */
    public TextBuilder at(int xMm, int yMm) {
        this.x = parent.mmToDots(xMm);
        this.y = parent.mmToDots(yMm);
        return this;
    }

    /**
     * Define o tamanho da fonte em dots.
     */
    public TextBuilder size(int heightDots, int widthDots) {
        this.height = heightDots;
        this.width = widthDots;
        return this;
    }

    /**
     * Define a rotação do texto (0, 90, 180, 270).
     */
    public TextBuilder rotate(int degrees) {
        this.rotation = degrees;
        return this;
    }

    /**
     * Aplica o efeito de negrito ao texto.
     */
    public TextBuilder bold() {
        this.bold = true;
        return this;
    }

    /**
     * Ativa a inversão de cor (imprime branco sobre fundo preto).
     * Útil para destacar informações sobre faixas pretas (^FR).
     */
    public TextBuilder reverse() {
        this.reverseColor = true;
        return this;
    }

    /**
     * Finaliza a configuração do texto, cria o Element e o adiciona ao Builder principal.
     */
    public ZplLabelBuilder end() {
        // Criamos o elemento com as coordenadas já convertidas para dots
        TextElement element = new TextElement(x, y, texto);

        // Configurei o elemento usando sua própria Fluent API interna
        element.size(height, width)
                .rotate(rotation);

        if (bold) {
            element.bold();
        }

        if (reverseColor) {
            element.reverse();
        }

        // Adiciona ao parent para que passe pela validação de limites (Segurança)
        parent.addElement(element);

        return parent;
    }
}