package com.yeahmobi.yscheduler.model.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeahmobi.yscheduler.common.Constants;
import com.yeahmobi.yscheduler.common.Paginator;
import com.yeahmobi.yscheduler.model.Task;
import com.yeahmobi.yscheduler.model.Team;
import com.yeahmobi.yscheduler.model.User;
import com.yeahmobi.yscheduler.model.Workflow;
import com.yeahmobi.yscheduler.model.WorkflowDetail;
import com.yeahmobi.yscheduler.model.WorkflowExample;
import com.yeahmobi.yscheduler.model.WorkflowExample.Criteria;
import com.yeahmobi.yscheduler.model.common.Query;
import com.yeahmobi.yscheduler.model.dao.WorkflowDao;
import com.yeahmobi.yscheduler.model.service.TaskService;
import com.yeahmobi.yscheduler.model.service.TeamService;
import com.yeahmobi.yscheduler.model.service.UserService;
import com.yeahmobi.yscheduler.model.service.WorkflowDetailService;
import com.yeahmobi.yscheduler.model.service.WorkflowService;
import com.yeahmobi.yscheduler.model.type.DependingStatus;
import com.yeahmobi.yscheduler.model.type.WorkflowStatus;

@Service
public class WorkflowServiceImpl implements WorkflowService {

    @Autowired
    private WorkflowDao           workflowDao;

    @Autowired
    private TaskService           taskService;

    @Autowired
    private TeamService           teamService;

    @Autowired
    private UserService           userService;

    @Autowired
    private WorkflowDetailService workflowDetailService;

    public Workflow get(long id) {
        return this.workflowDao.selectByPrimaryKey(id);
    }

    public List<Workflow> list(Query query, int pageNum, Paginator paginator, long userId, boolean common) {
        WorkflowExample example = new WorkflowExample();

        example.setOrderByClause("create_time DESC");

        Criteria criteria = example.createCriteria();
        query(query, criteria);
        criteria.andCommonEqualTo(common);
        criteria.andStatusNotEqualTo(WorkflowStatus.REMOVED);
        if (!common) {
            authorityCheck(criteria, userId);
        }

        int count = this.workflowDao.countByExample(example);

        paginator.setItemsPerPage(Constants.PAGE_SIZE);
        paginator.setItems(count);
        paginator.setPage(pageNum);

        int offset = paginator.getBeginIndex() - 1;
        int limit = Constants.PAGE_SIZE;

        RowBounds rowBounds = new RowBounds(offset, limit);

        List<Workflow> list = this.workflowDao.selectByExampleWithRowbounds(example, rowBounds);

        return list;
    }

    private void query(Query query, Criteria criteria) {
        if (StringUtils.isNotBlank(query.getName())) {
            criteria.andNameLike("%" + query.getName() + "%");
        }
        if (query.getOwner() != null) {
            criteria.andOwnerEqualTo(query.getOwner());
        }
        if (query.getWorkflowStatus() != null) {
            criteria.andStatusEqualTo(query.getWorkflowStatus());
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

        criteria.andOwnerIn(teamMemberIds);

    }

    public void createOrUpdate(Workflow workflow) {
        Date now = new Date();
        if (workflow.getId() == null) {
            workflow.setLastScheduleTime(now);
            create(workflow);
        } else {
            Workflow oldWorkflow = this.workflowDao.selectByPrimaryKey(workflow.getId());
            if (WorkflowStatus.PAUSED.equals(oldWorkflow.getStatus())
                && WorkflowStatus.OPEN.equals(workflow.getStatus())) {
                workflow.setLastScheduleTime(now);
            }
            update(workflow);
        }
    }

    private void create(Workflow workflow) {
        Date time = new Date();
        workflow.setCreateTime(time);
        workflow.setUpdateTime(time);
        if ((workflow.getCommon() != null) && workflow.getCommon()) {
            workflow.setCanSkip(false);
            workflow.setLastStatusDependency(DependingStatus.NONE);
        }
        this.workflowDao.insertSelective(workflow);
        if ((workflow.getCommon() != null) && workflow.getCommon()) {
            buildRootTasks(workflow.getId());
        }
    }

    private void buildRootTasks(long workflowId) {
        List<WorkflowDetail> details = new ArrayList<WorkflowDetail>();
        List<List<Long>> dependencyList = new ArrayList<List<Long>>();
        List<Team> teams = this.teamService.list();

        Task task = this.taskService.addRootTaskIfAbsent("");
        WorkflowDetail detail = new WorkflowDetail();
        detail.setTaskId(task.getId());
        detail.setWorkflowId(workflowId);
        detail.setLastStatusDependency(DependingStatus.NONE);
        details.add(detail);
        dependencyList.add(new ArrayList<Long>());
        List<Long> dependenciesOnRoot = new ArrayList<Long>();
        dependenciesOnRoot.add(task.getId());
        for (Team team : teams) {
            task = this.taskService.addRootTaskIfAbsent(team.getName());
            detail = new WorkflowDetail();
            detail.setTaskId(task.getId());
            detail.setWorkflowId(workflowId);
            detail.setLastStatusDependency(DependingStatus.NONE);
            details.add(detail);
            dependencyList.add(dependenciesOnRoot);
        }
        this.workflowDetailService.save(workflowId, details, dependencyList);
    }

    private void update(Workflow workflow) {
        Date time = new Date();
        workflow.setUpdateTime(time);
        this.workflowDao.updateByPrimaryKeySelective(workflow);
    }

    public boolean canModify(long workflowId, long userId) {
        Workflow workflow = this.workflowDao.selectByPrimaryKey(workflowId);
        if (workflow != null) {
            if (workflow.getOwner() == userId) {
                return true;
            } else {
                User user = this.userService.get(userId);
                User owner = this.userService.get(workflow.getOwner());
                return user.getTeamId().equals(owner.getTeamId());
            }
        }

        return false;
    }

    public boolean nameExist(String name) {
        WorkflowExample example = new WorkflowExample();
        example.createCriteria().andNameEqualTo(name);
        return this.workflowDao.selectByExample(example).size() != 0;
    }

    public List<Workflow> listAll(WorkflowStatus status) {
        WorkflowExample example = new WorkflowExample();
        example.createCriteria().andStatusEqualTo(status);
        return this.workflowDao.selectByExample(example);
    }

    public void updateScheduleTime(long workflowId, Date time) {
        Workflow record = new Workflow();
        record.setId(workflowId);
        record.setLastScheduleTime(time);
        this.workflowDao.updateByPrimaryKeySelective(record);
    }

    public List<Workflow> listAllPrivate() {
        WorkflowExample example = new WorkflowExample();
        example.createCriteria().andCommonEqualTo(false);
        return this.workflowDao.selectByExample(example);
    }

    public List<Workflow> listAllCommon() {
        WorkflowExample example = new WorkflowExample();
        example.createCriteria().andCommonEqualTo(true);
        return this.workflowDao.selectByExample(example);
    }
}
