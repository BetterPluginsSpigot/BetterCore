package be.betterplugins.core.collections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Counts the amount of a specific type that is put in this map.
 * Eg. for String: putting: "a", "b", "c", "a" and another "a":
 * The map would look like: {"a": 3, "b": 1, "c": 1}.
 *
 * @param <K> the type of which you want to count instances.
 */
public class CounterMap <K>
{

    private final Map<K, Integer> counterMap;

    /**
     * Counts the amount of a specific type that is put in this map.
     * Eg. for String: putting: "a", "b", "c", "a" and another "a":
     * The map would look like: {"a": 3, "b": 1, "c": 1}.
     */
    public CounterMap()
    {
        this.counterMap = new HashMap<>();
    }

    /**
     * Put the provided item in the map
     * Handles the counter internally
     *
     * @param key the instance you want to be considered when counting
     */
    public void put(K key)
    {
        if (this.counterMap.containsKey( key ))
        {
            int num = this.counterMap.get( key );
            assert num >= 1;
            this.counterMap.put( key, num );
        }
        else
        {
            this.counterMap.put( key, 1 );
        }
    }

    /**
     * Get the amount of a certain key that is in this map.
     *
     * @param key the instance for which you want to check the count.
     * @return the amount of this item, or 0 if it is not found
     */
    public int get(K key)
    {
        return this.counterMap.getOrDefault(key, 0);
    }

    public boolean containsKey(K key)
    {
        return this.counterMap.containsKey( key );
    }

    public Set<Map.Entry<K, Integer>> entrySet()
    {
        return this.counterMap.entrySet();
    }

    public Set<K> keySet()
    {
        return this.counterMap.keySet();
    }
}
