package com.github.vincostta.zpl.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Serviço que utiliza a API do Labelary para converter código ZPL em imagens PNG.
 * Útil para testes e visualização prévia das etiquetas.
 */
public class LabelaryService {

    /**
     * Gera o preview da etiqueta baseando-se nas configurações de densidade e tamanho.
     *
     * @param zpl Código ZPL completo.
     * @param dpmm Pontos por milímetro (ex: 8 para 203dpi, 12 para 300dpi).
     * @param widthMm Largura em milímetros.
     * @param heightMm Altura em milímetros.
     * @return Array de bytes da imagem PNG.
     */
    public byte[] generatePreview(String zpl, int dpmm, int widthMm, int heightMm) throws Exception {
        // Converte mm para polegadas para a URL do Labelary (1 polegada = 25.4mm)
        double widthInch = widthMm / 25.4;
        double heightInch = heightMm / 25.4;

        // Monta a URL dinâmica: /v1/printers/{dpmm}dpmm/labels/{width}x{height}/{index}/
        String url = String.format("http://api.labelary.com/v1/printers/%ddpmm/labels/%.2fx%.2f/0/",
                dpmm, widthInch, heightInch);

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "image/png")
                    .POST(HttpRequest.BodyPublishers.ofString(zpl))
                    .build();

            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new RuntimeException("Erro Labelary (Status " + response.statusCode() + "): " + new String(response.body()));
            }
        }
    }

    /**
     * Salva o preview em um arquivo local para conferência rápida.
     */
    public void savePreviewToFile(String zpl, int dpmm, int widthMm, int heightMm, String fileName) {
        try {
            byte[] imageBytes = generatePreview(zpl, dpmm, widthMm, heightMm);
            Path path = Paths.get(fileName);
            Files.write(path, imageBytes);
            System.out.println("✅ Preview gerado com sucesso: " + path.toAbsolutePath());
        } catch (Exception e) {
            System.err.println("❌ Falha ao gerar preview: " + e.getMessage());
        }
    }
}