package be.betterplugins.core.commands;

import be.betterplugins.core.messaging.messenger.Messenger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BPCommand
{
    protected final Messenger messenger;

    public BPCommand(Messenger messenger)
    {
        this.messenger = messenger;
    }

    /**
     * The name of this subcommand eg. /your_plugin command_name
     *
     * @return the name of this subcommand
     */
    public abstract @NotNull String getCommandName();

    /**
     * A list of aliases that can be used instead of the command name
     * Make sure to double check for duplicates
     *
     * @return a list of aliases
     */
    public abstract @NotNull List<String> getAliases();

    /**
     * Get the required permission for this command
     *
     * @return the permission string
     */
    public abstract @NotNull String getPermission();

    /**
     * Returns whether a given sender can execute a command, depending on the sender's type
     * It differentiates between: Player, ConsoleCommandSender
     *
     * @param commandSender the sender
     * @return whether this sender is supposed to be able to execute this command
     */
    public abstract boolean mayExecute(CommandSender commandSender);
    public abstract boolean execute(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String[] arguments);
}
