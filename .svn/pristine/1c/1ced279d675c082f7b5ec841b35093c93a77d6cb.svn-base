package com.yeahmobi.yscheduler.agentframework.zookeeper;

import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.BoundedExponentialBackoffRetry;

import com.yeahmobi.yscheduler.model.Attempt;
import com.yeahmobi.yscheduler.model.type.AttemptStatus;

public class ZookeeperManager implements AssignmentManager, AgentManager {

    private static final int       DEFAULT_MAX_SLEEP_TIME        = 30000;
    private static final int       DEFAULT_BASE_SLEEP_TIME       = 500;

    private static final int       DEFAULT_SESSION_TIMEOUT_MS    = 60 * 1000;
    private static final int       DEFAULT_CONNECTION_TIMEOUT_MS = 15 * 1000;

    private static final String    NAMESPACE                     = "yscheduler";

    private final CuratorFramework client;

    public ZookeeperManager(String rootPath, String zkUrl, int sessionTimeoutMs, int connectionTimeoutMs,
                            RetryPolicy retryPolicy) throws Exception {
        // 构造并启动zk client
        if (rootPath == null) {
            rootPath = NAMESPACE;
        }
        this.client = CuratorFrameworkFactory.builder().connectString(zkUrl).sessionTimeoutMs(sessionTimeoutMs).connectionTimeoutMs(connectionTimeoutMs).namespace(rootPath).retryPolicy(retryPolicy).build();
        this.client.start();
    }

    public ZookeeperManager(String zkUrl, int sessionTimeoutMs, int connectionTimeoutMs) throws Exception {
        this(null, zkUrl, sessionTimeoutMs, connectionTimeoutMs,
             new BoundedExponentialBackoffRetry(DEFAULT_BASE_SLEEP_TIME, DEFAULT_MAX_SLEEP_TIME, Integer.MAX_VALUE));
    }

    public ZookeeperManager(String rootPath, String zkUrl, int sessionTimeoutMs, int connectionTimeoutMs)
                                                                                                         throws Exception {
        this(rootPath, zkUrl, sessionTimeoutMs, connectionTimeoutMs,
             new BoundedExponentialBackoffRetry(DEFAULT_BASE_SLEEP_TIME, DEFAULT_MAX_SLEEP_TIME, Integer.MAX_VALUE));
    }

    public ZookeeperManager(String zkUrl, RetryPolicy retryPolicy) throws Exception {
        this(null, zkUrl, DEFAULT_SESSION_TIMEOUT_MS, DEFAULT_CONNECTION_TIMEOUT_MS, retryPolicy);
    }

    public ZookeeperManager(String rootPath, String zkUrl, RetryPolicy retryPolicy) throws Exception {
        this(rootPath, zkUrl, DEFAULT_SESSION_TIMEOUT_MS, DEFAULT_CONNECTION_TIMEOUT_MS, retryPolicy);
    }

    public ZookeeperManager(String zkUrl) throws Exception {
        this(null, zkUrl, DEFAULT_SESSION_TIMEOUT_MS, DEFAULT_CONNECTION_TIMEOUT_MS,
             new BoundedExponentialBackoffRetry(DEFAULT_BASE_SLEEP_TIME, DEFAULT_MAX_SLEEP_TIME, Integer.MAX_VALUE));
    }

    public ZookeeperManager(String rootPath, String zkUrl) throws Exception {
        this(rootPath, zkUrl, DEFAULT_SESSION_TIMEOUT_MS, DEFAULT_CONNECTION_TIMEOUT_MS,
             new BoundedExponentialBackoffRetry(DEFAULT_BASE_SLEEP_TIME, DEFAULT_MAX_SLEEP_TIME, Integer.MAX_VALUE));
    }

    public void assignTo(long agentId, Attempt attempt) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Auto-generated method stub");
    }

    public void addAssignmentListener(AssignmentListener listener) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Auto-generated method stub");
    }

    public void changeStatus(long agentId, long attemptId, AttemptStatus status) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Auto-generated method stub");
    }

    public void addStatusChangedListener(StatusChangedListener listener) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Auto-generated method stub");
    }

    public void cancel(long agentId, long attemptId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Auto-generated method stub");
    }

    public void addCancelListener(CancelListener listener) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Auto-generated method stub");
    }

    public long getAgentId(String host) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Auto-generated method stub");
    }

    public void active(long agentId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Auto-generated method stub");
    }

    public List<Long> activeList() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Auto-generated method stub");
    }

    public long nextActive() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Auto-generated method stub");
    }

}
