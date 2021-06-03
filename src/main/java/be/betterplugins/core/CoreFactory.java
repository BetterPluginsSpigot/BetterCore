package be.betterplugins.core;

import be.betterplugins.core.commands.BPCommand;
import be.betterplugins.core.commands.BPCommandHandler;
import be.betterplugins.core.commands.messages.CommandMessage;
import be.betterplugins.core.commands.messages.CommandMessages;
import be.betterplugins.core.messaging.logging.BPLogger;
import be.betterplugins.core.messaging.messenger.Messenger;
import be.betterplugins.core.runnable.countdown.CountdownRunnable;
import be.betterplugins.core.runnable.countdown.ICountdownAction;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Easily create core objects
 */
public class CoreFactory
{

    /**
     * Create a new BPLogger instance that logs all messages with a higher priority than the given level
     *
     * @param level the minimum level
     * @return new BPLogger instance
     */
    public BPLogger createLogger(Level level)
    {
        return new BPLogger(level);
    }


    /**
     * Create a Messenger instance to send messages to players, console, etc.
     * See the MsgEntry class and Messenger#sendMessage on how to replace placeholders
     * This has built-in support for singular/plural: [<placeholder>.singular.plural] will result in singular if the placeholder is 1 and plural otherwise
     *
     * @param prefix the prefix that will be placed before every message
     * @param messages a map that maps message ID to actual message
     * @param logger a BPLogger instance so that debug logging is possible
     * @return a new Messenger instance
     */
    public Messenger createMessenger(String prefix, Map<String, String> messages, BPLogger logger)
    {
        return new Messenger(messages, logger, prefix);
    }


    /**
     * Start a countdown at a given value, which is decremented any time the run-method is called
     * Every decrement, the countAction is called with the current count as argument
     * When the counter reaches 0 (or < 0), countAction is NOT called but doneAction is called with 0 as argument. The runnable cancels itself after this.
     * Do not forget to start this runnable with CountdownRunnable#runTaskTimer(...)!
     *
     * @param startingCount the starting value for the counter
     * @param onCount action to be ran for every countdown value that is > 0. No need to implement the interface, use lambda's instead!
     * @param onDone called once the counter reaches <= 0, after which this runnable cancels itself. No need to implement the interface, use lambda's instead!
     */
    public CountdownRunnable createCountdownRunnable(int startingCount, ICountdownAction onCount, ICountdownAction onDone)
    {
        return new CountdownRunnable(startingCount, onCount, onDone);
    }


    /**
     * Create a CommandMessages instance so you can create a command handler
     *
     * @param mayNotExecuteMessage sent when a player tries to execute a console command or vice versa. Placeholder: <command>
     * @param noPermissionmessage sent when someone does not have permission to execute a command. Placeholder: <command>
     * @param unknownCommandMessage sent when a command is unknown. Placeholder: <command>
     * @return a CommandMessages instance, mapping each situation to the provided message
     */
    public CommandMessages createCommandMessages(String mayNotExecuteMessage, String noPermissionmessage, String unknownCommandMessage)
    {
        Map<CommandMessage, String> messageMap = new HashMap<CommandMessage, String>()
        {{
            put(CommandMessage.MAY_NOT_EXECUTE, mayNotExecuteMessage);
            put(CommandMessage.NO_PERMISSION, noPermissionmessage);
            put(CommandMessage.UNKNOWN_COMMAND, unknownCommandMessage);
        }};

        return new CommandMessages(messageMap);
    }


    /**
     * Create a new command handler
     * Take a look at createCommandMessages & createMessenger to help you create the right objects
     * You have to provide your own commands: extend BPCommand for completely custom commands
     * Or extend PlayerBPCommand/ConsoleBPCommand for player/console-only commands respectively
     *
     * @param commandMessages the error messages
     * @param messenger messenger instance to send the error messages when needed
     * @param commands the list of subcommands you want to register
     * @return a new BPCommandHandler instance
     */
    public BPCommandHandler createCommandHandler(CommandMessages commandMessages, Messenger messenger, BPCommand... commands)
    {
        return new BPCommandHandler(commandMessages, messenger, commands);
    }
}
