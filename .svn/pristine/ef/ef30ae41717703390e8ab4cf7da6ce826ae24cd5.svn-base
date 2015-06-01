package com.yeahmobi.yscheduler.model.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeahmobi.yscheduler.model.Task;
import com.yeahmobi.yscheduler.model.Workflow;
import com.yeahmobi.yscheduler.model.WorkflowDetail;
import com.yeahmobi.yscheduler.model.WorkflowDetailExample;
import com.yeahmobi.yscheduler.model.WorkflowTaskDependency;
import com.yeahmobi.yscheduler.model.common.NameValuePair;
import com.yeahmobi.yscheduler.model.dao.WorkflowDetailDao;
import com.yeahmobi.yscheduler.model.service.TaskService;
import com.yeahmobi.yscheduler.model.service.UserService;
import com.yeahmobi.yscheduler.model.service.WorkflowDetailService;
import com.yeahmobi.yscheduler.model.service.WorkflowService;
import com.yeahmobi.yscheduler.model.service.WorkflowTaskDependencyService;
import com.yeahmobi.yscheduler.model.type.DependingStatus;

@Service
public class WorkflowDetailServiceImpl implements WorkflowDetailService {

    @Autowired
    private WorkflowDetailDao             workflowDetailDao;

    @Autowired
    private WorkflowTaskDependencyService workflowTaskDependencyService;

    @Autowired
    private TaskService                   taskService;

    @Autowired
    private WorkflowService               workflowService;

    @Autowired
    private UserService                   userService;

    public List<WorkflowDetail> list(long workflowId) {
        WorkflowDetailExample example = new WorkflowDetailExample();
        example.createCriteria().andWorkflowIdEqualTo(workflowId);
        return this.workflowDetailDao.selectByExample(example);
    }

    public List<Long> listDependencyTaskIds(long workflowId, long taskId) {
        List<Long> result = new ArrayList<Long>();
        WorkflowDetailExample example = new WorkflowDetailExample();
        example.createCriteria().andWorkflowIdEqualTo(workflowId).andTaskIdEqualTo(taskId);
        List<WorkflowDetail> workflowDetails = this.workflowDetailDao.selectByExample(example);
        if (workflowDetails.size() != 0) {
            long workflowDetailId = workflowDetails.get(0).getId();
            List<WorkflowTaskDependency> dependencies = this.workflowTaskDependencyService.listByWorkflowDetailId(workflowDetailId);

            for (WorkflowTaskDependency dependency : dependencies) {
                result.add(dependency.getDependencyTaskId());
            }
        }
        return result;
    }

    public WorkflowDetail get(long workflowId, long taskId) {
        WorkflowDetailExample example = new WorkflowDetailExample();
        example.createCriteria().andWorkflowIdEqualTo(workflowId).andTaskIdEqualTo(taskId);
        List<WorkflowDetail> workflowDetails = this.workflowDetailDao.selectByExample(example);

        return (workflowDetails != null) && !workflowDetails.isEmpty() ? workflowDetails.get(0) : null;
    }

    public void addTeamRootTask(Task task) {
        Task root = this.taskService.addRootTaskIfAbsent("");
        List<Workflow> workflows = this.workflowService.listAllCommon();
        for (Workflow workflow : workflows) {
            WorkflowDetail detail = new WorkflowDetail();
            detail.setLastStatusDependency(DependingStatus.NONE);
            detail.setWorkflowId(workflow.getId());
            detail.setTaskId(task.getId());
            detail.setDelay(0);
            detail.setRetryTimes(0);
            detail.setTimeout(5);
            List<Long> dependencyTasks = new ArrayList<Long>();
            dependencyTasks.add(root.getId());
            add(detail);
            this.workflowTaskDependencyService.addDependencyTasks(detail.getId(), dependencyTasks);
        }
    }

    public void add(WorkflowDetail detail) {
        Date time = new Date();
        detail.setCreateTime(time);
        detail.setUpdateTime(time);
        this.workflowDetailDao.insertSelective(detail);
    }

    /**
     * 列出公共工作流中，某一个用户所在组的所有WorkflowDetail
     */
    public List<WorkflowDetail> list(long workflowId, long userId) {
        List<NameValuePair> pairs = this.taskService.list(userId);
        WorkflowDetailExample example = new WorkflowDetailExample();
        List<Long> taskIds = new ArrayList<Long>();
        for (NameValuePair pair : pairs) {
            taskIds.add(pair.getValue());
        }
        example.createCriteria().andTaskIdIn(taskIds).andWorkflowIdEqualTo(workflowId);
        List<WorkflowDetail> details = this.workflowDetailDao.selectByExample(example);
        return details;
    }

    public int getTeamDelay(long workflowId, long userId) {
        Task teamRoot = this.taskService.getRootTask(this.userService.getTeamName(userId));
        WorkflowDetail detail = get(workflowId, teamRoot.getId());
        return detail.getDelay() == null ? 0 : detail.getDelay();
    }

    public void save(long workflowId, List<WorkflowDetail> workflowDetails, List<List<Long>> dependencyList) {

        WorkflowDetailExample example = new WorkflowDetailExample();
        example.createCriteria().andWorkflowIdEqualTo(workflowId);
        List<WorkflowDetail> oldWorkflowDetails = this.workflowDetailDao.selectByExample(example);

        for (WorkflowDetail detail : oldWorkflowDetails) {
            this.workflowTaskDependencyService.deleteByWorkflowDetailId(detail.getId());
        }

        this.workflowDetailDao.deleteByExample(example);
        for (int i = 0; i < workflowDetails.size(); i++) {
            WorkflowDetail detail = workflowDetails.get(i);
            List<Long> dependencyTasks = dependencyList.get(i);
            detail.setWorkflowId(workflowId);
            Date time = new Date();
            detail.setCreateTime(time);
            detail.setUpdateTime(time);

            this.workflowDetailDao.insertSelective(detail);
            this.workflowTaskDependencyService.addDependencyTasks(detail.getId(), dependencyTasks);
        }
    }

    public void save(long workflowId, List<WorkflowDetail> workflowDetails, List<List<Long>> dependencyList, long userId) {
        List<NameValuePair> pairs = this.taskService.list(userId);
        WorkflowDetailExample example = new WorkflowDetailExample();
        List<Long> taskIds = new ArrayList<Long>();
        for (NameValuePair pair : pairs) {
            taskIds.add(pair.getValue());
        }
        example.createCriteria().andTaskIdIn(taskIds).andWorkflowIdEqualTo(workflowId);
        List<WorkflowDetail> details = this.workflowDetailDao.selectByExample(example);
        for (WorkflowDetail detail : details) {
            this.workflowTaskDependencyService.deleteByWorkflowDetailId(detail.getId());
        }
        this.workflowDetailDao.deleteByExample(example);
        for (int i = 0; i < workflowDetails.size(); i++) {
            WorkflowDetail detail = workflowDetails.get(i);
            List<Long> dependencyTasks = dependencyList.get(i);
            detail.setWorkflowId(workflowId);
            Date time = new Date();
            detail.setCreateTime(time);
            detail.setUpdateTime(time);

            this.workflowDetailDao.insertSelective(detail);
            this.workflowTaskDependencyService.addDependencyTasks(detail.getId(), dependencyTasks);
        }

    }

    public void updateDelayTime(long workflowId, long userId, int delay) {
        Task teamRoot = this.taskService.getRootTask(this.userService.getTeamName(userId));
        WorkflowDetail detail = get(workflowId, teamRoot.getId());
        detail.setDelay(delay);
        this.workflowDetailDao.updateByPrimaryKey(detail);
    }

    public List<WorkflowDetail> listAllExceptRootTask(long workflowId) {
        WorkflowDetailExample example = new WorkflowDetailExample();
        List<Task> tasks = this.taskService.listRootTasks();
        @SuppressWarnings("unchecked")
        List<Long> taskids = new ArrayList<Long>(CollectionUtils.collect(tasks, new Transformer() {

            public Object transform(Object input) {
                return ((Task) input).getId();
            }
        }));
        example.createCriteria().andWorkflowIdEqualTo(workflowId).andTaskIdNotIn(taskids);

        return this.workflowDetailDao.selectByExample(example);
    }
}
