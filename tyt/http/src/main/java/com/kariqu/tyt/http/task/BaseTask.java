package com.kariqu.tyt.http.task;

public class BaseTask {
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_GAME_RECORD = 1;
    public static final int TYPE_COINS_LOG = 2;

    protected int type;

    public BaseTask(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
