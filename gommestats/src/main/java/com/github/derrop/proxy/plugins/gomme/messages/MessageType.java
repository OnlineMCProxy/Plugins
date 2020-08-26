package com.github.derrop.proxy.plugins.gomme.messages;

import com.github.derrop.proxy.plugins.gomme.GommeServerType;

public enum MessageType {

    // Language detection
    YOUR_RANK_INFO(GommeServerType.LOBBY),
    LANGUAGE_SELECTED(),

    // Every game mode
    GAME_BEGIN(),
    MAP_SELECTED(),
    GAME_END_FINISHED(),
    GAME_END_DIED(),
    PLAYER_JOIN_LOBBY(),
    PLAYER_LEAVE_LOBBY(),
    PLAYER_LEAVE_IN_GAME_MULTI(),
    PLAYER_LEAVE_IN_GAME_SINGLE(),
    PLAYER_DIED(),
    PLAYER_KILLED(),
    PLAYER_GLOBAL_CHAT_MESSAGE(),
    PLAYER_TEAM_CHAT_MESSAGE(),

    // Teams
    TEAM_BLACK(),
    TEAM_BLUE(),
    TEAM_TURQUOISE(),
    TEAM_DIAMOND(),
    TEAM_DARK_BLUE(),
    TEAM_DARK_GRAY(),
    TEAM_DARK_GREEN(),
    TEAM_DARK_RED(),
    TEAM_GRAY(),
    TEAM_GREEN(),
    TEAM_ORANGE(),
    TEAM_PINK(),
    TEAM_PURPLE(),
    TEAM_RED(),
    TEAM_WHITE(),
    TEAM_YELLOW(),


    // Cores
    CORE_DESTROYED(GommeServerType.CORES),
    CORE_NEARBY_PLAYERS(GommeServerType.CORES),


    // BedWars
    BED_DESTROYED(GommeServerType.BED_WARS),
    CANNOT_DESTROY_OWN_BED(GommeServerType.BED_WARS),
    TEAM_OUT(GommeServerType.BED_WARS),

    // Clans
    CLAN_INFO_BEGIN(),
    CLAN_INFO_NAME(),
    CLAN_INFO_TAG(),
    CLAN_INFO_GENERAL_MEMBER_COUNT(),
    CLAN_INFO_LEADER_COUNT(),
    CLAN_INFO_MODERATOR_COUNT(),
    CLAN_INFO_MEMBER_COUNT(),
    CLAN_INFO_USER(),
    CLAN_INFO_MORE();

    private final GommeServerType[] availableGameModes;

    MessageType() {
        this(GommeServerType.values());
    }

    MessageType(GommeServerType... availableGameModes) {
        this.availableGameModes = availableGameModes;
    }

    public GommeServerType[] getAvailableGameModes() {
        return this.availableGameModes;
    }
}
