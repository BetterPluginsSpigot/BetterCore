package be.betterplugins.core.version;

import be.betterplugins.core.messaging.logging.BPLogger;
import be.betterplugins.core.messaging.messenger.Messenger;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;
import java.util.logging.Level;

public class UpdateChecker extends Thread
{

    private final String pluginID;
    private final Plugin plugin;
    private final String currentVersion;
    private final BPLogger logger;

    UpdateChecker(Plugin plugin, String pluginID, String currentVersion, BPLogger logger)
    {
        this.plugin = plugin;
        this.pluginID = pluginID;
        this.currentVersion = currentVersion;
        this.logger = logger;
    }


    @Override
    public void run()
    {
        URL url = null;
        try
        {
            url = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + pluginID);
        }
        catch (MalformedURLException e)
        {
            logger.log(Level.WARNING, "An error occurred while retrieving the latest version for " + plugin.getName() + "!");
        }

        URLConnection conn = null;
        try
        {
            conn = Objects.requireNonNull(url).openConnection();
        }
        catch (IOException | NullPointerException e)
        {
            logger.log(Level.WARNING, "An error occurred while retrieving the latest version for " + plugin.getName() + "!");
        }

        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(conn).getInputStream()));
            String updateVersion = reader.readLine();

            Version current = new Version(currentVersion);
            Version latest = new Version(updateVersion);

            boolean areVersionsCorrect = current.isCorrectFormat() && latest.isCorrectFormat();

            if ( (areVersionsCorrect && current.compareTo(latest) == 0 ) || (!areVersionsCorrect && updateVersion.equals(currentVersion) ))
            {
                logger.log(Level.INFO, "You are using the latest version of " + plugin.getName() + ": " + currentVersion);
            }
            else if (!areVersionsCorrect || current.compareTo(latest) < 0)
            {
                logger.log(Level.WARNING, "Update detected! You are using version " + currentVersion + " and the latest version is " + updateVersion + "! Download it at https://www.spigotmc.org/resources/" + plugin.getName() + "." + pluginID + "/");
            }
        }
        catch (IOException | NullPointerException e)
        {
            logger.log(Level.WARNING, "An error occurred while retrieving the latest version for " + plugin.getName() + "!");
        }
    }

}