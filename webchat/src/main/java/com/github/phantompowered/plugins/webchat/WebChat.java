package com.github.phantompowered.plugins.webchat;

import com.github.derrop.documents.Document;
import com.github.derrop.documents.Documents;
import com.github.phantompowered.plugins.webchat.handler.WebChatWsHandler;
import com.github.phantompowered.plugins.webchat.listener.ChatReceiveListener;
import com.github.phantompowered.proxy.api.connection.ServiceConnection;
import com.github.phantompowered.proxy.api.connection.ServiceConnector;
import com.github.phantompowered.proxy.api.event.EventManager;
import com.github.phantompowered.proxy.api.plugin.PluginContainer;
import com.github.phantompowered.proxy.api.plugin.PluginState;
import com.github.phantompowered.proxy.api.plugin.annotation.Inject;
import com.github.phantompowered.proxy.api.plugin.annotation.Plugin;
import com.github.phantompowered.proxy.api.service.ServiceRegistry;
import io.javalin.Javalin;
import io.javalin.websocket.WsContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Plugin(
        id = "com.github.phantompowered.plugins.webchat",
        displayName = "WebChat",
        version = 1,
        website = "https://github.com/PhantomPowered/Plugins",
        authors = "derrop"
)
public class WebChat {

    public static final String CONNECTION_SESSIONS_PROPERTY = "WebChat-Session";
    public static final String CHAT_HISTORY_PROPERTY = "WebChat-ChatHistory"; // TODO replace with the history in the proxy
    public static final String SESSION_ATTRIBUTE = "WebChat-TargetConnection";

    // TODO authentication

    private int maxHistorySize = 250;

    @Inject(state = PluginState.ENABLED)
    public void enable(PluginContainer container, ServiceRegistry registry) {
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());

        this.readConfig(registry, Paths.get("plugins/WebChat/config.yml"));

        Thread.currentThread().setContextClassLoader(old);

        registry.getProviderUnchecked(EventManager.class).registerListener(container, new ChatReceiveListener(this));

        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            for (ServiceConnection connection : registry.getProviderUnchecked(ServiceConnector.class).getOnlineClients()) {
                Map<String, WsContext> sessions = connection.getProperty(WebChat.CONNECTION_SESSIONS_PROPERTY);
                if (sessions != null) {
                    String json = Documents.newDocument().append("ping", true).toString();

                    for (WsContext ctx : sessions.values()) {
                        ctx.send(json);
                    }
                }
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    private void readConfig(ServiceRegistry registry, Path path) {
        if (!Files.exists(path)) {
            Documents.newDocument()
                    .append("host", "127.0.0.1")
                    .append("port", 80)
                    .append("allowSending", true)
                    .append("maxHistorySize", this.maxHistorySize)
                    .yaml().write(path);
        }
        Document config = Documents.yamlStorage().read(path);
        this.initWeb(registry, config.getString("host"), config.getInt("port"), config.getBoolean("allowSending"));

        this.maxHistorySize = Math.max(0, config.getInt("maxHistorySize", this.maxHistorySize));
    }

    private void initWeb(ServiceRegistry registry, String host, int port, boolean allowSending) {
        Javalin javalin = Javalin.create(e -> e.showJavalinBanner = false).start(host, port);
        javalin.config.addStaticFiles("/web");

        javalin.ws("/ws", wsHandler -> {
            // TODO timeout when no init message is received 10 seconds after connection

            wsHandler.onMessage(new WebChatWsHandler(this, registry, allowSending));

            wsHandler.onClose(ctx -> {
                ServiceConnection connection = ctx.attribute(SESSION_ATTRIBUTE);
                if (connection == null) {
                    return;
                }

                Map<String, WsContext> sessions = connection.getProperty(CONNECTION_SESSIONS_PROPERTY);
                if (sessions != null) {
                    sessions.remove(ctx.getSessionId());
                }
            });
        });
    }

    public void sendHistory(Iterable<WsContext> contexts, ServiceConnection connection) {
        for (HistoricalMessage message : this.getHistory(connection)) {
            this.sendMessage(contexts, message);
        }
    }

    public void sendMessage(Iterable<WsContext> contexts, HistoricalMessage message) {
        String html = message.asHtml();
        String json = Documents.newDocument().append("appendChat", html).toString();

        for (WsContext context : contexts) {
            ChatMode mode = context.attribute("mode");
            if (mode != ChatMode.ALL && mode != message.getMode()) {
                continue;
            }

            context.send(json);
        }
    }

    public List<HistoricalMessage> getHistory(ServiceConnection connection) {
        List<HistoricalMessage> history = connection.getProperty(CHAT_HISTORY_PROPERTY);
        if (history == null) {
            history = new ArrayList<>();
            connection.setProperty(CHAT_HISTORY_PROPERTY, history);
        }

        return history;
    }

    public int getMaxHistorySize() {
        return this.maxHistorySize;
    }
}
