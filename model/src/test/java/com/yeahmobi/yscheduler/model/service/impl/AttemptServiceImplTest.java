package com.yeahmobi.yscheduler.model.service.impl;

import java.text.ParseException;
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

import com.yeahmobi.yscheduler.common.Paginator;
import com.yeahmobi.yscheduler.model.Attempt;
import com.yeahmobi.yscheduler.model.service.AttemptService;
import com.yeahmobi.yscheduler.model.type.AttemptStatus;
import com.yeahmobi.yunit.DbUnitTestExecutionListener;
import com.yeahmobi.yunit.annotation.DatabaseSetup;

/**
 * @author atell
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class AttemptServiceImplTest {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private AttemptService                attemptService;

    @Test
    @DatabaseSetup
    public void testGet() throws Exception {
        long id = 1;
        Attempt attempt = this.attemptService.get(id);

        assertAttempt(attempt, 1, true);
    }

    @Test
    @DatabaseSetup
    public void testGetLastOne() throws Exception {
        long instanceId = 1;
        Attempt attempt = this.attemptService.getLastOne(instanceId);

        assertAttempt(attempt, 14, true);

        instanceId = -1;
        attempt = this.attemptService.getLastOne(instanceId);
        Assert.assertNull(attempt);
    }

    @Test
    @DatabaseSetup
    public void testList() throws Exception {
        int instanceId = 1;
        // page 1
        int pageNum = 1;
        Paginator paginator = new Paginator();
        List<Attempt> list = this.attemptService.list(instanceId, pageNum, paginator);

        Assert.assertTrue(list.size() == 10);
        for (int i = 0; i < list.size(); i++) {
            Attempt attempt = list.get(i);
            assertAttempt(attempt, i + 1, true);
        }
        Assert.assertEquals(15, paginator.getItems());
        Assert.assertEquals(2, paginator.getPages());
        Assert.assertEquals(1, paginator.getPage());
        // page 2
        pageNum = 2;
        paginator = new Paginator();
        list = this.attemptService.list(instanceId, pageNum, paginator);

        Assert.assertTrue(list.size() == 5);
        for (int i = 0; i < (list.size() - 1); i++) {
            Attempt attempt = list.get(i);
            assertAttempt(attempt, i + 11, true);
        }

        assertAttempt(list.get(4), 15, false);
        Assert.assertEquals(15, paginator.getItems());
        Assert.assertEquals(2, paginator.getPages());
        Assert.assertEquals(2, paginator.getPage());

    }

    @Test
    @DatabaseSetup
    public void testSave() throws Exception {
        Attempt attempt = new Attempt();
        attempt.setAgentId(1L);
        attempt.setDuration(60000L);
        Date endTime = new Date();
        attempt.setEndTime(endTime);
        attempt.setInstanceId(1L);
        attempt.setOutput("test");
        attempt.setReturnValue(1);
        Date startTime = new Date();
        attempt.setStartTime(startTime);
        attempt.setStatus(AttemptStatus.RUNNING);
        attempt.setTaskId(1L);
        attempt.setTransactionId(1L);
        attempt.setActive(true);
        Date updateTime = new Date();
        attempt.setUpdateTime(updateTime);

        this.attemptService.save(attempt);
        Long id = attempt.getId();

        Assert.assertNotNull(id);
        Attempt actual = this.attemptService.get(id);
        assertAttempt(actual, id, attempt.getAgentId(), new Date(), attempt.getDuration(), attempt.getEndTime(),
                      attempt.getInstanceId(), attempt.getOutput(), attempt.getReturnValue(), attempt.getStartTime(),
                      attempt.getStatus(), attempt.getTaskId(), attempt.getTransactionId(), attempt.getUpdateTime(),
                      true);

    }

    @Test
    @DatabaseSetup
    public void testGetAllUncompleteds() throws Exception {
        List<Attempt> list = this.attemptService.getAllUncompleteds();

        Assert.assertTrue(list.size() == 14);
        for (int i = 0; i < list.size(); i++) {
            Attempt attempt = list.get(i);
            assertAttempt(attempt, i + 1, 1, "2014-11-26 17:00:00", 60000, "2014-11-26 17:39:00", 1, "test " + (i + 1),
                          0, "2014-11-26 17:38:00", AttemptStatus.RUNNING, 1, 1, "2014-11-26 17:38:00", true);
        }
    }

    @Test
    @DatabaseSetup
    public void testCountActive() throws Exception {
        long instanceId = 1;
        int count = this.attemptService.countActive(instanceId);

        Assert.assertEquals(14, count);
    }

    @Test
    @DatabaseSetup
    public void testUpdate() throws Exception {
        Attempt attempt = new Attempt();
        attempt.setId(1L);
        attempt.setCreateTime(new Date());
        attempt.setAgentId(1L);
        attempt.setDuration(60000L);
        Date endTime = new Date();
        attempt.setEndTime(endTime);
        attempt.setInstanceId(1L);
        attempt.setOutput("test");
        attempt.setReturnValue(1);
        Date startTime = new Date();
        attempt.setStartTime(startTime);
        attempt.setStatus(AttemptStatus.FAILED);
        attempt.setTaskId(1L);
        attempt.setTransactionId(1L);
        attempt.setActive(false);
        Date updateTime = new Date();
        attempt.setUpdateTime(updateTime);

        this.attemptService.update(attempt);

        Long id = attempt.getId();

        Attempt actual = this.attemptService.get(id);
        assertAttempt(actual, id, attempt.getAgentId(), attempt.getCreateTime(), attempt.getDuration(),
                      attempt.getEndTime(), attempt.getInstanceId(), attempt.getOutput(), attempt.getReturnValue(),
                      attempt.getStartTime(), attempt.getStatus(), attempt.getTaskId(), attempt.getTransactionId(),
                      attempt.getUpdateTime(), false);

    }

    @Test
    @DatabaseSetup
    public void testArchiveExistsAttempts() throws Exception {
        long instanceId = 1;
        this.attemptService.archiveExistsAttempts(instanceId);

        // page 1
        int pageNum = 1;
        Paginator paginator = new Paginator();
        List<Attempt> list = this.attemptService.list(instanceId, pageNum, paginator);

        Date updateTime = new Date();

        Assert.assertTrue(list.size() == 10);
        for (int i = 0; i < list.size(); i++) {
            Attempt attempt = list.get(i);
            assertAttempt(attempt, i + 1, 1, "2014-11-26 17:00:00", 60000, "2014-11-26 17:39:00", 1, "test " + (i + 1),
                          0, "2014-11-26 17:38:00", AttemptStatus.SUCCESS, 1, 1, sdf.format(updateTime), false);
        }
        Assert.assertEquals(15, paginator.getItems());
        Assert.assertEquals(2, paginator.getPages());
        Assert.assertEquals(1, paginator.getPage());
        // page 2
        pageNum = 2;
        paginator = new Paginator();
        list = this.attemptService.list(instanceId, pageNum, paginator);

        Assert.assertTrue(list.size() == 5);
        for (int i = 0; i < list.size(); i++) {
            Attempt attempt = list.get(i);
            assertAttempt(attempt, i + 11, 1, "2014-11-26 17:00:00", 60000, "2014-11-26 17:39:00", 1, "test "
                                                                                                      + (i + 11), 0,
                          "2014-11-26 17:38:00", AttemptStatus.SUCCESS, 1, 1, sdf.format(updateTime), false);
        }

        Assert.assertEquals(15, paginator.getItems());
        Assert.assertEquals(2, paginator.getPages());
        Assert.assertEquals(2, paginator.getPage());
    }

    @Test
    public void testGetOutputFileName() throws Exception {
        Attempt attempt = new Attempt();
        attempt.setId(10L);
        Assert.assertEquals("10.log", this.attemptService.getOutputFileName(attempt));
    }

    private void assertAttempt(Attempt attempt, long id, boolean active) throws ParseException {
        assertAttempt(attempt, id, 1, "2014-11-26 17:00:00", 60000, "2014-11-26 17:39:00", 1, "test " + id, 0,
                      "2014-11-26 17:38:00", AttemptStatus.SUCCESS, 1, 1, "2014-11-26 17:38:00", active);
    }

    private void assertAttempt(Attempt attempt, long id, long agentId, String createTime, long duration,
                               String endTime, long instanceId, String output, int returnValue, String startTime,
                               AttemptStatus status, long taskId, long transactionId, String updateTime, boolean active)
                                                                                                                        throws ParseException {
        assertAttempt(attempt, id, agentId, sdf.parse(createTime), duration, sdf.parse(endTime), instanceId, output,
                      returnValue, sdf.parse(startTime), status, taskId, transactionId, sdf.parse(updateTime), active);
    }

    private void assertAttempt(Attempt attempt, long id, long agentId, Date createTime, long duration, Date endTime,
                               long instanceId, String output, int returnValue, Date startTime, AttemptStatus status,
                               long taskId, long transactionId, Date updateTime, boolean active) throws ParseException {
        Assert.assertEquals(Long.valueOf(id), attempt.getId());
        Assert.assertEquals(Long.valueOf(agentId), attempt.getAgentId());
        Assert.assertTrue(TestUtils.generallyEquals(createTime, attempt.getCreateTime()));
        Assert.assertEquals(Long.valueOf(duration), attempt.getDuration());
        Assert.assertTrue(TestUtils.generallyEquals(endTime, attempt.getEndTime()));
        Assert.assertEquals(Long.valueOf(instanceId), attempt.getInstanceId());
        Assert.assertEquals(output, attempt.getOutput());
        Assert.assertEquals(Integer.valueOf(returnValue), attempt.getReturnValue());
        Assert.assertTrue(TestUtils.generallyEquals(startTime, attempt.getStartTime()));
        Assert.assertEquals(status, attempt.getStatus());
        Assert.assertEquals(Long.valueOf(taskId), attempt.getTaskId());
        Assert.assertEquals(Long.valueOf(transactionId), attempt.getTransactionId());
        Assert.assertTrue(TestUtils.generallyEquals(updateTime, attempt.getUpdateTime()));
        Assert.assertEquals(Boolean.valueOf(active), attempt.getActive());
    }
}
