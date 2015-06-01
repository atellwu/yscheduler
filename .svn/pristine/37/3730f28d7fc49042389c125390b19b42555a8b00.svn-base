package com.yeahmobi.yscheduler.model.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeahmobi.yscheduler.common.Constants;
import com.yeahmobi.yscheduler.common.Paginator;
import com.yeahmobi.yscheduler.model.User;
import com.yeahmobi.yscheduler.model.WorkflowTaskInstance;
import com.yeahmobi.yscheduler.model.WorkflowTaskInstanceExample;
import com.yeahmobi.yscheduler.model.WorkflowTaskInstanceExample.Criteria;
import com.yeahmobi.yscheduler.model.dao.WorkflowTaskInstanceDao;
import com.yeahmobi.yscheduler.model.service.UserService;
import com.yeahmobi.yscheduler.model.service.WorkflowTaskInstanceService;

@Service
public class WorkflowTaskInstanceServiceImpl implements WorkflowTaskInstanceService {

    @Autowired
    private WorkflowTaskInstanceDao workflowTaskInstanceDao;

    @Autowired
    private UserService             userService;

    @PostConstruct
    public void init() {
    }

    public List<WorkflowTaskInstance> list(int pageNum, Paginator paginator, long userId) {
        WorkflowTaskInstanceExample example = new WorkflowTaskInstanceExample();

        authorityCheck(example, userId);

        int count = this.workflowTaskInstanceDao.countByExample(example);

        paginator.setItemsPerPage(Constants.PAGE_SIZE);
        paginator.setItems(count);
        paginator.setPage(pageNum);

        int offset = paginator.getBeginIndex() - 1;
        int limit = Constants.PAGE_SIZE;

        RowBounds rowBounds = new RowBounds(offset, limit);

        example.setOrderByClause("create_time DESC");
        return this.workflowTaskInstanceDao.selectByExampleWithRowbounds(example, rowBounds);
    }

    @SuppressWarnings("unchecked")
    private void authorityCheck(WorkflowTaskInstanceExample example, long userId) {

        User user = this.userService.get(userId);
        List<User> teamMembers = this.userService.listByTeam(user.getTeamId());

        List<Long> teamMemberIds = new ArrayList<Long>(CollectionUtils.collect(teamMembers, new Transformer() {

            public Long transform(Object user) {
                return ((User) user).getId();
            }
        }));

        Criteria criteria1 = example.createCriteria();
        criteria1.andOwnerIn(teamMemberIds);
    }
}
