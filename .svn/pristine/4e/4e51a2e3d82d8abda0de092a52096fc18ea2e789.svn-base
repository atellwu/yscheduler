package com.yeahmobi.yscheduler.model.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.yeahmobi.yscheduler.model.service.TaskAuthorityService;
import com.yeahmobi.yunit.DbUnitTestExecutionListener;
import com.yeahmobi.yunit.annotation.DatabaseSetup;

/**
 * Leo Liang
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class TaskAuthorityServiceImplTest {

    @Autowired
    private TaskAuthorityService taskAuthorityService;

    @Test
    @DatabaseSetup
    public void testListReadonlyTaskIds() throws Exception {
        List<Long> readOnlyTasks = this.taskAuthorityService.listReadonlyTaskIds(1);

        Assert.assertEquals(2, readOnlyTasks.size());
        Assert.assertEquals(Long.valueOf(1), readOnlyTasks.get(0));
        Assert.assertEquals(Long.valueOf(2), readOnlyTasks.get(1));
    }

    @Test
    @DatabaseSetup
    public void testListReadonlyUser() throws Exception {
        List<Long> readonlyUsers = this.taskAuthorityService.listReadonlyUser(5);

        Assert.assertEquals(2, readonlyUsers.size());
        Assert.assertEquals(Long.valueOf(2), readonlyUsers.get(0));
        Assert.assertEquals(Long.valueOf(3), readonlyUsers.get(1));
    }

    @Test
    @DatabaseSetup
    public void testListWritableTaskIds() throws Exception {
        List<Long> writableTasks = this.taskAuthorityService.listWritableTaskIds(1);

        Assert.assertEquals(2, writableTasks.size());
        Assert.assertEquals(Long.valueOf(3), writableTasks.get(0));
        Assert.assertEquals(Long.valueOf(4), writableTasks.get(1));
    }

    @Test
    @DatabaseSetup
    public void testListWritableUser() throws Exception {
        List<Long> writableUsers = this.taskAuthorityService.listWritableUser(7);

        Assert.assertEquals(2, writableUsers.size());
        Assert.assertEquals(Long.valueOf(4), writableUsers.get(0));
        Assert.assertEquals(Long.valueOf(5), writableUsers.get(1));
    }

    @Test
    @DatabaseSetup
    public void testListFollowUser() throws Exception {
        List<Long> followUsers = this.taskAuthorityService.listFollowUser(7);

        Assert.assertEquals(2, followUsers.size());
        Assert.assertEquals(Long.valueOf(4), followUsers.get(0));
        Assert.assertEquals(Long.valueOf(5), followUsers.get(1));
    }

    @Test
    @DatabaseSetup
    public void testAdd() throws Exception {
        this.taskAuthorityService.add(Arrays.asList(new Long[] { 1000L, 1001L }),
                                      Arrays.asList(new Long[] { 2000L, 2001L }),
                                      Arrays.asList(new Long[] { 3000L, 3001L }), 100L);

        List<Long> readonlyUsers = this.taskAuthorityService.listReadonlyUser(100);

        Collections.sort(readonlyUsers);
        Assert.assertEquals(2, readonlyUsers.size());
        Assert.assertEquals(Long.valueOf(1000), readonlyUsers.get(0));
        Assert.assertEquals(Long.valueOf(1001), readonlyUsers.get(1));

        List<Long> writableUsers = this.taskAuthorityService.listWritableUser(100);

        Collections.sort(writableUsers);
        Assert.assertEquals(2, writableUsers.size());
        Assert.assertEquals(Long.valueOf(2000), writableUsers.get(0));
        Assert.assertEquals(Long.valueOf(2001), writableUsers.get(1));

        List<Long> followUsers = this.taskAuthorityService.listFollowUser(100);

        Collections.sort(followUsers);
        Assert.assertEquals(2, followUsers.size());
        Assert.assertEquals(Long.valueOf(3000), followUsers.get(0));
        Assert.assertEquals(Long.valueOf(3001), followUsers.get(1));
    }

    @Test
    @DatabaseSetup
    public void testDeleteByUser() throws Exception {
        this.taskAuthorityService.deleteByUser(6L);

        Assert.assertEquals(0, this.taskAuthorityService.listReadonlyTaskIds(6L).size());
        Assert.assertEquals(0, this.taskAuthorityService.listWritableTaskIds(6L).size());
    }

    @Test
    @DatabaseSetup
    public void testUpdate() throws Exception {
        this.taskAuthorityService.update(Arrays.asList(new Long[] { 1000L, 1001L }),
                                         Arrays.asList(new Long[] { 2000L, 2001L }),
                                         Arrays.asList(new Long[] { 3000L, 3001L }), 6L);

        List<Long> readonlyUsers = this.taskAuthorityService.listReadonlyUser(6);

        Collections.sort(readonlyUsers);
        Assert.assertEquals(2, readonlyUsers.size());
        Assert.assertEquals(Long.valueOf(1000), readonlyUsers.get(0));
        Assert.assertEquals(Long.valueOf(1001), readonlyUsers.get(1));

        List<Long> writableUsers = this.taskAuthorityService.listWritableUser(6);

        Collections.sort(writableUsers);
        Assert.assertEquals(2, writableUsers.size());
        Assert.assertEquals(Long.valueOf(2000), writableUsers.get(0));
        Assert.assertEquals(Long.valueOf(2001), writableUsers.get(1));

        List<Long> followUsers = this.taskAuthorityService.listFollowUser(6);

        Collections.sort(followUsers);
        Assert.assertEquals(2, followUsers.size());
        Assert.assertEquals(Long.valueOf(3000), followUsers.get(0));
        Assert.assertEquals(Long.valueOf(3001), followUsers.get(1));
    }

}
