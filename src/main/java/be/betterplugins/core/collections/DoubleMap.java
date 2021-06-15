package be.betterplugins.core.collections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DoubleMap<K1, K2, V>
{
    private final Map<K1, V> forwardMap;
    private final Map<K2, V> backwardMap;

    private final Map<K1, K2> fKeyMap;
    private final Map<K2, K1> bKeyMap;

    public DoubleMap()
    {
        this.forwardMap = new HashMap<>();
        this.backwardMap = new HashMap<>();

        this.fKeyMap = new HashMap<>();
        this.bKeyMap = new HashMap<>();
    }

    public void put(K1 fKey, K2 bKey, V value)
    {
        this.forwardMap.put(fKey, value);
        this.backwardMap.put(bKey, value);

        this.fKeyMap.put(fKey, bKey);
        this.bKeyMap.put(bKey, fKey);
    }

    public void clear()
    {
        this.forwardMap.clear();
        this.backwardMap.clear();

        this.fKeyMap.clear();
        this.bKeyMap.clear();
    }

    public @NotNull Set<K1> keySetForward()
    {
        return this.forwardMap.keySet();
    }

    public @NotNull Set<K2> keySetBackward()
    {
        return this.backwardMap.keySet();
    }

    public @NotNull Collection<V> values()
    {
        return this.forwardMap.values();
    }

    public @Nullable V removeForward(K1 fKey)
    {
        K2 bKey = this.fKeyMap.remove(fKey);
        this.bKeyMap.remove(bKey);
        this.backwardMap.remove(bKey);

        return this.forwardMap.remove(fKey);
    }

    public @Nullable V removeBackward(K2 bKey)
    {
        K1 fKey = this.bKeyMap.remove(bKey);
        this.fKeyMap.remove(fKey);
        this.forwardMap.remove(fKey);

        return this.backwardMap.remove(bKey);
    }

    public boolean containsForward(K1 key)
    {
        return forwardMap.containsKey( key );
    }

    public boolean containsBackward(K2 key)
    {
        return backwardMap.containsKey( key );
    }

    public @Nullable V getForward(K1 key)
    {
        return getForward(key, null);
    }

    /**
     * Get a value and perform a specific action with this value if a value is found for this forward key
     *
     * @param key the key for which a value is sought
     * @param onFound the action to perform with the resulting value. Nothing happens if no value was found
     * @return the value that is found for this key
     */
    public @Nullable V getForward(K1 key, @Nullable IGetFoundAction<V> onFound)
    {
        V value = forwardMap.get(key);
        if (onFound != null && value != null)
            onFound.handleResult(value);
        return value;
    }

    public @Nullable V getBackward(K2 key)
    {
        return getBackward(key, null);
    }

    /**
     * Get a value and perform a specific action with this value if a value is found for this backward key
     *
     * @param key the key for which a value is sought
     * @param onFound the action to perform with the resulting value. Nothing happens if no value was found
     * @return the value that is found for this key
     */
    public @Nullable V getBackward(K2 key, @Nullable IGetFoundAction<V> onFound)
    {
        V value = backwardMap.get(key);
        if (onFound != null && value != null)
            onFound.handleResult(value);
        return value;
    }
}
