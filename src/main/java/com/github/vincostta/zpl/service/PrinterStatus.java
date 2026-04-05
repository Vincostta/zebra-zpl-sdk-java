package com.github.vincostta.zpl.service;

public class PrinterStatus {
    private final boolean online;
    private final String message;

    public PrinterStatus(boolean online, String message) {
        this.online = online;
        this.message = message;
    }

    public boolean isReady() { return online && message.equals("Pronta"); }
    public String getMessage() { return message; }
}