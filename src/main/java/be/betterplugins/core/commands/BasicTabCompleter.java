package be.betterplugins.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BasicTabCompleter implements TabCompleter
{

    private final List<BPCommand> commands;

    /**
     * Enable tab completion for the given commands
     * Do not forget to register the instance as an event handler, otherwise tab completion events are not registered
     *
     * @param commands the BPCommands for which we want to enable automatic tab completion
     */
    public BasicTabCompleter(BPCommand... commands)
    {
        this.commands = new ArrayList<>(Arrays.asList(commands));
    }


    /**
     * This will return a sorted list, containing all allowed commands for a CommandSender
     *
     * @param cs which CommandSender needs to be checked
     * @param partialMatch enforces each command to start with the given String
     * @return a sorted list of possible commands
     */
    private List<String> getAllowedCommands( @NotNull CommandSender cs, @Nullable String partialMatch )
    {
        List<String> allowedCommands = new ArrayList<>();

        // Get the allowed commands for this CommandSender
        for(BPCommand command : commands)
        {
            String cmdName = command.getCommandName();

            if (command.mayExecute( cs ) && cs.hasPermission( command.getPermission() ))
                allowedCommands.add( cmdName );
        }

        // Only keep the matches
        List<String> matches;
        if (partialMatch != null)
        {
            matches = new ArrayList<>();
            StringUtil.copyPartialMatches( partialMatch, allowedCommands, matches );
        }
        else
        {
            matches = allowedCommands;
        }

        // Sort the result
        Collections.sort(matches);

        return matches;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] arguments)
    {
        // Return the correct list of possible commands
        if (arguments.length <= 1)
            return getAllowedCommands(commandSender, arguments.length > 0 ? arguments[0] : null);
        else
            return null;
    }
}
