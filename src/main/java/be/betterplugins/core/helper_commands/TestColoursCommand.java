package be.betterplugins.core.helper_commands;

import be.betterplugins.core.commands.BPCommand;
import be.betterplugins.core.messaging.messenger.Messenger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestColoursCommand extends BPCommand
{
    public TestColoursCommand(Messenger messenger)
    {
        super(messenger);
    }

    @Override
    public @NotNull String getCommandName()
    {
        return "testcolour";
    }

    @Override
    public @NotNull List<String> getAliases()
    {
        return Collections.emptyList();
    }

    @Override
    public @NotNull String getPermission()
    {
        return "";
    }

    @Override
    public boolean mayExecute(CommandSender commandSender)
    {
        return true;
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String[] arguments)
    {
        final List<ChatColor> colours = new ArrayList<>();
        if (arguments.length > 1)
        {
            for (String arg : arguments) {
                if (arg.equalsIgnoreCase("testcolour")) {
                    continue;
                }

                try {
                    ChatColor color = ChatColor.valueOf(arg.toUpperCase());
                    colours.add(color);
                } catch (NullPointerException | IllegalArgumentException ignored) {
                    messenger.sendMessage(commandSender, "&4The color: '&c" + arg + "&4' does not exist!");
                }
            }
        }
        else
        {
            colours.addAll(Arrays.asList(ChatColor.values()));
        }

        StringBuilder firstMessage = new StringBuilder();
        for (ChatColor colour : colours)
            firstMessage.append(colour).append("█");
        messenger.sendMessage(commandSender, "You selected the colours:");
        messenger.sendMessage(commandSender, firstMessage.toString());

        for (int i = 0; i < colours.size(); i++)
        for (int j = 0; j < colours.size(); j++)
        {
            ChatColor primary = colours.get(i);
            ChatColor secondary = colours.get(j);
            messenger.sendMessage( commandSender, primary + "█" + secondary + "█" + ChatColor.WHITE + " (" + primary.name() + ", " + secondary.name() +  ")" );
        }

        return true;
    }
}
