package com.yeahmobi.yscheduler.model.service.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.yeahmobi.yscheduler.common.Paginator;
import com.yeahmobi.yscheduler.model.service.WorkflowTaskInstanceService;
import com.yeahmobi.yunit.DbUnitTestExecutionListener;
import com.yeahmobi.yunit.annotation.DatabaseSetup;

/**
 * Ryan Sun
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class WorkflowTaskInstanceServiceImplTest {

    @Autowired
    private WorkflowTaskInstanceService workflowInstanceService;

    @Test
    @DatabaseSetup
    public void testList() {
        Paginator paginator = new Paginator();

        Assert.assertEquals(2, this.workflowInstanceService.list(1, paginator, 1l).size());
    }

}
