package com.yeahmobi.yscheduler.agentframework.zookeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.BoundedExponentialBackoffRetry;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.WatchedEvent;

import com.yeahmobi.yscheduler.common.Constants;
import com.yeahmobi.yscheduler.model.Agent;
import com.yeahmobi.yscheduler.model.Attempt;
import com.yeahmobi.yscheduler.model.type.AttemptStatus;

public class WebZookeeperManager extends AbstractZookeeperManager implements CuratorWatcher {

    private AtomicInteger       loadbalanceIndex = new AtomicInteger(0);
    private volatile List<Long> activeAgentIds;

    private final WebListener   listener;

    public WebZookeeperManager(String rootPath, String zkUrl, int sessionTimeoutMs, int connectionTimeoutMs,
                               RetryPolicy retryPolicy, WebListener listener) throws Exception {
        super(rootPath, zkUrl, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);

        this.listener = listener;
        // init active agent list
        this.activeAgentIds = parseActiveAgentIds(this.client.getChildren().usingWatcher(this).forPath(PATH_ACTIVE_LIST));
        // // 扫描所有 /agents/[agentId]/assignments/[attemptId]/status，监听其status
        // // 已经完成的也监听吧，也没啥。而且就算被web的AttemptExecutor在持久化到db后将其移除，也不会收到通知
        // List<Long> agentIds = this._getAgentIds();
        // if (agentIds != null) {
        // for (long agentId : agentIds) {
        // List<Long> attemptIds = this._getAttemptIds(agentId);
        // if (attemptIds != null) {
        // for (long attemptId : attemptIds) {
        // getAttemptStatus(agentId, attemptId, this);
        // }
        // }
        // }
        // }

    }

    public WebZookeeperManager(String zkUrl, int sessionTimeoutMs, int connectionTimeoutMs, WebListener listener)
                                                                                                                 throws Exception {
        this(null, zkUrl, sessionTimeoutMs, connectionTimeoutMs,
             new BoundedExponentialBackoffRetry(DEFAULT_BASE_SLEEP_TIME, DEFAULT_MAX_SLEEP_TIME, MAX_RETRY_VALUE),
             listener);
    }

    public WebZookeeperManager(String rootPath, String zkUrl, int sessionTimeoutMs, int connectionTimeoutMs,
                               WebListener listener) throws Exception {
        this(rootPath, zkUrl, sessionTimeoutMs, connectionTimeoutMs,
             new BoundedExponentialBackoffRetry(DEFAULT_BASE_SLEEP_TIME, DEFAULT_MAX_SLEEP_TIME, MAX_RETRY_VALUE),
             listener);
    }

    public WebZookeeperManager(String zkUrl, RetryPolicy retryPolicy, WebListener listener) throws Exception {
        this(null, zkUrl, DEFAULT_SESSION_TIMEOUT_MS, DEFAULT_CONNECTION_TIMEOUT_MS, retryPolicy, listener);
    }

    public WebZookeeperManager(String rootPath, String zkUrl, RetryPolicy retryPolicy, WebListener listener)
                                                                                                            throws Exception {
        this(rootPath, zkUrl, DEFAULT_SESSION_TIMEOUT_MS, DEFAULT_CONNECTION_TIMEOUT_MS, retryPolicy, listener);
    }

    public WebZookeeperManager(String zkUrl, WebListener listener) throws Exception {
        this(null, zkUrl, DEFAULT_SESSION_TIMEOUT_MS, DEFAULT_CONNECTION_TIMEOUT_MS,
             new BoundedExponentialBackoffRetry(DEFAULT_BASE_SLEEP_TIME, DEFAULT_MAX_SLEEP_TIME, MAX_RETRY_VALUE),
             listener);
    }

    public WebZookeeperManager(String rootPath, String zkUrl, WebListener listener) throws Exception {
        this(rootPath, zkUrl, DEFAULT_SESSION_TIMEOUT_MS, DEFAULT_CONNECTION_TIMEOUT_MS,
             new BoundedExponentialBackoffRetry(DEFAULT_BASE_SLEEP_TIME, DEFAULT_MAX_SLEEP_TIME, MAX_RETRY_VALUE),
             listener);
    }

    // public void init() throws Exception {
    // // 首次启动时，获取agents时(并监听agents)，新agent加入，就监听/agents/[agentId]/assignments/
    // // 有新加任务就通知this(attempt监听者)
    // List<Long> agentIds = this._getAgentIds();
    //
    // }

    // 监听/agents/activeList, 监听 /agents/[agentId]/assignments/[attemptId]/status
    public void process(WatchedEvent event) throws Exception {
        String path = event.getPath();
        if (PATH_ACTIVE_LIST.equals(path)) {
            this.activeAgentIds = parseActiveAgentIds(WebZookeeperManager.this.client.getChildren().usingWatcher(this).forPath(PATH_ACTIVE_LIST));
        } else {
            // 从path中解析出agentId和attemptId
            String[] splits = StringUtils.split(path, '/');
            long agentId = Long.parseLong(splits[1]);
            long attemptId = Long.parseLong(splits[3]);
            AttemptStatus status = this.getAttemptStatus(agentId, attemptId, this);// 继续注册监听
            if (this.listener != null) {
                // 触发webListeners
                this.listener.onChange(agentId, attemptId, status);
            }
        }
    }

    private ArrayList<Long> parseActiveAgentIds(List<String> agentIdStrs) {
        ArrayList<Long> list = null;
        if (agentIdStrs != null) {
            list = new ArrayList<Long>(agentIdStrs.size());
            for (String agentIdStr : agentIdStrs) {
                long agentId = Long.parseLong(agentIdStr);
                list.add(agentId);
            }
        } else {
            list = new ArrayList<Long>(0);
        }
        return list;
    }

    /**
     * 监听/agents/[agentId]/assignments/[attemptId]/status，attempt有状态变更就通知
     *
     * @throws Exception
     */
    public void registerStatusWatcher(final long agentId, final long attemptId) throws Exception {
        final String path = this.getStatusPath(agentId, attemptId);
        byte[] bytes = this.client.getData().usingWatcher(this).forPath(path);
        AttemptStatus status = AttemptStatus.valueOf(Integer.parseInt(new String(bytes, ENCODE)));
        this.listener.onChange(agentId, attemptId, status);// 最新status要触发一次
    }

    public void assignTo(Attempt attempt) throws Exception {
        long agentId = attempt.getAgentId();
        String path = PATH_AGENTS + "/" + agentId + "/assignments/" + attempt.getId();
        String statusPath = path + "/status";
        String cancelPath = path + "/cancelled";
        String json = toJson(attempt);
        // json
        this.client.create().creatingParentsIfNeeded().forPath(path, toBytes(json));
        // status
        this.client.create().creatingParentsIfNeeded().forPath(statusPath, toBytes(attempt.getStatus().toString()));
        // cancel
        this.client.create().creatingParentsIfNeeded().forPath(cancelPath, toBytes("false"));
    }

    // public void changeStatus(long agentId, long attemptId, AttemptStatus status) throws Exception {
    // String statusPath = getStatusPath(agentId, attemptId);
    // this.client.setData().forPath(statusPath, BigInteger.valueOf(status.getId()).toByteArray());
    // }

    public void cancel(long agentId, long attemptId) throws Exception {
        String cancelPath = getCancelPath(agentId, attemptId);
        this.client.setData().forPath(cancelPath, toBytes("true"));
    }

    // public long getAgentId(String host) throws Exception {
    // List<Long> ids = _getAgentIds();
    // for (long agentId : ids) {
    // String agentPath = PATH_AGENTS + "/" + agentId;
    // String agentHost = new String(this.client.getData().forPath(agentPath));
    // if (StringUtils.equals(agentHost, host)) {
    // return agentId;
    // }
    // }
    // throw new IllegalArgumentException(String.format("Agent not exists(host:%s)", host));
    // }

    // public void active(long agentId) throws Exception {
    // String activeAgentPath = PATH_ACTIVE_LIST + "/" + agentId;
    // try {
    // this.client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(activeAgentPath);
    // } catch (Exception e) {
    // if (e instanceof NodeExistsException) {
    // // ignore
    // } else {
    // throw e;
    // }
    // }
    // }

    public List<Long> activeList() throws Exception {
        return this.activeAgentIds;
    }

    public long nextActive() {
        List<Long> activeAgentIds0 = this.activeAgentIds;
        int loadbalanceIndex0 = this.loadbalanceIndex.getAndIncrement();
        // ensure positive
        if (loadbalanceIndex0 != Integer.MIN_VALUE) {
            loadbalanceIndex0 = Math.abs(loadbalanceIndex0);
        } else {
            loadbalanceIndex0 = 0;
        }
        int index = loadbalanceIndex0 % activeAgentIds0.size();
        return activeAgentIds0.get(index);
    }

    public void addAgent(Agent agent) throws Exception {
        String agentPath = PATH_AGENTS + "/" + agent.getId();
        String host = getHost(agent);
        try {
            this.client.create().forPath(agentPath, toBytes(host));
        } catch (Exception e) {
            if (e instanceof NodeExistsException) {
                // modify if exists
                this.client.setData().forPath(agentPath, toBytes(host));
            } else {
                throw e;
            }
        }
    }

    private String getHost(Agent agent) {
        String ip = agent.getIp();
        String host = (StringUtils.contains(ip, ':')) ? ip : ip + ":" + Constants.DEFAULT_AGENT_PORT;
        return host;
    }

    public void delAgent(Agent agent) throws Exception {
        String agentPath = PATH_AGENTS + "/" + agent.getId();
        this.client.delete().forPath(agentPath);
    }

    public void modAgent(Agent agent) throws Exception {
        String agentPath = PATH_AGENTS + "/" + agent.getId();
        String host = getHost(agent);
        this.client.setData().forPath(agentPath, toBytes(host));
    }

    public static void main(String[] args) throws Exception {
        System.out.println((-9) % -2);

        WebZookeeperManager manager = new WebZookeeperManager("172.20.0.180:2181", null);

        // Attempt attempt = new Attempt();
        // attempt.setAgentId(1L);
        // attempt.setId(4L);
        // attempt.setStatus(AttemptStatus.INIT);
        // manager.assignTo(attempt);

        // System.out.println(manager.getAgentId("127.0.0.1:24368"));

        System.out.println(manager.nextActive());
        System.out.println(manager.nextActive());
        System.out.println(manager.nextActive());
        System.out.println(manager.nextActive());
        System.out.println(manager.nextActive());
        // manager.active(1);
    }

}
