package org.cdbtool.cdbtool.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cdbtool.cdbtool.exceptions.UtilsException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ListUtils {

    public static <V, I> I findOrThrow(List<I> items, V value, FindCallback<V, I> findCallback) throws UtilsException {
        return find(items, value, findCallback)
                .orElseThrow(() -> new UtilsException("Cannot find item"));
    }

    public static <V, I> Optional<I> find(List<I> items, V value, FindCallback<V, I> findCallback) {
        return items.stream()
                .filter(item -> Objects.equals(value, findCallback.matches(item)))
                .findFirst();
    }
}
