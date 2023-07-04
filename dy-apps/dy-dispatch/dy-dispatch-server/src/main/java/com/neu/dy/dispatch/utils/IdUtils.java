package com.neu.dy.dispatch.utils;


import com.neu.dy.base.common.IdWorker;

public class IdUtils {
    private static final IdWorker ID_WORKER = new IdWorker();

    public static String get() {
        return String.valueOf(ID_WORKER.nextId());
    }
}
