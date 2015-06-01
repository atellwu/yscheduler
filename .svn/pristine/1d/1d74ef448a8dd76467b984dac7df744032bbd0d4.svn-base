package com.yeahmobi.yscheduler.model.service.impl;

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
import com.yeahmobi.yscheduler.model.TeamWorkflowInstanceStatus;
import com.yeahmobi.yscheduler.model.common.Query;
import com.yeahmobi.yscheduler.model.service.TeamWorkflowStatusInstanceService;
import com.yeahmobi.yscheduler.model.type.WorkflowInstanceStatus;
import com.yeahmobi.yunit.DbUnitTestExecutionListener;
import com.yeahmobi.yunit.annotation.DatabaseSetup;

/**
 * atell.wu
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class TeamWorkflowInstanceStatusServiceImplTest {

    private static final long                 WORKFLOW_ID      = 1L;
    private static final int                  PLATFORM_USER_ID = 2;
    private static final long                 PLATFORM_TEAM_ID = 2L;
    @Autowired
    private TeamWorkflowStatusInstanceService workflowInstanceService;

    private TeamWorkflowInstanceStatus buildInstance() {
        TeamWorkflowInstanceStatus instance = new TeamWorkflowInstanceStatus();

        instance.setTeamId(PLATFORM_TEAM_ID);
        instance.setWorkflowInstanceId(200L);
        instance.setWorkflowId(WORKFLOW_ID);
        instance.setStatus(WorkflowInstanceStatus.RUNNING);
        return instance;
    }

    @Test
    @DatabaseSetup
    public void testList() {
        Paginator paginator = new Paginator();

        Query query = new Query();
        query.setWorkflowInstanceStatus(WorkflowInstanceStatus.RUNNING);
        long userId = PLATFORM_USER_ID;
        long workflowId = WORKFLOW_ID;
        List<TeamWorkflowInstanceStatus> list = this.workflowInstanceService.list(query, userId, workflowId, 1,
                                                                                  paginator);
        Assert.assertEquals(10, list.size());
        list = this.workflowInstanceService.list(query, userId, workflowId, 2, paginator);
        Assert.assertEquals(1, list.size());
    }

    @Test
    @DatabaseSetup
    public void testListWithQuery() {
        Paginator paginator = new Paginator();

        Query query = new Query();
        query.setWorkflowInstanceStatus(WorkflowInstanceStatus.CANCELLED);
        long userId = PLATFORM_USER_ID;
        long workflowId = WORKFLOW_ID;
        List<TeamWorkflowInstanceStatus> list = this.workflowInstanceService.list(query, userId, workflowId, 1,
                                                                                  paginator);
        Assert.assertEquals(0, list.size());
    }

    @Test
    @DatabaseSetup
    public void testSave() {
        TeamWorkflowInstanceStatus instance = buildInstance();
        this.workflowInstanceService.save(instance);

        Long id = instance.getId();
        Assert.assertNotNull(id);

        {
            Paginator paginator = new Paginator();

            Query query = new Query();
            query.setWorkflowInstanceStatus(WorkflowInstanceStatus.RUNNING);
            long userId = PLATFORM_USER_ID;
            long workflowId = WORKFLOW_ID;
            List<TeamWorkflowInstanceStatus> list = this.workflowInstanceService.list(query, userId, workflowId, 1,
                                                                                      paginator);
            Assert.assertEquals(10, list.size());
            list = this.workflowInstanceService.list(query, userId, workflowId, 2, paginator);
            Assert.assertEquals(2, list.size());
        }
    }

    @Test
    @DatabaseSetup
    public void testUpsteaStatus() {
        WorkflowInstanceStatus status = WorkflowInstanceStatus.CANCELLED;
        this.workflowInstanceService.updateStatus(PLATFORM_TEAM_ID, WORKFLOW_ID, status);

        {
            Paginator paginator = new Paginator();

            Query query = new Query();
            query.setWorkflowInstanceStatus(WorkflowInstanceStatus.CANCELLED);
            long userId = PLATFORM_USER_ID;
            long workflowId = WORKFLOW_ID;
            List<TeamWorkflowInstanceStatus> list = this.workflowInstanceService.list(query, userId, workflowId, 1,
                                                                                      paginator);
            Assert.assertEquals(1, list.size());
        }
    }

}
