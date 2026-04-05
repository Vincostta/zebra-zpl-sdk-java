package com.github.vincostta.zpl.service;

import java.awt.Desktop;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Utilitário para abrir o visualizador online do Labelary diretamente no navegador padrão.
 */
public class ZplPreviewer {

    /**
     * Codifica o ZPL e abre o visualizador oficial do Labelary.
     * Nota: URLs têm limites de caracteres (geralmente ~2000).
     * Para etiquetas muito complexas com imagens grandes, prefira o LabelaryService (POST).
     *
     * @param zpl String ZPL gerada pelo builder.
     */
    public static void openInBrowser(String zpl) {
        try {
            // O Labelary Viewer usa o parâmetro 'zpl' na query string
            String encodedZpl = URLEncoder.encode(zpl, StandardCharsets.UTF_8)
                    .replace("+", "%20"); // Garante compatibilidade com espaços

            String url = "http://labelary.com/viewer.html?zpl=" + encodedZpl;

            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
                System.out.println("🚀 Abrindo preview no navegador...");
            } else {
                // Caso rode em servidores ou ambientes sem interface gráfica
                System.out.println("🌐 Visualização disponível em: " + url);
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao abrir navegador: " + e.getMessage());
        }
    }
}