package com.yeahmobi.yscheduler.model.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.yeahmobi.yscheduler.model.service.ScheduleProgressService;
import com.yeahmobi.yunit.DbUnitTestExecutionListener;
import com.yeahmobi.yunit.annotation.DatabaseSetup;

/**
 * @author atell
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class ScheduleProgressServiceImplTest {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private ScheduleProgressService       scheduleProgressService;

    @Test
    @DatabaseSetup
    public void testGet() throws Exception {
        Long currentScheduleTime = this.scheduleProgressService.getCurrentScheduleTime();

        Assert.assertEquals(new Date(currentScheduleTime), sdf.parse("2014-11-26 17:00:00"));
    }

    @Test
    @DatabaseSetup
    public void testGetScheduleProgressNull() throws Exception {
        Long currentScheduleTime = this.scheduleProgressService.getCurrentScheduleTime();

        Assert.assertNull(currentScheduleTime);
    }

    @Test
    @DatabaseSetup
    public void testSave() throws Exception {
        Date date = new Date();
        Long currentScheduleTime = date.getTime();
        this.scheduleProgressService.saveCurrentScheduleTime(currentScheduleTime);

        Long actual = this.scheduleProgressService.getCurrentScheduleTime();
        Assert.assertEquals(currentScheduleTime, actual);
    }

}
