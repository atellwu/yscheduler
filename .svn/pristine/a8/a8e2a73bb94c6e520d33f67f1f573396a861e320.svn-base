package com.yeahmobi.yscheduler.agentframework.agent.event.task;

import java.util.concurrent.ConcurrentHashMap;

import com.yeahmobi.yscheduler.agentframework.agent.event.task.CalloutTaskExecutor.CalloutFutureTask;
import com.yeahmobi.yscheduler.agentframework.agent.task.TaskTransaction;

public class CalloutTaskHolder {

    // txId,Pair
    private static ConcurrentHashMap<Long, Pair> holder = new ConcurrentHashMap<Long, Pair>();

    public static class Pair {

        public Pair(TaskTransaction tx, CalloutFutureTask task) {
            super();
            this.tx = tx;
            this.task = task;
        }

        TaskTransaction   tx;
        CalloutFutureTask task;

        public TaskTransaction getTx() {
            return this.tx;
        }

        public void setTx(TaskTransaction tx) {
            this.tx = tx;
        }

        public CalloutFutureTask getTask() {
            return this.task;
        }

        public void setTask(CalloutFutureTask task) {
            this.task = task;
        }

    }

    // TODO 需要remove
    public static void put(long txId, Pair value) {
        holder.put(txId, value);
    }

    public static Pair get(long txId) {
        return holder.get(txId);
    }

}
