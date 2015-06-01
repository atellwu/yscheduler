package com.yeahmobi.yscheduler.model.service.impl;

import java.text.SimpleDateFormat;
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

import com.google.common.collect.Lists;
import com.yeahmobi.yscheduler.common.Paginator;
import com.yeahmobi.yscheduler.model.TeamWorkflowInstanceStatus;
import com.yeahmobi.yscheduler.model.Workflow;
import com.yeahmobi.yscheduler.model.WorkflowInstance;
import com.yeahmobi.yscheduler.model.common.Query;
import com.yeahmobi.yscheduler.model.common.Query.WorkflowScheduleType;
import com.yeahmobi.yscheduler.model.service.TeamWorkflowStatusInstanceService;
import com.yeahmobi.yscheduler.model.service.WorkflowInstanceService;
import com.yeahmobi.yscheduler.model.type.WorkflowInstanceStatus;
import com.yeahmobi.yunit.DbUnitTestExecutionListener;
import com.yeahmobi.yunit.annotation.DatabaseOperation;
import com.yeahmobi.yunit.annotation.DatabaseSetup;
import com.yeahmobi.yunit.annotation.DatabaseTearDown;

/**
 * Ryan Sun
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class WorkflowInstanceServiceImplTest {

    @Autowired
    private WorkflowInstanceService           workflowInstanceService;

    @Autowired
    private TeamWorkflowStatusInstanceService teamWorkflowStatusInstanceService;

    private static final SimpleDateFormat     sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private WorkflowInstance buildInstance() {
        WorkflowInstance instance = new WorkflowInstance();
        Date time = TestUtils.DEFAULT_TIME;

        instance.setWorkflowId(1l);
        instance.setStatus(WorkflowInstanceStatus.INITED);
        instance.setEndTime(time);
        instance.setScheduleTime(time);
        instance.setStartTime(time);
        return instance;
    }

    @Test
    @DatabaseSetup
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL)
    public void testList() {
        Paginator paginator = new Paginator();
        Assert.assertEquals(1, this.workflowInstanceService.list(new Query(), 1, 1, paginator).size());
        Assert.assertEquals(1, this.workflowInstanceService.listAll(1).size());
        Assert.assertEquals(2, this.workflowInstanceService.getAllInits().size());
    }

    @Test
    @DatabaseSetup
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL)
    public void testListByIds() {
        List<Long> ids = Lists.asList(1L, new Long[] { 2L });
        Assert.assertEquals(2, this.workflowInstanceService.list(ids).size());
    }

    @Test
    @DatabaseSetup
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL)
    public void testGetAllRunning() {
        List<WorkflowInstance> commonRunningWorkflowInstances = this.workflowInstanceService.getAllRunning(true);
        Assert.assertEquals(2, commonRunningWorkflowInstances.size());
        Assert.assertEquals(Long.valueOf(3), commonRunningWorkflowInstances.get(0).getId());
        Assert.assertEquals(Long.valueOf(4), commonRunningWorkflowInstances.get(1).getId());

        List<WorkflowInstance> privateRunningWorkflowInstances = this.workflowInstanceService.getAllRunning(false);
        Assert.assertEquals(2, privateRunningWorkflowInstances.size());
        Assert.assertEquals(Long.valueOf(1), privateRunningWorkflowInstances.get(0).getId());
        Assert.assertEquals(Long.valueOf(2), privateRunningWorkflowInstances.get(1).getId());
    }

    @Test
    public void testGetAllRunningNoWorkflowExists() {
        List<WorkflowInstance> commonRunningWorkflowInstances = this.workflowInstanceService.getAllRunning(true);
        Assert.assertEquals(0, commonRunningWorkflowInstances.size());

        List<WorkflowInstance> privateRunningWorkflowInstances = this.workflowInstanceService.getAllRunning(false);
        Assert.assertEquals(0, privateRunningWorkflowInstances.size());
    }

    @Test
    @DatabaseSetup
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL)
    public void testQuery() throws InterruptedException {
        Paginator paginator = new Paginator();

        // 搜索得到0条
        {
            Query query = new Query();
            query.setWorkflowInstanceStatus(WorkflowInstanceStatus.CANCELLED);
            Assert.assertEquals(0, this.workflowInstanceService.list(query, 2, 1, paginator).size());
        }

        // 搜索得到11条
        {
            Query query = new Query();
            query.setWorkflowInstanceStatus(WorkflowInstanceStatus.FAILED);
            query.setWorkflowScheduleType(WorkflowScheduleType.AUTO);
            Assert.assertEquals(10, this.workflowInstanceService.list(query, 21, 1, paginator).size());
            Assert.assertEquals(1, this.workflowInstanceService.list(query, 21, 2, paginator).size());
        }

        // 搜索得到0条
        {
            Query query = new Query();
            query.setWorkflowInstanceStatus(WorkflowInstanceStatus.SUCCESS);
            query.setWorkflowScheduleType(WorkflowScheduleType.MANAUAL);
            Assert.assertEquals(0, this.workflowInstanceService.list(query, 21, 1, paginator).size());
        }

    }

    @Test
    @DatabaseSetup
    public void testGetLast() throws Exception {
        WorkflowInstance workflowInstance1 = this.workflowInstanceService.get(1);
        WorkflowInstance workflowInstance3 = this.workflowInstanceService.get(3);
        WorkflowInstance workflowInstance4 = this.workflowInstanceService.get(4);
        Workflow workflow = new Workflow();
        workflow.setId(1l);
        workflow.setCrontab("*/5 * * * * ");
        Assert.assertEquals(workflowInstance1, this.workflowInstanceService.getLast(workflow, workflowInstance3));
        WorkflowInstance newWorkflowInstance = new WorkflowInstance();
        newWorkflowInstance.setScheduleTime(sdf.parse("2014-11-26 00:20:00"));
        Assert.assertEquals(workflowInstance4, this.workflowInstanceService.getLast(workflow, newWorkflowInstance));
        Assert.assertNull(this.workflowInstanceService.getLast(workflow, workflowInstance1));
        Assert.assertNull(this.workflowInstanceService.getLast(workflow, workflowInstance4));
        Assert.assertNull(this.workflowInstanceService.getLast(null, workflowInstance4));

    }

    @Test
    @DatabaseSetup
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL)
    public void testSave() {
        WorkflowInstance instance = buildInstance();
        this.workflowInstanceService.save(instance);

        Long id = instance.getId();
        Assert.assertNotNull(id);
        assertInstanceEquals(instance, this.workflowInstanceService.get(id));
    }

    @Test
    @DatabaseSetup
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL)
    public void testSaveCommon() {
        WorkflowInstance instance = buildInstance();
        this.workflowInstanceService.save(instance);

        // 验证common
        Paginator paginator = new Paginator();
        List<TeamWorkflowInstanceStatus> list = this.teamWorkflowStatusInstanceService.list(new Query(), 2, 1, 1,
                                                                                            paginator);
        Assert.assertEquals(1, list.size());
    }

    private void assertInstanceEquals(WorkflowInstance expected, WorkflowInstance actual) {
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getScheduleTime(), actual.getScheduleTime());
        Assert.assertEquals(expected.getStatus(), actual.getStatus());
        Assert.assertEquals(expected.getStartTime(), actual.getStartTime());
        Assert.assertEquals(expected.getEndTime(), actual.getEndTime());
        Assert.assertEquals(expected.getWorkflowId(), actual.getWorkflowId());
        TestUtils.generallyEquals(new Date(), actual.getCreateTime());
        TestUtils.generallyEquals(new Date(), actual.getUpdateTime());
    }

    @Test
    @DatabaseSetup
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL)
    public void updateStatus() {
        Long instanceId = 1l;
        WorkflowInstanceStatus status = WorkflowInstanceStatus.CANCELLED;
        this.workflowInstanceService.updateStatus(instanceId, status);
        WorkflowInstance instance = this.workflowInstanceService.get(instanceId);
        Assert.assertEquals(status, instance.getStatus());
        TestUtils.generallyEquals(new Date(), instance.getEndTime());

        status = WorkflowInstanceStatus.RUNNING;
        this.workflowInstanceService.updateStatus(instanceId, status);
        instance = this.workflowInstanceService.get(instanceId);
        Assert.assertEquals(status, instance.getStatus());
        TestUtils.generallyEquals(new Date(), instance.getStartTime());

        status = WorkflowInstanceStatus.SKIPPED;
        this.workflowInstanceService.updateStatus(instanceId, status);
        instance = this.workflowInstanceService.get(instanceId);
        Assert.assertEquals(status, instance.getStatus());
    }

    @Test
    @DatabaseSetup
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL)
    public void testExistUncompleted() {
        Assert.assertTrue(this.workflowInstanceService.existUncompleted(1l));
        Assert.assertFalse(this.workflowInstanceService.existUncompleted(7l));

    }

}
