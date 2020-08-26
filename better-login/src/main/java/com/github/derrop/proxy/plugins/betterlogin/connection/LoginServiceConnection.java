package com.github.derrop.proxy.plugins.betterlogin.connection;

import com.github.derrop.proxy.api.block.BlockAccess;
import com.github.derrop.proxy.api.block.Material;
import com.github.derrop.proxy.api.chat.ChatMessageType;
import com.github.derrop.proxy.api.connection.*;
import com.github.derrop.proxy.api.entity.types.Entity;
import com.github.derrop.proxy.api.location.Location;
import com.github.derrop.proxy.api.network.NetworkAddress;
import com.github.derrop.proxy.api.network.Packet;
import com.github.derrop.proxy.api.player.Player;
import com.github.derrop.proxy.api.player.PlayerAbilities;
import com.github.derrop.proxy.api.player.id.PlayerId;
import com.github.derrop.proxy.api.player.inventory.InventoryType;
import com.github.derrop.proxy.api.raytrace.BlockingObject;
import com.github.derrop.proxy.api.scoreboard.Scoreboard;
import com.github.derrop.proxy.api.service.ServiceRegistry;
import com.github.derrop.proxy.api.session.MCServiceCredentials;
import com.github.derrop.proxy.api.task.Task;
import com.github.derrop.proxy.api.task.TaskFutureListener;
import com.github.derrop.proxy.api.task.util.TaskUtil;
import com.github.derrop.proxy.connection.player.DefaultPlayer;
import com.github.derrop.proxy.plugins.betterlogin.LoginPrepareListener;
import com.github.derrop.proxy.protocol.play.server.PacketPlayServerLogin;
import com.github.derrop.proxy.protocol.play.server.PacketPlayServerPlayerInfo;
import com.github.derrop.proxy.protocol.play.server.player.PacketPlayServerPlayerAbilities;
import com.github.derrop.proxy.protocol.play.server.player.spawn.PacketPlayServerPosition;
import com.github.derrop.proxy.protocol.play.server.world.PacketPlayServerTimeUpdate;
import com.github.derrop.proxy.protocol.play.server.world.material.PacketPlayServerEmptyMapChunk;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.UserAuthentication;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.function.Consumer;

public class LoginServiceConnection implements ServiceConnection, Entity.Callable {

    private static final ServiceWorldDataProvider WORLD_DATA_PROVIDER = new LoginServiceWorldDataProvider();

    private final Player player;

    private boolean connected = false;
    private LoginBlockAccess blockAccess;

    private final PlayerAbilities abilities = new LoginPlayerAbilities();
    private final long connectionTimestamp = System.currentTimeMillis();

    public LoginServiceConnection(Player player) {
        this.player = player;
    }

    public long getConnectionTimestamp() {
        return this.connectionTimestamp;
    }

    @Override
    public @NotNull ServiceRegistry getServiceRegistry() {
        return this.player.getServiceRegistry();
    }

    @Override
    public @Nullable Player getPlayer() {
        return this.connected ? this.player : null;
    }

    @Override
    public long getLastDisconnectionTimestamp() {
        return -1;
    }

    @Override
    public PlayerId getLastConnectedPlayer() {
        return null;
    }

    @Override
    public PlayerAbilities getAbilities() {
        return this.abilities;
    }

    @Override
    public ServiceInventory getInventory() {
        return null;
    }

    @Override
    public @NotNull MCServiceCredentials getCredentials() {
        return MCServiceCredentials.offline(this.player.getName(), null, false);
    }

    @Override
    public @Nullable UserAuthentication getAuthentication() {
        return null;
    }

    @Override
    public @Nullable UUID getUniqueId() {
        return this.player.getUniqueId();
    }

    @Override
    public @Nullable String getName() {
        return this.player.getName();
    }

    @Override
    public int getEntityId() {
        return -1;
    }

    @Override
    public int getDimension() {
        return this.player.getDimension();
    }

    @Override
    public @NotNull Location getLocation() {
        return LoginPrepareListener.SPAWN.clone();
    }

    @Override
    public InteractiveServiceConnection interactive() {
        return null;
    }

    @Override
    public boolean isOnGround() {
        return true;
    }

    @Override
    public ServiceWorldDataProvider getWorldDataProvider() {
        return WORLD_DATA_PROVIDER;
    }

    @Override
    public @NotNull NetworkAddress getServerAddress() {
        return Objects.requireNonNull(NetworkAddress.parse("127.0.0.1"));
    }

    @Override
    public void chat(@NotNull String message) {
    }

    @Override
    public void displayMessage(@NotNull ChatMessageType type, @NotNull String message) {
        this.player.sendMessage(type, TextComponent.of(message));
    }

    @Override
    public void chat(@NotNull Component component) {
    }

    @Override
    public void displayMessage(@NotNull ChatMessageType type, @NotNull Component component) {
        this.player.sendMessage(type, component);
    }

    @Override
    public void chat(@NotNull Component... components) {
    }

    @Override
    public void displayMessage(@NotNull ChatMessageType type, @NotNull Component... components) {
        this.player.sendMessage(type, components);
    }

    @Override
    public boolean setTabHeaderAndFooter(Component header, Component footer) {
        return true;
    }

    @Override
    public void resetTabHeaderAndFooter() {

    }

    @Override
    public Component getTabHeader() {
        return null;
    }

    @Override
    public Component getTabFooter() {
        return null;
    }

    @Override
    public @NotNull Task<Boolean> connect() {
        return TaskUtil.completedTask(false);
    }

    @Override
    public @NotNull Task<Boolean> connect(@NotNull TaskFutureListener<Boolean> listener) {
        return TaskUtil.completedTask(false);
    }

    @Override
    public @NotNull Task<Boolean> connect(@NotNull Collection<TaskFutureListener<Boolean>> listener) {
        return TaskUtil.completedTask(false);
    }

    @Override
    public @NotNull Task<Boolean> reconnect() {
        return TaskUtil.completedTask(false);
    }

    @Override
    public @NotNull Task<Boolean> reconnect(@NotNull TaskFutureListener<Boolean> listener) {
        return TaskUtil.completedTask(false);
    }

    @Override
    public @NotNull Task<Boolean> reconnect(@NotNull Collection<TaskFutureListener<Boolean>> listener) {
        return TaskUtil.completedTask(false);
    }

    @Override
    public void setReScheduleOnFailure(boolean reScheduleOnFailure) {
    }

    @Override
    public boolean isReScheduleOnFailure() {
        return false;
    }

    @Override
    public @NotNull InetSocketAddress getSocketAddress() {
        return this.player.getSocketAddress();
    }

    @Override
    public void disconnect(@NotNull Component reason) {
        this.player.disconnect(reason);
    }

    @Override
    public void write(@NotNull Object packet) {
    }

    @Override
    public @NotNull Task<Boolean> writeWithResult(@NotNull Object packet) {
        return TaskUtil.completedTask(false);
    }

    @Override
    public void setProtocolState(@NotNull ProtocolState state) {
    }

    @Override
    public @NotNull ProtocolState getProtocolState() {
        return ProtocolState.PLAY;
    }

    @Override
    public @NotNull InetSocketAddress getAddress() {
        return this.player.getAddress();
    }

    @Override
    public void close(@Nullable Object goodbyeMessage) {
    }

    @Override
    public void delayedClose(@Nullable Packet goodbyeMessage) {
    }

    @Override
    public void setCompression(int compression) {
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public boolean isClosing() {
        return false;
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public Channel getWrappedChannel() {
        return null;
    }

    @Override
    public <T> @Nullable T getProperty(String key) {
        return null;
    }

    @Override
    public <T> void setProperty(String key, T value) {
    }

    @Override
    public void removeProperty(String key) {
    }

    @Override
    public void addOutgoingPacketListener(UUID key, Consumer<Packet> consumer) {
    }

    @Override
    public void removeOutgoingPacketListener(UUID key) {
    }

    @Override
    public void handleDisconnected(@NotNull ServiceConnection connection, @NotNull Component reason) {
    }

    @Override
    public void unregister() {
    }

    @Override
    public Scoreboard getScoreboard() {
        return null;
    }

    @Override
    public BlockAccess getBlockAccess() {
        return this.blockAccess;
    }

    @Override
    public void syncPackets(Player player, boolean switched) {
        if (!this.player.equals(player)) {
            throw new IllegalArgumentException("Cannot set LoginServiceConnection to other player than on initialization");
        }

        if (switched) {
            this.connected = true;
            return;
        }

        player.sendPacket(new PacketPlayServerLogin(1265, (short) 1, 0, (short) 0, (short) 255, "default", false));
        player.sendPacket(new PacketPlayServerPlayerAbilities(true, true, true, true, 0.05F, 0.1F));
        player.sendPacket(new PacketPlayServerTimeUpdate(1, 23700));
        player.sendPacket(new PacketPlayServerEmptyMapChunk(0, 0));
        player.sendPacket(new PacketPlayServerPlayerInfo(PacketPlayServerPlayerInfo.Action.ADD_PLAYER, new PacketPlayServerPlayerInfo.Item[]{
                new PacketPlayServerPlayerInfo.Item(new GameProfile(player.getUniqueId(), player.getName()), 1, 0, player.getName())
        }));

        this.blockAccess = new LoginBlockAccess((DefaultPlayer) player);

        player.sendPacket(new PacketPlayServerPosition(LoginPrepareListener.SPAWN.clone().add(new Location(0, 3, 0, 0, 0))));

        player.getInventory().setWindowId((byte) 1);
        player.getInventory().setContent(LoginPrepareListener.PARENT_INVENTORY);
        player.getInventory().setType(InventoryType.CHEST);
        player.getInventory().open();

        // TODO send keep alive packets

        this.connected = true;
    }

    @Override
    public void updateLocation(@NotNull Location location) {
    }

    @Override
    public Collection<Player> getViewers() {
        return Collections.emptyList();
    }

    @Override
    public void startViewing(Player player) {
    }

    @Override
    public void stopViewing(Player player) {
    }

    @Override
    public boolean isSneaking() {
        return false;
    }

    @Override
    public boolean isSprinting() {
        return false;
    }

    @Override
    public Location getTargetBlock(Set<Material> transparent, int range) {
        return null;
    }

    @Override
    public Location getTargetBlock(int range) {
        return null;
    }

    @Override
    public @NotNull BlockingObject getTargetObject(int range) {
        return BlockingObject.MISS;
    }

    @Override
    public void sendPacket(@NotNull Packet packet) {
    }

    @Override
    public void sendPacket(@NotNull ByteBuf byteBuf) {
    }

    @Override
    public @NotNull NetworkUnsafe networkUnsafe() {
        return packet -> {
        };
    }

    @Override
    public void handleEntityPacket(@NotNull Packet packet) {
    }
}
