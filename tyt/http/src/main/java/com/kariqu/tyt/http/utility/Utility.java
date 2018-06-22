package com.kariqu.tyt.http.utility;

import com.google.gson.Gson;

import java.util.concurrent.atomic.AtomicLong;

public class Utility {

    public static AtomicLong atomicLong = new AtomicLong();

    public static <T> T parseJsonToObject(String str, Class<T> c) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(str, c);
        } catch (Exception e) {
            return null;
        }
    }

    public static long increment() {
        return atomicLong.incrementAndGet();
    }

}
