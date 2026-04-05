package com.github.vincostta.zpl.element;

import com.github.vincostta.zpl.util.ImageZplConverter;
import java.awt.image.BufferedImage;

public class ImageElement implements ZplElement {
    private final int x;
    private final int y;
    private final BufferedImage image;

    public ImageElement(int x, int y, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }

    @Override public int getX() { return x; }
    @Override public int getY() { return y; }
    @Override public int getWidth() { return image.getWidth(); }
    @Override public int getHeight() { return image.getHeight(); }

    @Override
    public String getZplCode() {
        return ImageZplConverter.toZplGfa(image, x, y);
    }
}