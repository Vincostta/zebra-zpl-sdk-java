package com.github.vincostta.zpl.element;

public interface ZplElement {

    String getZplCode();

    // Novos métodos para validação de layout
    int getX();
    int getY();
    int getWidth();
    int getHeight();
}