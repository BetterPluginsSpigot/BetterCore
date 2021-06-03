package be.betterplugins.core.runnable.countdown;

/**
 * Used in CountdownRunnable, for every countdown iteration, the execute method is called
 */
public interface ICountdownAction
{

    /**
     * Called for every countdown (depending on how often you run the runnable)
     *
     * @param count the current count value
     */
    void execute(int count);

}