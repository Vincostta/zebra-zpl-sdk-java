package com.github.vincostta.zpl.element;

/**
 * Elemento para desenho de QR Codes.
 * Implementa a interface expandida para suporte a validação de layout.
 */
public class QrCodeElement implements ZplElement {
    private final int x;
    private final int y;
    private final String data;
    private final int size;

    /**
     * @param x Posição horizontal (dots)
     * @param y Posição vertical (dots)
     * @param data Conteúdo do QR Code
     * @param size Magnificação (1 a 10). Padrão recomendado: 6.
     */
    public QrCodeElement(int x, int y, String data, int size) {
        this.x = x;
        this.y = y;
        this.data = data;
        this.size = size;
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
     * Calcula o tamanho total do QR Code em dots.
     * Um QR Code típico tem entre 25x25 e 33x33 módulos.
     * Usamos uma média de 33 módulos * magnification (size) para validação de segurança.
     */
    private int calculateTotalSize() {
        if (data == null) return 0;
        // 33 módulos é um tamanho padrão para URLs curtas/IDs.
        // Adicionamos 4 módulos de "Quiet Zone" (margem obrigatória do QR Code).
        return (33 + 4) * size;
    }

    @Override
    public int getWidth() {
        return calculateTotalSize();
    }

    @Override
    public int getHeight() {
        return calculateTotalSize();
    }

    @Override
    public String getZplCode() {
        // ^BQN: QR Code
        // 2: Modelo 2 (Padrão moderno)
        // %d (size): Tamanho do módulo
        // ^FDQA,: O "QA" é necessário no ZPL para indicar correção de erro padrão (Level M)
        return String.format("^FO%d,%d^BQN,2,%d^FDQA,%s^FS", x, y, size, data);
    }
}