package com.github.phantompowered.plugins.gomme.messages.defaults.game.bedwars;

import com.github.phantompowered.plugins.gomme.match.event.global.match.MatchBeginEvent;
import com.github.phantompowered.plugins.gomme.messages.Language;
import com.github.phantompowered.plugins.gomme.messages.MessageType;
import com.github.phantompowered.plugins.gomme.messages.defaults.game.SingleGameMessageRegistrar;
import com.github.phantompowered.plugins.gomme.messages.defaults.game.SpecificGameMessageRegistrar;

public class BedWarsMessageGameBegin extends SingleGameMessageRegistrar {
    @Override
    public void register(SpecificGameMessageRegistrar registrar) {
        registrar.registerMessage(Language.GERMANY, MessageType.GAME_BEGIN, "[BedWars] Das Spiel beginnt!", MatchBeginEvent::new);
        registrar.registerMessage(Language.AUSTRIA, MessageType.GAME_BEGIN, "[BedWars] Des Spü startet!", MatchBeginEvent::new);
    }
}
