package org.cdbtool.cdbtool.utils;

@FunctionalInterface
public interface FindCallback<V, I> {

    V matches(I value);
}
