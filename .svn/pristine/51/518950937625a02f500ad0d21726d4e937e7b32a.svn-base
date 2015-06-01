package com.yeahmobi.yscheduler.model.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeahmobi.yscheduler.common.Constants;
import com.yeahmobi.yscheduler.common.Paginator;
import com.yeahmobi.yscheduler.model.TeamWorkflowInstanceStatus;
import com.yeahmobi.yscheduler.model.TeamWorkflowInstanceStatusExample;
import com.yeahmobi.yscheduler.model.TeamWorkflowInstanceStatusExample.Criteria;
import com.yeahmobi.yscheduler.model.User;
import com.yeahmobi.yscheduler.model.common.Query;
import com.yeahmobi.yscheduler.model.dao.TeamWorkflowInstanceStatusDao;
import com.yeahmobi.yscheduler.model.service.TeamWorkflowStatusInstanceService;
import com.yeahmobi.yscheduler.model.service.UserService;
import com.yeahmobi.yscheduler.model.type.WorkflowInstanceStatus;

@Service
public class TeamWorkflowStatusInstanceServiceImpl implements TeamWorkflowStatusInstanceService {

    @Autowired
    private TeamWorkflowInstanceStatusDao workflowInstanceDao;

    @Autowired
    private UserService                   userService;

    public void updateStatus(long teamId, long workflowInstanceId, WorkflowInstanceStatus status) {
        TeamWorkflowInstanceStatusExample example = new TeamWorkflowInstanceStatusExample();
        Criteria criteria = example.createCriteria();
        criteria.andTeamIdEqualTo(teamId);
        criteria.andWorkflowInstanceIdEqualTo(workflowInstanceId);

        TeamWorkflowInstanceStatus record = new TeamWorkflowInstanceStatus();
        record.setStatus(status);
        record.setUpdateTime(new Date());
        this.workflowInstanceDao.updateByExampleSelective(record, example);

    }

    public List<TeamWorkflowInstanceStatus> list(Query query, long userId, long workflowId, int pageNum,
                                                 Paginator paginator) {
        User user = this.userService.get(userId);
        Long teamId = user.getTeamId();

        TeamWorkflowInstanceStatusExample example = new TeamWorkflowInstanceStatusExample();

        Criteria criteria = example.or();

        query(query, criteria);

        criteria.andWorkflowIdEqualTo(workflowId);
        criteria.andTeamIdEqualTo(teamId);

        int count = this.workflowInstanceDao.countByExample(example);

        paginator.setItemsPerPage(Constants.PAGE_SIZE);
        paginator.setItems(count);
        paginator.setPage(pageNum);

        int offset = paginator.getBeginIndex() - 1;
        int limit = Constants.PAGE_SIZE;

        RowBounds rowBounds = new RowBounds(offset, limit);

        example.setOrderByClause("id DESC");
        return this.workflowInstanceDao.selectByExampleWithRowbounds(example, rowBounds);
    }

    private void query(Query query, Criteria criteria) {
        if (query.getWorkflowInstanceStatus() != null) {
            criteria.andStatusEqualTo(query.getWorkflowInstanceStatus());
        }
    }

    public void save(TeamWorkflowInstanceStatus status) {
        status.setCreateTime(new Date());
        this.workflowInstanceDao.insert(status);
    }

}
