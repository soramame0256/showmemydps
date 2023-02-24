package com.github.soramame0256.showmemydps.util;

import java.time.Instant;

public class Removal<T> {
    public Instant a = Instant.now();
    public T b;
    private final int expire;

    public Removal(T b, int expire) {
        this.b = b;
        this.expire = expire;
    }

    public boolean checkExpire() {
        return a.toEpochMilli() + expire <= Instant.now().toEpochMilli();
    }
}
