package com.github.vincostta.zpl.core;

import com.github.vincostta.zpl.builder.TextBuilder;
import com.github.vincostta.zpl.builder.BarcodeBuilder;
import com.github.vincostta.zpl.builder.QrCodeBuilder;
import com.github.vincostta.zpl.element.*;
import java.util.ArrayList;
import java.util.List;

public class ZplLabelBuilder {
    private final float dpmm;
    private final int dpi; // Armazenamos o DPI para cálculos de Auto-Fit
    private final int widthDots;
    private final int heightDots;
    private final List<ZplElement> elements = new ArrayList<>();
    private boolean hasBorder = false;

    public ZplLabelBuilder(int widthMm, int heightMm, int dpi) {
        this.dpi = dpi;
        this.dpmm = dpi / 25.4f;
        this.widthDots = Math.round(widthMm * dpmm);
        this.heightDots = Math.round(heightMm * dpmm);
    }

    // Mudamos para public para que os Builders (TextBuilder) possam usar
    public int mmToDots(int mm) {
        return Math.round(mm * dpmm);
    }

    // Getter necessário para o cálculo de Auto-Fit no TextBuilder
    public int getDpi() {
        return this.dpi;
    }

    public ZplLabelBuilder withBorder() {
        this.hasBorder = true;
        return this;
    }

    // --- Geometria ---

    public ZplLabelBuilder line(int xMm, int yMm, int widthMm) {
        this.addElement(new GraphicElement(mmToDots(xMm), mmToDots(yMm), mmToDots(widthMm), 1, 2));
        return this;
    }

    public ZplLabelBuilder vLine(int xMm, int yMm, int heightMm) {
        this.addElement(new GraphicElement(mmToDots(xMm), mmToDots(yMm), 1, mmToDots(heightMm), 2));
        return this;
    }

    public ZplLabelBuilder circle(int xMm, int yMm, int diameterMm, int thicknessMm) {
        this.addElement(new CircleElement(mmToDots(xMm), mmToDots(yMm), mmToDots(diameterMm), mmToDots(thicknessMm)));
        return this;
    }

    public ZplLabelBuilder ellipse(int xMm, int yMm, int widthMm, int heightMm, int thicknessMm) {
        this.addElement(new EllipseElement(mmToDots(xMm), mmToDots(yMm), mmToDots(widthMm), mmToDots(heightMm), mmToDots(thicknessMm)));
        return this;
    }

    // --- Fluent API entry points ---

    public TextBuilder text(String texto) {
        return new TextBuilder(this, texto);
    }

    /**
     * Implementação do Auto-Fit diretamente no ponto de entrada.
     * @param texto O conteúdo
     * @param maxWidthMm Largura máxima permitida para este texto em milímetros
     */
    public TextBuilder textAutoFit(String texto, int maxWidthMm) {
        // Cálculo rápido de pontos: (LarguraDots / (Caracteres * 0.7))
        int maxWidthDots = mmToDots(maxWidthMm);
        int estimatedHeight = (int) (maxWidthDots / (texto.length() * 0.7));

        // Limita entre 15 (pequeno) e 40 (título)
        int finalSize = Math.max(15, Math.min(40, estimatedHeight));

        return new TextBuilder(this, texto).size(finalSize, finalSize);
    }

    public BarcodeBuilder barcode(String data, int heightMm) {
        return new BarcodeBuilder(this, data, mmToDots(heightMm));
    }

    public QrCodeBuilder qrCode(String data, int size) {
        return new QrCodeBuilder(this, data, size);
    }

    // --- Core Management ---

    public void addElement(ZplElement element) {
        if (element == null) return;

        // Validação de bordas (Apenas Log, não impede a criação)
        if (element.getX() + element.getWidth() > this.widthDots) {
            System.err.printf("⚠️ AVISO: [%s] ultrapassa LARGURA! (%d > %d)%n",
                    element.getClass().getSimpleName(), (element.getX() + element.getWidth()), this.widthDots);
        }

        if (element.getY() + element.getHeight() > this.heightDots) {
            System.err.printf("⚠️ AVISO: [%s] ultrapassa ALTURA! (%d > %d)%n",
                    element.getClass().getSimpleName(), (element.getY() + element.getHeight()), this.heightDots);
        }

        this.elements.add(element);
    }

    public String build() {
        StringBuilder zpl = new StringBuilder();
        zpl.append("^XA\n^CI28\n"); // UTF-8 Support
        zpl.append("^PW").append(widthDots).append("\n");
        zpl.append("^LL").append(heightDots).append("\n");

        if (hasBorder) {
            zpl.append(new GraphicElement(2, 2, widthDots - 4, heightDots - 4, 2).getZplCode()).append("\n");
        }

        for (ZplElement element : elements) {
            zpl.append(element.getZplCode()).append("\n");
        }

        zpl.append("^XZ");
        return zpl.toString();
    }
}