package com.yeahmobi.yscheduler.model.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.yeahmobi.yscheduler.common.Constants;
import com.yeahmobi.yscheduler.common.Paginator;
import com.yeahmobi.yscheduler.model.Agent;
import com.yeahmobi.yscheduler.model.Task;
import com.yeahmobi.yscheduler.model.TaskExample;
import com.yeahmobi.yscheduler.model.TaskExample.Criteria;
import com.yeahmobi.yscheduler.model.User;
import com.yeahmobi.yscheduler.model.common.NameValuePair;
import com.yeahmobi.yscheduler.model.common.Query;
import com.yeahmobi.yscheduler.model.dao.TaskDao;
import com.yeahmobi.yscheduler.model.service.AgentService;
import com.yeahmobi.yscheduler.model.service.TaskService;
import com.yeahmobi.yscheduler.model.service.UserService;
import com.yeahmobi.yscheduler.model.type.DependingStatus;
import com.yeahmobi.yscheduler.model.type.TaskStatus;
import com.yeahmobi.yscheduler.model.type.TaskType;

@Service
public class TaskServiceImpl implements TaskService {

    private static final String ROOT_NODE_COMMAND               = "echo %s root node start";

    private static final String HEARTBEAT_TASK_NAME_PREFIX_LIKE = Constants.HEARTBEAT_TASK_NAME_PREFIX + "%";

    @Value("#{confProperties['serverDomain']}")
    private String              serverDomain;

    @Autowired
    private TaskDao             taskDao;

    @Autowired
    private UserService         userService;

    @Autowired
    private AgentService        agentService;

    private String              attachmentServerUri;

    @Value("#{confProperties['storageServerUri']}")
    private String              storageServerUri;

    @PostConstruct
    public void init() {
        this.attachmentServerUri = this.storageServerUri + "/download";
    }

    public Task get(long id) {
        return this.taskDao.selectByPrimaryKey(id);
    }

    public List<Task> list(Query query, int pageNum, Paginator paginator, long userId) {
        TaskExample example = new TaskExample();

        Criteria criteria = example.createCriteria();

        query(query, criteria);

        example.setOrderByClause("create_time DESC");

        authorityCheck(criteria, userId);

        int count = this.taskDao.countByExample(example);

        paginator.setItemsPerPage(Constants.PAGE_SIZE);
        paginator.setItems(count);
        paginator.setPage(pageNum);

        int offset = paginator.getBeginIndex() > 0 ? paginator.getBeginIndex() - 1 : 0;
        int limit = Constants.PAGE_SIZE;

        RowBounds rowBounds = new RowBounds(offset, limit);

        List<Task> list = this.taskDao.selectByExampleWithBLOBsWithRowbounds(example, rowBounds);

        return list;
    }

    private void query(Query query, Criteria criteria) {
        if (StringUtils.isNotBlank(query.getName())) {
            criteria.andNameLike("%" + query.getName() + "%");
        }
        if (query.getOwner() != null) {
            criteria.andOwnerEqualTo(query.getOwner());
        }
        if (query.getTaskStatus() != null) {
            criteria.andStatusEqualTo(query.getTaskStatus());
        }
        if (query.getTaskType() != null) {
            criteria.andTypeEqualTo(query.getTaskType());
        }
    }

    @SuppressWarnings("unchecked")
    private void authorityCheck(Criteria criteria, long userId) {
        User user = this.userService.get(userId);
        List<User> teamMembers = this.userService.listByTeam(user.getTeamId());

        List<Long> teamMemberIds = new ArrayList<Long>(CollectionUtils.collect(teamMembers, new Transformer() {

            public Long transform(Object user) {
                return ((User) user).getId();
            }
        }));

        criteria.andStatusNotEqualTo(TaskStatus.REMOVED).andOwnerIn(teamMemberIds);
    }

    public void add(Task task) {
        Date time = new Date();
        task.setCreateTime(time);
        task.setUpdateTime(time);
        task.setLastScheduleTime(time);
        this.taskDao.insertSelective(task);
    }

    public List<NameValuePair> list(long userId) {
        TaskExample example = new TaskExample();
        Criteria criteria = example.createCriteria();
        authorityCheck(criteria, userId);
        List<Task> tasks = this.taskDao.selectByExampleWithBLOBs(example);
        List<NameValuePair> result = new ArrayList<NameValuePair>();
        for (Task task : tasks) {
            NameValuePair pair = new NameValuePair();
            pair.setValue(task.getId());
            pair.setName(task.getName());
            result.add(pair);
        }
        return result;
    }

    public void update(Task task) {
        Date time = new Date();
        Task oldTask = this.taskDao.selectByPrimaryKey(task.getId());
        task.setUpdateTime(time);
        if (TaskStatus.PAUSED.equals(oldTask.getStatus()) && TaskStatus.OPEN.equals(task.getStatus())) {
            task.setLastScheduleTime(time);
        }
        this.taskDao.updateByPrimaryKeySelective(task);
    }

    public boolean canModify(long taskId, long userId) {
        Task task = this.taskDao.selectByPrimaryKey(taskId);
        if (task != null) {
            if (task.getOwner() == userId) {
                return true;
            } else {
                User user = this.userService.get(userId);
                User owner = this.userService.get(task.getOwner());
                return user.getTeamId().equals(owner.getTeamId());
            }
        }

        return false;
    }

    public boolean nameExist(String name) {
        TaskExample example = new TaskExample();
        example.createCriteria().andNameEqualTo(name);
        return this.taskDao.selectByExample(example).size() != 0;
    }

    public List<Task> list(TaskStatus status) {
        TaskExample example = new TaskExample();
        example.createCriteria().andStatusEqualTo(status);
        return this.taskDao.selectByExampleWithBLOBs(example);
    }

    public void updateLastScheduleTime(Long id, Date scheduleTime) {
        Task record = new Task();
        record.setId(id);
        record.setLastScheduleTime(scheduleTime);
        this.taskDao.updateByPrimaryKeySelective(record);
    }

    public boolean hasTaskAttachedToAgent(long agentId) {
        TaskExample example = new TaskExample();
        // 排除掉内置的任务
        example.createCriteria().andAgentIdEqualTo(agentId).andStatusIn(Arrays.asList(new TaskStatus[] {
                                                                                TaskStatus.OPEN, TaskStatus.PAUSED })).andNameNotLike("\\_%");
        return this.taskDao.selectByExample(example).size() != 0;
    }

    @SuppressWarnings("unchecked")
    public boolean hasTaskAttachedToTeamAgents(long teamId) {
        TaskExample example = new TaskExample();
        List<Long> userIds = (List<Long>) CollectionUtils.collect(this.userService.listByTeam(teamId),
                                                                  new Transformer() {

                                                                      public Object transform(Object input) {
                                                                          return ((User) input).getId();
                                                                      }
                                                                  });
        example.createCriteria().andAgentIdIsNull().andOwnerIn(userIds).andStatusIn(Arrays.asList(new TaskStatus[] {
                                                                                            TaskStatus.OPEN,
                                                                                            TaskStatus.PAUSED })).andNameNotLike("\\_%");
        return this.taskDao.selectByExample(example).size() != 0;
    }

    public void addHeartbeatTask(Agent agent) {
        Task heartbeatTask = new Task();
        heartbeatTask.setAgentId(agent.getId());
        // curl心跳接口
        String heartbeatUrl = "http://" + this.serverDomain + "/heartbeat/agent/active?agentId="
                              + heartbeatTask.getAgentId() + "\\&agentVersion=${agentVersion}";
        heartbeatTask.setCommand("curl " + heartbeatUrl);
        heartbeatTask.setCrontab("0 * * * * *");
        heartbeatTask.setDescription("Hearbeat task for agent " + agent.getName());
        heartbeatTask.setName(Constants.HEARTBEAT_TASK_NAME_PREFIX + agent.getName());
        // monitor用户的id
        heartbeatTask.setOwner(Constants.MONITOR_USER_ID);
        heartbeatTask.setRetryTimes(5);
        heartbeatTask.setTimeout(1);
        heartbeatTask.setType(TaskType.SHELL);
        heartbeatTask.setStatus(TaskStatus.OPEN);
        heartbeatTask.setCanSkip(false);
        heartbeatTask.setLastStatusDependency(DependingStatus.NONE);
        this.add(heartbeatTask);
    }

    // TODO 改为removeByName,agent名称从外面传进来
    public void removeHeartbeatTask(long agentId, String name) {
        TaskExample example = new TaskExample();
        example.createCriteria().andAgentIdEqualTo(agentId).andNameEqualTo(Constants.HEARTBEAT_TASK_NAME_PREFIX + name);
        this.taskDao.deleteByExample(example);
    }

    public void addUpgradeTask(Agent agent) {
        Task upgradeTask = new Task();
        upgradeTask.setAgentId(agent.getId());
        // curl心跳接口
        String upgradeShellDownloadUrl = this.attachmentServerUri + "?nameSpace=agentUpgradeShellFile\\&key=default ";
        String agentArchiveDownloadUrl = this.attachmentServerUri + "?nameSpace=agentArchive\\&key=default";
        upgradeTask.setCommand("wget " + upgradeShellDownloadUrl + " -O upgrade.sh;bash upgrade.sh "
                               + agentArchiveDownloadUrl + " ${agentHome}");
        upgradeTask.setDescription(String.format("Upgrade task for agent(name=%s) ", agent.getName()));
        upgradeTask.setName(Constants.UPGRADE_TASK_NAME_PREFIX + agent.getName());
        // admin用户的id
        upgradeTask.setOwner(Constants.ADMIN_USER_ID);
        upgradeTask.setRetryTimes(0);
        upgradeTask.setTimeout(5);
        upgradeTask.setType(TaskType.SHELL);
        upgradeTask.setStatus(TaskStatus.PAUSED);
        upgradeTask.setCanSkip(true);
        upgradeTask.setLastStatusDependency(DependingStatus.NONE);
        this.add(upgradeTask);

        agent.setUpgradeTaskId(upgradeTask.getId());
    }

    public void removeTask(long taskId) {
        this.taskDao.deleteByPrimaryKey(taskId);
    }

    public List<Task> listHeartbeatTask() {
        TaskExample example = new TaskExample();
        example.createCriteria().andNameLike(HEARTBEAT_TASK_NAME_PREFIX_LIKE);
        List<Task> tasks = this.taskDao.selectByExampleWithBLOBs(example);
        return tasks;
    }

    public Task get(String taskName) {
        TaskExample example = new TaskExample();
        example.createCriteria().andNameEqualTo(taskName);
        List<Task> tasks = this.taskDao.selectByExampleWithBLOBs(example);
        if ((tasks != null) && !tasks.isEmpty()) {
            return tasks.get(0);
        } else {
            return null;
        }
    }

    public Task updateTeamRootTaskName(String name, String newName) {
        Task task = getRootTask(name);
        if (task == null) {
            return addRootTask(newName);
        } else {
            task.setName(String.format(Constants.ROOT_NODE_PATTERN, newName));
            task.setCommand(String.format(ROOT_NODE_COMMAND, newName));
            update(task);
            return task;
        }
    }

    public Task getRootTask(String teamName) {
        Task root = null;
        if (StringUtils.isBlank(teamName)) {
            root = get(Constants.ROOT_NODE);
        } else {
            String taskName = String.format(Constants.ROOT_NODE_PATTERN, teamName);
            root = get(taskName);
        }
        return root;
    }

    public Task addRootTaskIfAbsent(String teamName) {
        Task root = null;
        if (StringUtils.isBlank(teamName)) {
            root = get(Constants.ROOT_NODE);
            if (root == null) {
                root = addRootTask("");
            }
        } else {
            String taskName = String.format(Constants.ROOT_NODE_PATTERN, teamName);
            root = get(taskName);
            if (root == null) {
                root = addRootTask(teamName);
            }
        }
        return root;
    }

    private Task addRootTask(String teamName) {
        Task task = new Task();
        if (StringUtils.isBlank(teamName)) {
            task.setName(Constants.ROOT_NODE);
        } else {
            task.setName(String.format(Constants.ROOT_NODE_PATTERN, teamName));
        }
        task.setId(null);
        task.setCommand(String.format(ROOT_NODE_COMMAND, teamName));
        List<Agent> agents = this.agentService.listInPlatform();
        if (agents.size() == 0) {
            throw new IllegalStateException("无法创建公共工作流根节点，请先添加一台机器到platform组！");
        } else {
            task.setAgentId(agents.get(0).getId());
        }
        long owner = this.userService.get(Constants.ADMIN_NAME).getId();
        task.setOwner(owner);
        task.setCanSkip(false);
        task.setLastStatusDependency(DependingStatus.NONE);
        task.setTimeout(5);
        task.setType(TaskType.SHELL);
        task.setRetryTimes(3);
        task.setStatus(TaskStatus.PAUSED);
        add(task);
        return task;
    }

    public List<NameValuePair> list() {
        TaskExample example = new TaskExample();
        List<Task> tasks = this.taskDao.selectByExampleWithBLOBs(example);
        List<NameValuePair> result = new ArrayList<NameValuePair>();
        for (Task task : tasks) {
            NameValuePair pair = new NameValuePair();
            pair.setValue(task.getId());
            pair.setName(task.getName());
            result.add(pair);
        }
        return result;
    }

    public List<Task> listRootTasks() {
        TaskExample example = new TaskExample();
        example.createCriteria().andNameLike("%" + Constants.ROOT_NODE);
        List<Task> tasks = this.taskDao.selectByExampleWithBLOBs(example);
        return tasks;
    }

    public boolean isRootTask(long id) {
        return isRootTask(get(id).getName());
    }

    public boolean isRootTask(String taskName) {
        return taskName.endsWith(Constants.ROOT_NODE);
    }

    public void setServerDomain(String serverDomain) {
        this.serverDomain = serverDomain;
    }

    public void updateAttachment(long taskId, String filename, Long version) {
        Task task = this.taskDao.selectByPrimaryKey(taskId);
        task.setAttachment(filename);
        task.setAttachmentVersion(version);
        this.taskDao.updateByPrimaryKey(task);
    }

    public void updateAgentId(long taskId, Long agentId) {
        Task task = this.taskDao.selectByPrimaryKey(taskId);
        task.setAgentId(agentId);
        this.taskDao.updateByPrimaryKey(task);
    }

}
