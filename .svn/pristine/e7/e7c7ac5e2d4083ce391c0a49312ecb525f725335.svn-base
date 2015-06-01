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
import com.yeahmobi.yscheduler.model.Task;
import com.yeahmobi.yscheduler.model.Team;
import com.yeahmobi.yscheduler.model.Workflow;
import com.yeahmobi.yscheduler.model.WorkflowDetail;
import com.yeahmobi.yscheduler.model.WorkflowTaskDependency;
import com.yeahmobi.yscheduler.model.service.TaskService;
import com.yeahmobi.yscheduler.model.service.TeamService;
import com.yeahmobi.yscheduler.model.service.WorkflowDetailService;
import com.yeahmobi.yscheduler.model.service.WorkflowService;
import com.yeahmobi.yscheduler.model.service.WorkflowTaskDependencyService;
import com.yeahmobi.yscheduler.model.type.DependingStatus;
import com.yeahmobi.yscheduler.model.type.WorkflowStatus;
import com.yeahmobi.yunit.DbUnitTestExecutionListener;
import com.yeahmobi.yunit.annotation.DatabaseSetup;

/**
 * @author Leo Liang
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class TeamServiceImplTest {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private TeamService                   teamService;

    @Autowired
    private TaskService                   taskService;

    @Autowired
    private WorkflowService               workflowService;

    @Autowired
    private WorkflowDetailService         workflowDetailService;

    @Autowired
    private WorkflowTaskDependencyService workflowDependencyService;

    @Test
    @DatabaseSetup
    public void testGet() throws Exception {
        long id = 1;
        Team user = this.teamService.get(id);

        assertTeam(user, 1);
    }

    @Test
    @DatabaseSetup
    public void testListByPage() throws Exception {
        // page 1
        int pageNum = 1;
        Paginator paginator = new Paginator();
        List<Team> list = this.teamService.list(pageNum, paginator);

        Assert.assertTrue(list.size() == 10);
        for (int i = 0; i < list.size(); i++) {
            Team team = list.get(i);
            assertTeam(team, i + 1);
        }
        Assert.assertEquals(11, paginator.getItems());
        Assert.assertEquals(2, paginator.getPages());
        Assert.assertEquals(1, paginator.getPage());
        // page 2
        pageNum = 2;
        paginator = new Paginator();
        list = this.teamService.list(pageNum, paginator);

        Assert.assertTrue(list.size() == 1);
        for (int i = 0; i < list.size(); i++) {
            Team team = list.get(i);
            assertTeam(team, i + 11);
        }
        Assert.assertEquals(11, paginator.getItems());
        Assert.assertEquals(2, paginator.getPages());
        Assert.assertEquals(2, paginator.getPage());

    }

    @Test
    @DatabaseSetup
    public void testList() throws Exception {
        List<Team> list = this.teamService.list();

        Assert.assertTrue(list.size() == 11);
        for (int i = 0; i < list.size(); i++) {
            Team team = list.get(i);
            assertTeam(team, i + 1);
        }

    }

    @Test
    @DatabaseSetup
    public void testGetByName() throws Exception {
        String teamName = "team1";
        Team team = this.teamService.get(teamName);

        assertTeam(team, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    @DatabaseSetup
    public void testGetByNameNotExists() throws Exception {
        String teamName = "team1000";
        this.teamService.get(teamName);
    }

    @Test
    @DatabaseSetup
    public void testAdd() throws Exception {
        Workflow workflow = new Workflow();
        workflow.setName("test");
        workflow.setCanSkip(false);
        workflow.setCommon(true);
        workflow.setLastStatusDependency(DependingStatus.NONE);
        workflow.setStatus(WorkflowStatus.OPEN);
        workflow.setTimeout(1);
        workflow.setOwner(1l);
        this.workflowService.createOrUpdate(workflow);
        Team team = new Team();
        String newTeam = "admin16";
        team.setName(newTeam);

        this.teamService.add(team);
        Long id = team.getId();

        Assert.assertNotNull(id);
        Team actual = this.teamService.get(id);
        assertTeam(actual, id, team.getName(), new Date(), new Date());
        Task task = this.taskService.getRootTask(newTeam);
        Assert.assertNotNull(task);
        WorkflowDetail detail = this.workflowDetailService.get(workflow.getId(), task.getId());
        Assert.assertNotNull(detail);
        List<WorkflowTaskDependency> list = this.workflowDependencyService.listByWorkflowDetailId(detail.getId());
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(this.taskService.addRootTaskIfAbsent("").getId(), list.get(0).getDependencyTaskId());

    }

    @Test(expected = IllegalArgumentException.class)
    @DatabaseSetup
    public void testAddDuplicate() throws Exception {
        Team team = new Team();
        team.setName("team1");

        this.teamService.add(team);
    }

    @Test
    @DatabaseSetup
    public void testRemove() throws Exception {
        long teamId = 2;
        this.teamService.remove(teamId);

        Team actual = this.teamService.get(teamId);

        Assert.assertNull(actual);

    }

    @Test
    @DatabaseSetup
    public void testUpdate() throws Exception {
        long teamId = 2;
        String newTeamName = "newteam";
        String oldTeamName = "team2";
        Task task = this.taskService.getRootTask(oldTeamName);
        Assert.assertNotNull(task);
        task = this.taskService.getRootTask(newTeamName);
        Assert.assertNull(task);

        this.teamService.updateName(teamId, newTeamName);
        task = this.taskService.getRootTask(newTeamName);
        Assert.assertNotNull(task);
        task = this.taskService.getRootTask(oldTeamName);
        Assert.assertNull(task);
        Team team = this.teamService.get(teamId);
        assertTeam(team, teamId, newTeamName, team.getCreateTime(), new Date());

    }

    @Test
    @DatabaseSetup
    public void testUpdateWithSameName() throws Exception {
        long teamId = 2;
        String newTeamName = "team2";
        this.teamService.updateName(teamId, newTeamName);

        Team team = this.teamService.get(teamId);
        assertTeam(team, teamId, newTeamName, team.getCreateTime(), new Date());

    }

    @Test(expected = IllegalArgumentException.class)
    @DatabaseSetup
    public void testUpdateWithUnexistsId() throws Exception {
        this.teamService.updateName(100L, "123");

    }

    @Test(expected = IllegalArgumentException.class)
    @DatabaseSetup
    public void testUpdateWithEmptyName() throws Exception {
        this.teamService.updateName(1L, "");

    }

    @Test(expected = IllegalArgumentException.class)
    @DatabaseSetup
    public void testUpdateWithexistsName() throws Exception {
        this.teamService.updateName(1L, "team2");

    }

    @Test(expected = IllegalArgumentException.class)
    @DatabaseSetup
    public void testRemoveWithUserExists() throws Exception {
        long teamId = 1;
        this.teamService.remove(teamId);
    }

    private void assertTeam(Team team, long id) throws ParseException {
        assertTeam(team, id, "team" + id, sdf.parse("2014-11-26 17:00:00"), sdf.parse("2014-11-26 17:38:00"));
    }

    private void assertTeam(Team team, long id, String name, Date createTime, Date updateTime) throws ParseException {
        Assert.assertEquals(Long.valueOf(id), team.getId());
        Assert.assertEquals(name, team.getName());
        Assert.assertTrue(TestUtils.generallyEquals(createTime, team.getCreateTime()));
        Assert.assertTrue(TestUtils.generallyEquals(updateTime, team.getUpdateTime()));
    }

}
