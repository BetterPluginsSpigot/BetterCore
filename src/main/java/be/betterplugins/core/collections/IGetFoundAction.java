package be.betterplugins.core.collections;

import org.jetbrains.annotations.NotNull;

public interface IGetFoundAction<V>
{

    void handleResult(@NotNull V result);

}
