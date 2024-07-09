package com.github.soramame0256.showmemydps.util;

import java.time.Instant;

public class Removal<T> {
    private final Instant a = Instant.now();
    private final T b;
    private final int expire;

    public Removal(T b, int expire) {
        this.b = b;
        this.expire = expire;
    }

    public boolean checkExpire() {
        return a.toEpochMilli() + expire <= Instant.now().toEpochMilli();
    }

    public T getB() {
        return b;
    }
}
