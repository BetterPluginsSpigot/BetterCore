package be.betterplugins.core.messaging.messenger.placeholderapi;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class PapiUtil implements IPapiUtil
{
    @Override
    public String setPlaceholders(Player player, String message)
    {
        return PlaceholderAPI.setPlaceholders( player, message );
    }
}
