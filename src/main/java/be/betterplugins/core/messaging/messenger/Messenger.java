package be.betterplugins.core.messaging.messenger;

import be.betterplugins.core.messaging.logging.BPLogger;
import be.betterplugins.core.messaging.messenger.placeholderapi.IPapiUtil;
import be.betterplugins.core.messaging.messenger.placeholderapi.NoPapiUtil;
import be.betterplugins.core.messaging.messenger.placeholderapi.PapiUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class Messenger
{

    private final Map<String, String> messages;     // Contains the messages in lang.yml by mapping path to value
    private final String prefix;
    private final BPLogger logger;

    private final IPapiUtil papiUtil;

    /**
     * Creates a messenger for player output.
     *
     * @param messages the messages from lang.yml, mapping path to message.
     * @param prefix the prefix for all messages.
     */
    public Messenger(Map<String, String> messages, BPLogger logger, String prefix)
    {
        this.messages = messages;
        this.logger = logger;
        this.prefix = prefix;

        boolean hasPapi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
        this.papiUtil = hasPapi ? new PapiUtil() : new NoPapiUtil();
    }


    /**
     * Compose a ready-to-be-sent BetterPlugin message.
     *
     * @param messageID the ID of the message, or a custom message.
     * @param replacements the tag replacements for this message.
     * @return the message ready to be sent.
     */
    public String composeMessage(String messageID, MsgEntry... replacements)
    {
        return this.composeMessage(messageID, true, replacements);
    }

    /**
     * Compose a ready-to-be-sent BetterPlugin message.
     *
     * @param messageID the ID of the message, or a custom message.
     * @param includePrefix whether or not a prefix should be put in front of this message.
     * @param replacements the tag replacements for this message.
     * @return the message ready to be sent.
     */
    public String composeMessage(String messageID, boolean includePrefix, MsgEntry... replacements)
    {
        // Get the message from lang.yml OR if non existent, get the raw message
        String message = messages.getOrDefault(messageID, messageID);

        // Early return if the message is disabled
        if (message.equals("") || message.equalsIgnoreCase("ignored"))
            return "";

        if (message.equals( messageID ))
        {
            logger.log(Level.CONFIG, "Missing language option found: " + messageID + ". Consider adding it to the language file");
        }

        // Perform variable replacements
        for (MsgEntry entry : replacements)
        {
            message = message.replace(entry.getTag(), entry.getReplacement());
        }

        // Singular/plural support
        String[] replaceThis = StringUtils.substringsBetween(message, "[", "]");
        if (replaceThis != null)
        {
            String[] replaceBy = new String[replaceThis.length];
            for (int i = 0; i < replaceThis.length; i++)
            {
                String[] options = replaceThis[i].split("\\.");
                if (options.length >= 3)
                {
                    try
                    {
                        double amount = Double.parseDouble(options[0]);
                        replaceBy[i] = amount == 1 ? options[1] : options[2];
                    }
                    catch(NumberFormatException exception)
                    {
                        replaceBy[i] = options[1];
                    }
                }
                else if (options.length >= 1)
                {
                    replaceBy[i] = options[options.length - 1];
                }
            }

            message = StringUtils.replaceEach(message, replaceThis, replaceBy);
            message = message.replaceAll("\\[", "").replaceAll("]", "");
        }

        // Get the prefix and put it before the message
        if (includePrefix)
        {
            message = this.prefix + message;
        }

        // Perform final replacements for color
        message = message.replace('&', '§');

        // Perform final replacement to allow square brackets []
        message = message.replaceAll("\\|\\(", "[");
        message = message.replaceAll("\\)\\|", "]");

        return message;
    }


    /**
     * Send a message from lang.yml to a CommandSender.
     * If the message does not exist, it will be sent to the player in its raw form.
     * As optional parameter, a list or several MsgEntries can be given as parameter.
     * Will automatically replace PAPI placeholders relative to the provided <player>-tag or each receiver if no <player>-tag is provided.
     * To not replace placeholders, provide a MsgEntry that replaces <player> by null.
     *
     * @param receiver the receiver.
     * @param messageID the id of the message.
     * @param replacements The strings that are to be replaced to allow using variables in messages.
     * @return False if this message is disabled (set to "" or "ignored"), true otherwise.
     */
    public boolean sendMessage(CommandSender receiver, String messageID, MsgEntry... replacements)
    {
        return sendMessage(Collections.singletonList(receiver), messageID, replacements);
    }



    /**
     * Send a message from lang.yml to a list of players.
     * If the message does not exist, it will be sent to the player in its raw form.
     * As optional parameter, a list or several MsgEntries can be given as parameter.
     * Will automatically replace PAPI placeholders relative to the provided <player>-tag or each receiver if no <player>-tag is provided.
     * To not replace placeholders, provide a MsgEntry that replaces <player> by null.
     *
     * @param receivers the list of players.
     * @param messageID the id of the message.
     * @param replacements The strings that are to be replaced to allow using variables in messages.
     * @return False if this message is disabled (set to "" or "ignored"), true otherwise.
     */
    public boolean sendMessage(List<? extends CommandSender> receivers, String messageID, MsgEntry... replacements)
    {
        // Compose the message and return if message is disabled
        String message = composeMessage(messageID, replacements);
        if (message.equals(""))
            return false;

        // Get the player if there is a player who did an action
        Player placeholderPlayer = null;
        boolean hasPlayerTag = false;
        for (MsgEntry entry : replacements)
        {
            if (entry.getTag().equals("<player>"))
            {
                hasPlayerTag = true;
                placeholderPlayer = Bukkit.getPlayer(entry.getReplacement());
            }
        }

        // Replace PAPI placeholders with respect to the player that did an action
        if (placeholderPlayer != null)
        {
            message = papiUtil.setPlaceholders( placeholderPlayer, message );
        }

        // Send everyone a message
        for (CommandSender receiver : receivers)
        {
            // Get the senders name and replace <user> tags
            String name = receiver.getName();
            String finalMessage = message.replace("<user>", ChatColor.stripColor( name ));

            // Use PAPI to replace messages. ONLY When no <player> tag was provided
            if (!hasPlayerTag && receiver instanceof Player)
            {
                Player player = (Player) receiver;
                finalMessage = papiUtil.setPlaceholders( player, finalMessage );
            }

            // Send the message
            sendMessage(receiver, finalMessage);
        }

        return true;
    }

    /**
     * Used so the method of sending can be overridden
     */
    protected void sendMessage(CommandSender receiver, String message)
    {
        receiver.sendMessage( message );
    }
}