package be.betterplugins.core.commands;

import be.betterplugins.core.commands.messages.CommandMessage;
import be.betterplugins.core.commands.messages.CommandMessages;
import be.betterplugins.core.messaging.messenger.Messenger;
import be.betterplugins.core.messaging.messenger.MsgEntry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class BPCommandHandler implements CommandExecutor
{

    private final Map<String, BPCommand> commandMap;
    private final Messenger messenger;
    private final CommandMessages commandMessages;

    /**
     * Don't forget to manually register this as the executor of a command!
     *
     * @param messenger your messenger instance to handle messaging
     * @param commands the list of BPCommands you want to include
     */
    public BPCommandHandler(CommandMessages messages, Messenger messenger, BPCommand... commands )
    {
        this.messenger = messenger;
        this.commandMessages = messages;

        this.commandMap = new HashMap<>();

        for (BPCommand command : commands)
        {
            commandMap.put(command.getCommandName(), command);

            for (String alias : command.getAliases())
            {
                commandMap.put(alias, command);
            }
        }
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] arguments)
    {
        // Default to help command if no argument given
        String cmd = (arguments.length == 0) ? "help" : arguments[0].toLowerCase();

        if (commandMap.containsKey( cmd ))
        {
            BPCommand bpCommand = commandMap.get(cmd);

            if (!bpCommand.mayExecute( commandSender ))
            {
                this.messenger.sendMessage(commandSender, commandMessages.getMessage(CommandMessage.MAY_NOT_EXECUTE), new MsgEntry("<command>", cmd));
                return true;
            }

            // Has permission OR is the console
            if (commandSender.hasPermission( bpCommand.getPermission() ) || commandSender instanceof ConsoleCommandSender)
                return bpCommand.execute(commandSender, command, arguments);
            else
            {
                this.messenger.sendMessage(commandSender, commandMessages.getMessage(CommandMessage.NO_PERMISSION), new MsgEntry("<command>", cmd));
                return true;
            }
        }
        else
        {
            this.messenger.sendMessage(commandSender, commandMessages.getMessage(CommandMessage.UNKNOWN_COMMAND), new MsgEntry("<command>", cmd));
            return true;
        }
    }
}
