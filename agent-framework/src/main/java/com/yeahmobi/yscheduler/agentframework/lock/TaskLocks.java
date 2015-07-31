package com.yeahmobi.yscheduler.agentframework.lock;

import java.util.concurrent.locks.ReentrantLock;

public class TaskLocks {

    /**
     * 相同的taskName对应相同的锁。可以控制相同的task做某些操作时串行(比如下载attachment)。锁的个数也决定了整体并发度。
     */
    private static final int       LOCK_NUM = 100;
    private static ReentrantLock[] locks    = new ReentrantLock[LOCK_NUM];
    static {
        for (int i = 0; i < locks.length; i++) {
            locks[i] = new ReentrantLock();
        }
    }

    public static ReentrantLock getLock(String taskName) {
        return locks[index(taskName)];
    }

    private static int index(String taskName) {
        int hashcode = taskName.hashCode();
        hashcode = hashcode == Integer.MIN_VALUE ? 0 : Math.abs(hashcode);// 保证非负
        return hashcode % LOCK_NUM;
    }

}
