package com.yeahmobi.yscheduler.model.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeahmobi.yscheduler.common.Constants;
import com.yeahmobi.yscheduler.common.Paginator;
import com.yeahmobi.yscheduler.model.Agent;
import com.yeahmobi.yscheduler.model.AgentExample;
import com.yeahmobi.yscheduler.model.dao.AgentDao;
import com.yeahmobi.yscheduler.model.service.AgentService;
import com.yeahmobi.yscheduler.model.service.TaskService;

@Service
public class AgentServiceImpl implements AgentService {

    @Autowired
    private AgentDao    agentDao;

    @Autowired
    private TaskService taskService;

    public Agent get(long id) {
        return this.agentDao.selectByPrimaryKey(id);
    }

    public List<Agent> list(int pageNum, Paginator paginator) {
        AgentExample example = new AgentExample();

        int count = this.agentDao.countByExample(example);

        paginator.setItemsPerPage(Constants.PAGE_SIZE);
        paginator.setItems(count);
        paginator.setPage(pageNum);

        int offset = paginator.getBeginIndex() - 1;
        int limit = Constants.PAGE_SIZE;

        RowBounds rowBounds = new RowBounds(offset, limit);

        List<Agent> list = this.agentDao.selectByExampleWithRowbounds(example, rowBounds);

        return list;
    }

    public List<Agent> list(long teamId, int pageNum, Paginator paginator) {
        AgentExample example = new AgentExample();
        example.createCriteria().andTeamIdEqualTo(teamId);
        int count = this.agentDao.countByExample(example);

        paginator.setItemsPerPage(Constants.PAGE_SIZE);
        paginator.setItems(count);
        paginator.setPage(pageNum);

        int offset = paginator.getBeginIndex() - 1;
        int limit = Constants.PAGE_SIZE;

        RowBounds rowBounds = new RowBounds(offset, limit);

        List<Agent> list = this.agentDao.selectByExampleWithRowbounds(example, rowBounds);

        return list;
    }

    public List<Agent> list() {
        return this.agentDao.selectByExample(new AgentExample());
    }

    private boolean nameExists(String name) {
        AgentExample example = new AgentExample();
        example.createCriteria().andNameEqualTo(name);
        return this.agentDao.selectByExample(example).size() != 0;
    }

    public void add(Agent agent) {
        if (nameExists(agent.getName())) {
            throw new IllegalArgumentException(String.format("Agent %s 已经存在", agent.getName()));
        }
        Date now = new Date();
        agent.setCreateTime(now);
        agent.setUpdateTime(now);
        this.agentDao.insertSelective(agent);

        // 新增heartbeat任务
        this.taskService.addHeartbeatTask(agent);
        // 新增upgrade任务
        this.taskService.addUpgradeTask(agent);
        // upgradeTaskId已经回填到agent中，此时可以更新
        this.agentDao.updateByPrimaryKeySelective(agent);
    }

    public void remove(long agentId, String name) {
        // 是否允许删除
        if (this.taskService.hasTaskAttachedToAgent(agentId)) {
            throw new IllegalArgumentException(String.format("Agent删除失败(原因：有Task关联在本Agent上)"));
        } else {
            long teamId = get(agentId).getTeamId();
            int size = list(teamId, true).size();
            if (((size == 1) || (size == 0)) && this.taskService.hasTaskAttachedToTeamAgents(teamId)) {
                throw new IllegalArgumentException(String.format("Agent删除失败(原因：有Task关联在本组的Agent上，而这是本组唯一的机器)"));
            }
        }

        // 删除heartbeat任务
        this.taskService.removeHeartbeatTask(agentId, name);
        // 删除upgrade任务
        Agent agent = this.get(agentId);
        if (agent.getUpgradeTaskId() != null) {
            this.taskService.removeTask(agent.getUpgradeTaskId());
        }
        this.agentDao.deleteByPrimaryKey(agentId);
    }

    public void update(long agentId, String ip, long teamId) {
        Agent agent = new Agent();
        agent.setId(agentId);
        agent.setIp(ip);
        agent.setTeamId(teamId);
        agent.setUpdateTime(new Date());
        this.agentDao.updateByPrimaryKeySelective(agent);
    }

    public List<Agent> listInPlatform() {
        AgentExample example = new AgentExample();
        example.createCriteria().andNameLike("platform%");

        List<Agent> list = this.agentDao.selectByExample(example);

        return list;
    }

    public Agent get(String name) {
        AgentExample example = new AgentExample();
        example.createCriteria().andNameEqualTo(name);

        List<Agent> list = this.agentDao.selectByExample(example);

        if ((list == null) || list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public List<Agent> list(long teamId, boolean enable) {
        AgentExample example = new AgentExample();
        example.createCriteria().andTeamIdEqualTo(teamId).andEnableEqualTo(enable);

        List<Agent> list = this.agentDao.selectByExample(example);

        return list;
    }

    public void updateStatus(long agentId, boolean enable) {
        // 是否允许下线
        if (enable == false) {
            if (this.taskService.hasTaskAttachedToAgent(agentId)) {
                throw new IllegalArgumentException(String.format("Agent下线失败(原因：有Task关联在本Agent上)"));
            } else {
                long teamId = get(agentId).getTeamId();
                int size = list(teamId, true).size();
                if (((size == 1) || (size == 0)) && this.taskService.hasTaskAttachedToTeamAgents(teamId)) {
                    throw new IllegalArgumentException(String.format("Agent下线失败(原因：有Task关联在本组的Agent上，而这是本组唯一的机器)"));
                }
            }
        }
        Agent agent = this.agentDao.selectByPrimaryKey(agentId);
        agent.setEnable(enable);
        this.agentDao.updateByPrimaryKey(agent);
    }

    public void updateVersion(long agentId, String agentVersion) {
        Agent agent = this.agentDao.selectByPrimaryKey(agentId);
        agent.setVersion(agentVersion);
        this.agentDao.updateByPrimaryKey(agent);
    }

}
