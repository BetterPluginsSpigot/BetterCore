package be.betterplugins.core.commands.shortcuts;

import be.betterplugins.core.commands.BPCommand;
import be.betterplugins.core.messaging.messenger.Messenger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class PlayerBPCommand extends BPCommand
{

    public PlayerBPCommand(Messenger messenger)
    {
        super(messenger);
    }

    @Override
    public boolean mayExecute(CommandSender commandSender)
    {
        return commandSender instanceof Player;
    }

    public abstract boolean execute(@NotNull Player player, @NotNull Command command, @NotNull String[] arguments);

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String[] arguments)
    {
        if (commandSender instanceof Player)
            return this.execute( (Player) commandSender, command, arguments );
        else
            return false;
    }
}
