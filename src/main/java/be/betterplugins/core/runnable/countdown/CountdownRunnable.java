package be.betterplugins.core.runnable.countdown;

import org.bukkit.scheduler.BukkitRunnable;

public class CountdownRunnable extends BukkitRunnable
{

    private int count;

    private final ICountdownAction countAction;
    private final ICountdownAction doneAction;

    /**
     * Start a countdown at a given value, which is decremented any time the run-method is called
     * Every decrement, the countAction is called with the current count as argument
     * When the counter reaches 0 (or < 0), countAction is NOT called but doneAction is called with 0 as argument. The runnable cancels itself after this.
     * Do not forget to start this runnable with CountdownRunnable#runTaskTimer(...)!
     *
     * @param startingCount the starting value for the counter
     * @param countAction action to be ran for every countdown value that is > 0
     * @param doneAction called once the counter reaches <= 0, after which this runnable cancels itself
     */
    public CountdownRunnable(int startingCount, ICountdownAction countAction, ICountdownAction doneAction)
    {
        this.count = startingCount;

        this.countAction = countAction;
        this.doneAction = doneAction;
    }

    @Override
    public void run()
    {
        if (count > 0)
        {
            this.countAction.execute( count );
        }
        else
        {
            this.doneAction.execute( count );
            this.cancel();
        }

        count--;
    }
}