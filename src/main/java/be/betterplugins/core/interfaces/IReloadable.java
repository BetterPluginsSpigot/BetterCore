package be.betterplugins.core.interfaces;

/**
 * An interface to be implemented by classes whose instances should be reloadable (such as the main plugin class, config classes, ...à
 */
public interface IReloadable
{

    /**
     * Reload this object
     */
    void reload();

}
