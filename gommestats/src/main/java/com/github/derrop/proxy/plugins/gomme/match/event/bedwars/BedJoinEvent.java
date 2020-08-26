package com.github.derrop.proxy.plugins.gomme.match.event.bedwars;

import com.github.derrop.proxy.api.entity.PlayerInfo;
import com.github.derrop.proxy.api.location.Location;
import com.github.derrop.proxy.plugins.gomme.match.event.MatchEvent;

public class BedJoinEvent extends MatchEvent {

    private final PlayerInfo player;
    private final Location bed;

    public BedJoinEvent(PlayerInfo player, Location bed) {
        this.player = player;
        this.bed = bed;
    }

    public PlayerInfo getPlayer() {
        return this.player;
    }

    public Location getBed() {
        return this.bed;
    }

    @Override
    public String toPlainText() {
        return "Player " + this.player.getUsername() + " is near the bed at " + this.bed.toShortString();
    }
}
