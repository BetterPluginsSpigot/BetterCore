package be.betterplugins.core.messaging.messenger.placeholderapi;

import org.bukkit.entity.Player;

public class NoPapiUtil implements IPapiUtil
{
    @Override
    public String setPlaceholders(Player player, String message)
    {
        return message;
    }
}
