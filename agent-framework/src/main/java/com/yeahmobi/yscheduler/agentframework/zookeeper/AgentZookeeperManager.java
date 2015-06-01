package com.yeahmobi.yscheduler.agentframework.zookeeper;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.BoundedExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.WatchedEvent;

import com.yeahmobi.yscheduler.model.Attempt;
import com.yeahmobi.yscheduler.model.type.AttemptStatus;

public class AgentZookeeperManager extends AbstractZookeeperManager implements CuratorWatcher {

    private final AgentListener listener;
    private final long          agentId;
    private Set<Long>           attemptIds = new HashSet<Long>();

    public AgentZookeeperManager(String rootPath, String zkUrl, int sessionTimeoutMs, int connectionTimeoutMs,
                                 RetryPolicy retryPolicy, final String agentHost, final AgentListener listener)
                                                                                                               throws Exception {
        super(rootPath, zkUrl, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);

        this.agentId = _getAgentId(agentHost);
        this.listener = listener;

        // 监听/agents/[agentId]/assignments/，有新加任务就通知
        this.attemptIds.addAll(getAttemptIds(this.agentId, this));

    }

    public AgentZookeeperManager(String zkUrl, int sessionTimeoutMs, int connectionTimeoutMs, final String agentHost,
                                 final AgentListener listener) throws Exception {
        this(null, zkUrl, sessionTimeoutMs, connectionTimeoutMs,
             new BoundedExponentialBackoffRetry(DEFAULT_BASE_SLEEP_TIME, DEFAULT_MAX_SLEEP_TIME, MAX_RETRY_VALUE),
             agentHost, listener);
    }

    public AgentZookeeperManager(String rootPath, String zkUrl, int sessionTimeoutMs, int connectionTimeoutMs,
                                 final String agentHost, final AgentListener listener) throws Exception {
        this(rootPath, zkUrl, sessionTimeoutMs, connectionTimeoutMs,
             new BoundedExponentialBackoffRetry(DEFAULT_BASE_SLEEP_TIME, DEFAULT_MAX_SLEEP_TIME, MAX_RETRY_VALUE),
             agentHost, listener);
    }

    public AgentZookeeperManager(String zkUrl, RetryPolicy retryPolicy, final String agentHost,
                                 final AgentListener listener) throws Exception {
        this(null, zkUrl, DEFAULT_SESSION_TIMEOUT_MS, DEFAULT_CONNECTION_TIMEOUT_MS, retryPolicy, agentHost, listener);
    }

    public AgentZookeeperManager(String rootPath, String zkUrl, RetryPolicy retryPolicy, final String agentHost,
                                 final AgentListener listener) throws Exception {
        this(rootPath, zkUrl, DEFAULT_SESSION_TIMEOUT_MS, DEFAULT_CONNECTION_TIMEOUT_MS, retryPolicy, agentHost,
             listener);
    }

    public AgentZookeeperManager(String zkUrl, final String agentHost, final AgentListener listener) throws Exception {
        this(null, zkUrl, DEFAULT_SESSION_TIMEOUT_MS, DEFAULT_CONNECTION_TIMEOUT_MS,
             new BoundedExponentialBackoffRetry(DEFAULT_BASE_SLEEP_TIME, DEFAULT_MAX_SLEEP_TIME, MAX_RETRY_VALUE),
             agentHost, listener);
    }

    public AgentZookeeperManager(String rootPath, String zkUrl, final String agentHost, final AgentListener listener)
                                                                                                                     throws Exception {
        this(rootPath, zkUrl, DEFAULT_SESSION_TIMEOUT_MS, DEFAULT_CONNECTION_TIMEOUT_MS,
             new BoundedExponentialBackoffRetry(DEFAULT_BASE_SLEEP_TIME, DEFAULT_MAX_SLEEP_TIME, MAX_RETRY_VALUE),
             agentHost, listener);
    }

    // public void init() throws Exception {
    // // 首次启动时，获取agents时(并监听agents)，新agent加入，就监听/agents/[agentId]/assignments/
    // // 有新加任务就通知this(attempt监听者)
    // List<Long> agentIds = this._getAgentIds();
    //
    // }

    // 监听/agents/[agentId]/assignments/，有新加任务就通知
    // 监听/agents/[agentId]/assignments/[attemptId]/cancel
    public void process(WatchedEvent event) throws Exception {
        String path = event.getPath();
        if (path.equals(getAssignmentsRoot(this.agentId))) {// 收到新attempt
            List<Long> attemptIds = getAttemptIds(this.agentId);

            for (long attemptId : attemptIds) {
                if (!this.attemptIds.contains(attemptId)) {
                    // 获取,解析
                    Attempt attempt = getAttempt(this.agentId, attemptId);
                    // 添加到内存
                    this.attemptIds.add(attemptId);
                    // 触发listener
                    this.listener.onAssign(this.agentId, attempt);
                }
            }

        } else {// 收到/agents/[agentId]/assignments/[attemptId]/cancel
            // 从path中解析出attemptId和cancel
            String[] splits = StringUtils.split(path.substring(getAssignmentsRoot(this.agentId).length()), '/');
            long attemptId = Long.parseLong(splits[0]);
            boolean cancel = Boolean.parseBoolean(splits[1]);
            if (cancel) {
                this.listener.onCancel(this.agentId, attemptId);
            }
        }

    }

    public void changeStatus(long agentId, long attemptId, AttemptStatus status) throws Exception {
        String statusPath = getStatusPath(agentId, attemptId);
        this.client.setData().forPath(statusPath, BigInteger.valueOf(status.getId()).toByteArray());
    }

    public List<Attempt> getUncompletedAttempts() throws Exception {
        // 遍历，获取未完成的attempt，agent启动时需要的调用
        List<Attempt> list = new ArrayList<Attempt>();
        List<Long> attemptIds = this.getAttemptIds(this.agentId);
        if (attemptIds != null) {
            for (long attemptId : attemptIds) {
                AttemptStatus status = this.getAttemptStatus(this.agentId, attemptId);
                if (!status.isCompleted()) {
                    Attempt attempt = getAttempt(attemptId, attemptId);
                    list.add(attempt);
                }
            }
        }

        return list;
    }

    /**
     * 监听/agents/[agentId]/assignments/[attemptId]/cancel，attempt有状态变更就通知
     *
     * @throws Exception
     */
    public boolean registerCancelWatcher(final long attemptId) throws Exception {
        // 并监听/agents/[agentId]/assignments/[attemptId]/cancelled，attempt有取消的变更就通知
        final String path = this.getCancelPath(this.agentId, attemptId);
        byte[] bytes = this.client.getData().usingWatcher(this).forPath(path);
        if (bytes != null) {
            return Boolean.parseBoolean(new String(bytes, ENCODE));
        }
        return false;
    }

    private long _getAgentId(String host) throws Exception {
        List<Long> ids = _getAgentIds();
        for (long agentId : ids) {
            String agentPath = PATH_AGENTS + "/" + agentId;
            String agentHost = new String(this.client.getData().forPath(agentPath));
            if (StringUtils.equals(agentHost, host)) {
                return agentId;
            }
        }
        throw new IllegalArgumentException(String.format("Agent not exists(host:%s)", host));
    }

    public long getAgentId() {
        return this.agentId;
    }

    public void active() throws Exception {
        String activeAgentPath = PATH_ACTIVE_LIST + "/" + this.agentId;
        try {
            this.client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(activeAgentPath);
        } catch (Exception e) {
            if (e instanceof NodeExistsException) {
                // ignore
            } else {
                throw e;
            }
        }
    }

    // private String getHost(Agent agent) {
    // String ip = agent.getIp();
    // String host = (StringUtils.contains(ip, ':')) ? ip : ip + ":" + Constants.DEFAULT_AGENT_PORT;
    // return host;
    // }

    public static void main(String[] args) throws Exception {
        // AgentZookeeperManager manager = new AgentZookeeperManager("172.20.0.180:2181");
        // manager.client.getChildren().usingWatcher(new CuratorWatcher() {
        //
        // public void process(WatchedEvent event) throws Exception {
        // System.out.println(event);
        // }
        //
        // }).forPath(PATH_AGENTS);
        //
        // Thread.sleep(Integer.MAX_VALUE);
    }

}
