package com.yeahmobi.yscheduler.model.service.impl;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.yeahmobi.yscheduler.common.Paginator;
import com.yeahmobi.yscheduler.model.Task;
import com.yeahmobi.yscheduler.model.Workflow;
import com.yeahmobi.yscheduler.model.WorkflowDetail;
import com.yeahmobi.yscheduler.model.WorkflowTaskDependency;
import com.yeahmobi.yscheduler.model.common.Query;
import com.yeahmobi.yscheduler.model.service.TaskService;
import com.yeahmobi.yscheduler.model.service.WorkflowDetailService;
import com.yeahmobi.yscheduler.model.service.WorkflowService;
import com.yeahmobi.yscheduler.model.service.WorkflowTaskDependencyService;
import com.yeahmobi.yscheduler.model.type.DependingStatus;
import com.yeahmobi.yscheduler.model.type.WorkflowStatus;
import com.yeahmobi.yunit.DbUnitTestExecutionListener;
import com.yeahmobi.yunit.annotation.DatabaseSetup;

/**
 * Ryan Sun
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class WorkflowServiceImplTest {

    @Autowired
    private WorkflowService               workflowService;

    @Autowired
    private TaskService                   taskService;

    @Autowired
    private WorkflowDetailService         workflowDetailService;

    @Autowired
    private WorkflowTaskDependencyService workflowDependencyService;

    private Workflow buildWorkflow() {
        Workflow workflow = new Workflow();
        workflow.setName("test");
        workflow.setLastScheduleTime(TestUtils.DEFAULT_TIME);
        workflow.setCrontab("* * * * *");
        workflow.setDescription("");
        workflow.setOwner(1l);
        workflow.setStatus(WorkflowStatus.OPEN);
        workflow.setTimeout(1);
        return workflow;
    }

    @Test
    @DatabaseSetup
    public void testListAllPrivate() throws InterruptedException {
        Assert.assertEquals(13, this.workflowService.listAllPrivate().size());
    }

    @Test
    @DatabaseSetup
    public void testListAllCommon() throws InterruptedException {
        Assert.assertEquals(1, this.workflowService.listAllCommon().size());
    }

    @Test
    @DatabaseSetup
    public void testCreatePrivate() throws InterruptedException {
        Workflow workflow = buildWorkflow();
        workflow.setCommon(false);
        workflow.setCanSkip(false);
        workflow.setLastStatusDependency(DependingStatus.COMPLETED);
        this.workflowService.createOrUpdate(workflow);
        Long id = workflow.getId();
        Assert.assertNotNull(id);
        Workflow actual = this.workflowService.get(id);
        assertWorkflow(workflow, actual, new Date(), TestUtils.DEFAULT_TIME);
        Task task = this.taskService.getRootTask("platform");
        Assert.assertNull(task);
    }

    @Test
    @DatabaseSetup
    public void testCreateCommonWithoutTeamRootTask() throws InterruptedException {
        Workflow workflow = buildWorkflow();
        workflow.setCommon(true);
        workflow.setCanSkip(false);
        workflow.setLastStatusDependency(DependingStatus.COMPLETED);
        this.workflowService.createOrUpdate(workflow);
        Long id = workflow.getId();
        Assert.assertNotNull(id);
        Workflow actual = this.workflowService.get(id);
        assertWorkflow(workflow, actual, new Date(), TestUtils.DEFAULT_TIME);
        Task task = this.taskService.getRootTask("platform");
        Assert.assertNull(task);
    }

    @Test
    @DatabaseSetup
    public void testCreateCommon() throws InterruptedException {
        Workflow workflow = buildWorkflow();
        workflow.setCommon(true);
        this.workflowService.createOrUpdate(workflow);
        workflow.setCanSkip(false);
        workflow.setLastStatusDependency(DependingStatus.NONE);
        Long id = workflow.getId();
        Assert.assertNotNull(id);
        Workflow actual = this.workflowService.get(id);
        assertWorkflow(workflow, actual, new Date(), TestUtils.DEFAULT_TIME);
        Task task = this.taskService.getRootTask("platform");
        Assert.assertNotNull(task);
        Assert.assertNotNull(task.getId());
        WorkflowDetail detail = this.workflowDetailService.get(id, task.getId());
        Assert.assertNotNull(detail);
        Assert.assertNotNull(detail.getId());
        Assert.assertEquals(task.getId(), detail.getTaskId());

        List<WorkflowTaskDependency> dependencies = this.workflowDependencyService.listByWorkflowDetailId(detail.getId());
        Assert.assertEquals(1, dependencies.size());
        Assert.assertEquals(this.taskService.addRootTaskIfAbsent("").getId(), dependencies.get(0).getDependencyTaskId());
    }

    @Test
    @DatabaseSetup
    public void testUpdate() throws InterruptedException {
        Workflow workflow = buildWorkflow();
        workflow.setStatus(WorkflowStatus.OPEN);
        workflow.setLastStatusDependency(DependingStatus.COMPLETED);
        workflow.setCanSkip(false);
        workflow.setId(1l);
        this.workflowService.createOrUpdate(workflow);
        Workflow actual = this.workflowService.get(1l);
        assertWorkflow(workflow, actual, TestUtils.DEFAULT_TIME, new Date());
    }

    @Test
    @DatabaseSetup
    public void testListPrivate() throws InterruptedException {
        Paginator paginator = new Paginator();
        Assert.assertEquals(1, this.workflowService.list(new Query(), 2, paginator, 1, false).size());
        Assert.assertEquals(1, this.workflowService.listAll(WorkflowStatus.PAUSED).size());
    }

    @Test
    @DatabaseSetup
    public void testListCommon() throws InterruptedException {
        Paginator paginator = new Paginator();
        Assert.assertEquals(1, this.workflowService.list(new Query(), 1, paginator, 1, true).size());
        Assert.assertEquals(12, this.workflowService.listAll(WorkflowStatus.OPEN).size());
    }

    @Test
    @DatabaseSetup
    public void testQuery() throws InterruptedException {
        Paginator paginator = new Paginator();

        // 搜索得到11条
        {
            Query query = new Query();
            query.setName("test");
            Assert.assertEquals(10, this.workflowService.list(query, 1, paginator, 1, false).size());
            Assert.assertEquals(1, this.workflowService.list(query, 2, paginator, 1, false).size());
        }

        // 搜索得到1条
        {
            Query query = new Query();
            query.setWorkflowStatus(WorkflowStatus.PAUSED);
            Assert.assertEquals(1, this.workflowService.list(query, 1, paginator, 1, false).size());
        }

        // 搜索得到1条
        {
            Query query = new Query();
            query.setWorkflowStatus(WorkflowStatus.PAUSED);
            query.setName("test");
            query.setOwner(1L);
            Assert.assertEquals(1, this.workflowService.list(query, 1, paginator, 1, false).size());
        }

        // 权限过滤之后，无数据
        {
            Query query = new Query();
            query.setOwner(2L);
            Assert.assertEquals(0, this.workflowService.list(query, 1, paginator, 1, false).size());
        }

    }

    @Test
    @DatabaseSetup
    public void testCanModify() {
        Assert.assertTrue(this.workflowService.canModify(1l, 1l));
        Assert.assertTrue(this.workflowService.canModify(2l, 1l));
        Assert.assertTrue(this.workflowService.canModify(1l, 2l));
        Assert.assertFalse(this.workflowService.canModify(13l, 1l));
        Assert.assertFalse(this.workflowService.canModify(100l, 1l));

    }

    @Test
    @DatabaseSetup
    public void testNameExist() {
        Assert.assertTrue(this.workflowService.nameExist("test1"));
        Assert.assertTrue(this.workflowService.nameExist("test2"));
        Assert.assertFalse(this.workflowService.nameExist("testx"));

    }

    @Test
    @DatabaseSetup
    public void testUpdateScheduleTime() {
        Date time = new Date();
        this.workflowService.updateScheduleTime(1, time);
        Assert.assertEquals(time, this.workflowService.get(1).getLastScheduleTime());
    }

    private void assertWorkflow(Workflow expected, Workflow actual, Date createTime, Date scheduleTime) {
        Assert.assertEquals(expected.getId(), actual.getId());
        TestUtils.generallyEquals(createTime, actual.getCreateTime());
        TestUtils.generallyEquals(new Date(), actual.getUpdateTime());

        Assert.assertEquals(expected.getCrontab(), actual.getCrontab());
        Assert.assertEquals(expected.getDescription(), actual.getDescription());
        if (scheduleTime == null) {
            Assert.assertNull(actual.getLastScheduleTime());
        } else {
            TestUtils.generallyEquals(scheduleTime, actual.getLastScheduleTime());
        }
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getOwner(), actual.getOwner());
        Assert.assertEquals(expected.getStatus(), actual.getStatus());
        Assert.assertEquals(expected.getTimeout(), actual.getTimeout());
        Assert.assertEquals(expected.getLastScheduleTime(), actual.getLastScheduleTime());
    }
}
