package com.github.vincostta.zpl.util;

import java.util.Map;

public class ZplTemplateEngine {
    /**
     * Substitui chaves no formato {{chave}} pelos valores de um Map.
     */
    public static String process(String zpl, Map<String, String> data) {
        String processed = zpl;
        for (Map.Entry<String, String> entry : data.entrySet()) {
            processed = processed.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return processed;
    }
}