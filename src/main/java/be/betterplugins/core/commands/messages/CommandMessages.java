package be.betterplugins.core.commands.messages;

import java.util.Map;

public class CommandMessages
{

    private final Map<CommandMessage, String> messageMap;

    /**
     * Types:
     *     MAY_NOT_EXECUTE: Executor is a Player or console but this command cannot be executed by them. Placeholder: <command>
     *     NO_PERMISSION: The player does not have permission to execute this command. Placeholder: <command>
     *     UNKNOWN_COMMAND: The executed command was not found. Placeholder: <command>
     *
     * @param messageMap
     */
    public CommandMessages(Map<CommandMessage, String> messageMap)
    {
        this.messageMap = messageMap;
    }

    public String getMessage(CommandMessage message)
    {
        return messageMap.getOrDefault(message, "");
    }
}
