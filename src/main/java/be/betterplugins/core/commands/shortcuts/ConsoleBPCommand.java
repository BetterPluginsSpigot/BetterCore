package be.betterplugins.core.commands.shortcuts;

import be.betterplugins.core.commands.BPCommand;
import be.betterplugins.core.messaging.messenger.Messenger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class ConsoleBPCommand extends BPCommand
{

    public ConsoleBPCommand(Messenger messenger)
    {
        super(messenger);
    }

    @Override
    public boolean mayExecute(CommandSender commandSender)
    {
        return commandSender instanceof ConsoleCommandSender;
    }

    public abstract boolean execute(@NotNull ConsoleCommandSender console, @NotNull Command command, @NotNull String[] arguments);

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String[] arguments)
    {
        if (commandSender instanceof ConsoleCommandSender)
            return this.execute( (ConsoleCommandSender) commandSender, command, arguments );
        else
            return false;
    }
}
