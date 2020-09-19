package com.github.phantompowered.plugins.webchat;

import net.kyori.adventure.text.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoricalMessage {

    private static final DateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

    private final long time;
    private final Component message;

    private HistoricalMessage(long time, Component message) {
        this.time = time;
        this.message = message;
    }

    public static HistoricalMessage now(Component message) {
        return of(System.currentTimeMillis(), message);
    }

    public static HistoricalMessage of(long time, Component message) {
        return new HistoricalMessage(time, message);
    }

    public long getTime() {
        return this.time;
    }

    public Component getMessage() {
        return this.message;
    }

    public String asHtml() {
        return FORMAT.format(new Date(this.time)) + " " + HtmlComponentSerializer.html().serialize(this.message);
    }

}
