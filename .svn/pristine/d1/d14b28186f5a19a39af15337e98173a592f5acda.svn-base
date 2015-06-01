package com.yeahmobi.yscheduler.model.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeahmobi.yscheduler.common.Constants;
import com.yeahmobi.yscheduler.common.CrontabUtils;
import com.yeahmobi.yscheduler.common.Paginator;
import com.yeahmobi.yscheduler.model.Task;
import com.yeahmobi.yscheduler.model.TaskInstance;
import com.yeahmobi.yscheduler.model.TaskInstanceExample;
import com.yeahmobi.yscheduler.model.TaskInstanceExample.Criteria;
import com.yeahmobi.yscheduler.model.common.NameValuePair;
import com.yeahmobi.yscheduler.model.common.Query;
import com.yeahmobi.yscheduler.model.dao.TaskInstanceDao;
import com.yeahmobi.yscheduler.model.service.TaskInstanceService;
import com.yeahmobi.yscheduler.model.service.TaskService;
import com.yeahmobi.yscheduler.model.service.TeamService;
import com.yeahmobi.yscheduler.model.service.UserService;
import com.yeahmobi.yscheduler.model.type.TaskInstanceStatus;

@Service
public class TaskInstanceServiceImpl implements TaskInstanceService {

    @Autowired
    private TaskInstanceDao instanceDao;

    @Autowired
    private TaskService     taskService;

    @Autowired
    private UserService     userService;

    @Autowired
    private TeamService     teamService;

    @PostConstruct
    public void init() {
    }

    public TaskInstance get(long id) {
        return this.instanceDao.selectByPrimaryKey(id);
    }

    public List<TaskInstance> list(Query query, long taskId, int pageNum, Paginator paginator) {
        TaskInstanceExample example = new TaskInstanceExample();

        Criteria criteria = example.or();

        query(query, criteria);

        criteria.andTaskIdEqualTo(taskId);

        int count = this.instanceDao.countByExample(example);

        paginator.setItemsPerPage(Constants.PAGE_SIZE);
        paginator.setItems(count);
        paginator.setPage(pageNum);

        int offset = paginator.getBeginIndex() - 1;
        int limit = Constants.PAGE_SIZE;

        RowBounds rowBounds = new RowBounds(offset, limit);

        example.setOrderByClause("id DESC");
        List<TaskInstance> instances = this.instanceDao.selectByExampleWithRowbounds(example, rowBounds);

        return instances;
    }

    private void query(Query query, Criteria criteria) {
        if (query.getTaskScheduleType() != null) {
            switch (query.getTaskScheduleType()) {
                case AUTO:
                    criteria.andScheduleTimeIsNotNull();
                    break;
                case MANAUAL:
                    criteria.andScheduleTimeIsNull().andWorkflowInstanceIdIsNull();
                    break;
                case WORKFLOW_SCHEDULED:
                    criteria.andWorkflowInstanceIdIsNotNull();
                    break;
            }
        }
        if (query.getTaskInstanceStatus() != null) {
            criteria.andStatusEqualTo(query.getTaskInstanceStatus());
        }
    }

    public List<TaskInstance> listAll(long taskId) {
        TaskInstanceExample example = new TaskInstanceExample();

        example.createCriteria().andTaskIdEqualTo(taskId);
        example.setOrderByClause("id DESC");

        return this.instanceDao.selectByExample(example);
    }

    public void save(TaskInstance instance) {
        Date now = new Date();
        instance.setCreateTime(now);
        instance.setUpdateTime(now);
        this.instanceDao.insert(instance);

    }

    public List<TaskInstance> getAllUncompleteds() {
        TaskInstanceExample example = new TaskInstanceExample();
        example.createCriteria().andStatusEqualTo(TaskInstanceStatus.RUNNING);
        example.or().andStatusEqualTo(TaskInstanceStatus.READY);
        return this.instanceDao.selectByExample(example);
    }

    public void updateStatus(Long instanceId, TaskInstanceStatus status) {
        Date now = new Date();
        TaskInstance record = new TaskInstance();
        record.setId(instanceId);
        record.setStatus(status);
        record.setUpdateTime(now);

        if (status.isCompleted()) {
            record.setEndTime(now);
        } else if (TaskInstanceStatus.RUNNING.equals(status)) {
            record.setStartTime(now);
        }

        this.instanceDao.updateByPrimaryKeySelective(record);
    }

    public void deleteByWorkflowInstanceId(long workflowInstanceId) {
        TaskInstanceExample example = new TaskInstanceExample();
        example.createCriteria().andWorkflowInstanceIdEqualTo(workflowInstanceId);
        this.instanceDao.deleteByExample(example);
    }

    public List<TaskInstance> listAllDependencyWait() {
        TaskInstanceExample example = new TaskInstanceExample();
        example.createCriteria().andStatusEqualTo(TaskInstanceStatus.DEPENDENCY_WAIT);
        return this.instanceDao.selectByExample(example);
    }

    public List<TaskInstance> listByWorkflowInstanceId(long workflowInstanceId) {
        TaskInstanceExample example = new TaskInstanceExample();
        example.createCriteria().andWorkflowInstanceIdEqualTo(workflowInstanceId);
        return this.instanceDao.selectByExample(example);
    }

    public List<TaskInstance> listByWorkflowInstanceId(Long instanceId, int pageNum, Paginator paginator) {
        TaskInstanceExample example = new TaskInstanceExample();
        example.createCriteria().andWorkflowInstanceIdEqualTo(instanceId);

        int count = this.instanceDao.countByExample(example);

        paginator.setItemsPerPage(Constants.PAGE_SIZE);
        paginator.setItems(count);
        paginator.setPage(pageNum);

        int offset = paginator.getBeginIndex() - 1;
        int limit = Constants.PAGE_SIZE;

        RowBounds rowBounds = new RowBounds(offset, limit);

        example.setOrderByClause("start_time DESC");

        return this.instanceDao.selectByExampleWithRowbounds(example, rowBounds);
    }

    // (相同taskId,存在schedule，状态未完成的Inited和RUNNING)task
    public boolean existUncompletedScheduled(long taskId) {
        TaskInstanceExample example = new TaskInstanceExample();
        example.createCriteria().andTaskIdEqualTo(taskId).andScheduleTimeIsNotNull().andStatusIn(TaskInstanceStatus.getUncompleted());

        return this.instanceDao.countByExample(example) > 0;
    }

    public boolean exist(long taskId, Date scheduleTime) {
        TaskInstanceExample example = new TaskInstanceExample();
        example.createCriteria().andTaskIdEqualTo(taskId).andScheduleTimeEqualTo(scheduleTime);
        List<TaskInstance> instances = this.instanceDao.selectByExample(example);
        if (instances.size() > 0) {
            return true;
        }
        return false;
    }

    public TaskInstance get(long taskId, long workflowInstanceId) {
        TaskInstanceExample example = new TaskInstanceExample();
        example.createCriteria().andTaskIdEqualTo(taskId).andWorkflowInstanceIdEqualTo(workflowInstanceId);
        List<TaskInstance> instances = this.instanceDao.selectByExample(example);
        if ((instances != null) && !instances.isEmpty()) {
            return instances.get(0);
        } else {
            return null;
        }
    }

    public TaskInstance getLast(Task task, TaskInstance taskInstance) {
        if ((task == null) || (taskInstance == null) || (taskInstance.getScheduleTime() == null)) {
            return null;
        }
        TaskInstanceExample example = new TaskInstanceExample();
        Criteria criteria = example.createCriteria().andTaskIdEqualTo(task.getId()).andWorkflowInstanceIdIsNull().andScheduleTimeIsNotNull();
        if (taskInstance.getId() != null) {
            criteria.andIdLessThan(taskInstance.getId());
        }
        example.setOrderByClause("id DESC");
        RowBounds rowBounds = new RowBounds(0, 1);
        List<TaskInstance> taskInstances = this.instanceDao.selectByExampleWithRowbounds(example, rowBounds);
        if (!taskInstances.isEmpty()) {
            TaskInstance result = taskInstances.get(0);
            if (CrontabUtils.validateLastScheduleTime(task.getCrontab(), result.getScheduleTime(),
                                                      taskInstance.getScheduleTime())) {
                return result;
            }
        }
        return null;

    }

    public List<TaskInstance> listByWorkflowInstanceIdAndUserId(long workflowInstanceId, long userId) {
        TaskInstanceExample example = new TaskInstanceExample();
        queryByUser(example.createCriteria(), workflowInstanceId, userId, null);
        return this.instanceDao.selectByExample(example);
    }

    private void queryByUser(Criteria criteria, long workflowInstanceId, long userId, List<Long> additionTaskIds) {
        List<NameValuePair> tasks = this.taskService.list(userId);

        Set<Long> taskIds = new HashSet<Long>();

        CollectionUtils.collect(tasks, new Transformer() {

            public Object transform(Object input) {
                return ((NameValuePair) input).getValue();
            }
        }, taskIds);

        criteria.andWorkflowInstanceIdEqualTo(workflowInstanceId);

        if (taskIds.isEmpty()) {
            if ((additionTaskIds == null) || additionTaskIds.isEmpty()) {
                // 强制返回空数据
                criteria.andIdIsNull();
            } else {
                criteria.andTaskIdIn(additionTaskIds);
            }
        } else {
            if ((additionTaskIds != null) && !additionTaskIds.isEmpty()) {
                taskIds.addAll(additionTaskIds);
            }
            criteria.andTaskIdIn(new ArrayList<Long>(taskIds));
        }
    }

    public List<TaskInstance> listByWorkflowInstanceIdAndUserId(long workflowInstanceId, long userId, int pageNum,
                                                                Paginator paginator) {
        TaskInstanceExample example = new TaskInstanceExample();

        Criteria criteria = example.createCriteria();
        List<Long> additionTaskIds = new ArrayList<Long>();
        Task rootTask = this.taskService.getRootTask("");
        Task teamRootTask = this.taskService.getRootTask(this.teamService.get(this.userService.get(userId).getTeamId()).getName());
        if ((rootTask != null) && (rootTask.getId() != null)) {
            additionTaskIds.add(rootTask.getId());
        }

        if ((teamRootTask != null) && (teamRootTask.getId() != null)) {
            additionTaskIds.add(teamRootTask.getId());
        }

        queryByUser(criteria, workflowInstanceId, userId, additionTaskIds);

        int count = this.instanceDao.countByExample(example);

        paginator.setItemsPerPage(Constants.PAGE_SIZE);
        paginator.setItems(count);
        paginator.setPage(pageNum);

        int offset = paginator.getBeginIndex() - 1;
        int limit = Constants.PAGE_SIZE;

        RowBounds rowBounds = new RowBounds(offset, limit);

        List<TaskInstance> instances = this.instanceDao.selectByExampleWithRowbounds(example, rowBounds);

        return instances;
    }
}
