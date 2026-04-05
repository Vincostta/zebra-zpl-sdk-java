package com.github.vincostta.zpl.util;

import java.awt.image.BufferedImage;

public class ImageZplConverter {

    /**
     * Converte uma imagem para o comando ZPL ^GFA (Graphic Field ASCII).
     * Aplica o algoritmo de Dithering Floyd-Steinberg para preservar detalhes.
     */
    public static String toZplGfa(BufferedImage image, int x, int y) {
        int width = image.getWidth();
        int height = image.getHeight();

        // 1. Matriz de luminosidade (Grayscale 0-255)
        int[][] pixels = new int[width][height];
        for (int v = 0; v < height; v++) {
            for (int h = 0; h < width; h++) {
                int rgb = image.getRGB(h, v);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                // Fórmula de luminosidade padrão (percepção humana)
                pixels[h][v] = (int) (0.299 * r + 0.587 * g + 0.114 * b);
            }
        }

        // 2. Aplicação do Dithering Floyd-Steinberg
        for (int v = 0; v < height; v++) {
            for (int h = 0; h < width; h++) {
                int oldPixel = pixels[h][v];
                int newPixel = (oldPixel < 128) ? 0 : 255; // Threshold binário
                pixels[h][v] = newPixel;

                int error = oldPixel - newPixel;

                // Distribuição do erro (Matriz de Difusão)
                if (h + 1 < width) pixels[h + 1][v] += error * 7 / 16;
                if (h - 1 >= 0 && v + 1 < height) pixels[h - 1][v + 1] += error * 3 / 16;
                if (v + 1 < height) pixels[h][v + 1] += error * 5 / 16;
                if (h + 1 < width && v + 1 < height) pixels[h + 1][v + 1] += error * 1 / 16;
            }
        }

        // 3. Conversão para Hexadecimal ZPL
        int bytesPerRow = (width + 7) / 8;
        int totalBytes = bytesPerRow * height;

        StringBuilder hexData = new StringBuilder();
        for (int v = 0; v < height; v++) {
            for (int b = 0; b < bytesPerRow; b++) {
                int currentByte = 0;
                for (int bit = 0; bit < 8; bit++) {
                    int h = (b * 8) + bit;
                    if (h < width) {
                        // Se o pixel for preto (0), o bit deve ser 1 no ZPL
                        if (pixels[h][v] == 0) {
                            currentByte |= (1 << (7 - bit));
                        }
                    }
                }
                hexData.append(String.format("%02X", currentByte));
            }
            hexData.append("\n");
        }

        // Comando ^GFA: Graphic Field ASCII
        // Parâmetros: Compressão (A), Total de Bytes, Bytes por Linha, Hexadecimal
        return String.format("^FO%d,%d^GFA,%d,%d,%d,%s^FS",
                x, y, totalBytes, totalBytes, bytesPerRow, hexData.toString());
    }
}