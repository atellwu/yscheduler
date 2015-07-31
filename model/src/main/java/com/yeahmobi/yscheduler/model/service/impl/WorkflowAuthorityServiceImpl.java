package com.yeahmobi.yscheduler.model.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeahmobi.yscheduler.model.WorkflowAuthority;
import com.yeahmobi.yscheduler.model.WorkflowAuthorityExample;
import com.yeahmobi.yscheduler.model.dao.WorkflowAuthorityDao;
import com.yeahmobi.yscheduler.model.service.WorkflowAuthorityService;
import com.yeahmobi.yscheduler.model.type.AuthorityMode;

/**
 * @author Leo.Liang
 */
@Service
public class WorkflowAuthorityServiceImpl implements WorkflowAuthorityService {

    @Autowired
    private WorkflowAuthorityDao workflowAuthorityDao;

    public List<WorkflowAuthority> listByWorkflow(long workflowId) {
        WorkflowAuthorityExample example = new WorkflowAuthorityExample();
        example.or().andWorkflowIdEqualTo(workflowId);
        return this.workflowAuthorityDao.selectByExample(example);
    }

    public List<WorkflowAuthority> listByUser(long userId) {
        WorkflowAuthorityExample example = new WorkflowAuthorityExample();
        example.or().andUserIdEqualTo(userId);
        return this.workflowAuthorityDao.selectByExample(example);
    }

    public List<Long> listReadonlyUser(long workflowId) {
        List<Long> result = new ArrayList<Long>();
        WorkflowAuthorityExample example = new WorkflowAuthorityExample();
        example.or().andWorkflowIdEqualTo(workflowId);
        List<WorkflowAuthority> authorities = this.workflowAuthorityDao.selectByExample(example);
        for (WorkflowAuthority authority : authorities) {
            if (authority.getMode() == AuthorityMode.READONLY) {
                result.add(authority.getUserId());
            }
        }
        return result;
    }

    public List<Long> listWritableUser(long workflowId) {
        List<Long> result = new ArrayList<Long>();
        WorkflowAuthorityExample example = new WorkflowAuthorityExample();
        example.or().andWorkflowIdEqualTo(workflowId);
        List<WorkflowAuthority> authorities = this.workflowAuthorityDao.selectByExample(example);
        for (WorkflowAuthority authority : authorities) {
            if (authority.getMode() == AuthorityMode.WRITABLE) {
                result.add(authority.getUserId());
            }
        }
        return result;
    }

    public List<Long> listFollowUser(long workflowId) {
        List<Long> result = new ArrayList<Long>();
        WorkflowAuthorityExample example = new WorkflowAuthorityExample();
        example.or().andWorkflowIdEqualTo(workflowId);
        List<WorkflowAuthority> authorities = this.workflowAuthorityDao.selectByExample(example);
        for (WorkflowAuthority authority : authorities) {
            if (authority.getFollow()) {
                result.add(authority.getUserId());
            }
        }
        return result;
    }

    public boolean writable(long workflowId, long userId) {
        WorkflowAuthorityExample example = new WorkflowAuthorityExample();
        example.or().andWorkflowIdEqualTo(workflowId).andUserIdEqualTo(userId);
        List<WorkflowAuthority> authorities = this.workflowAuthorityDao.selectByExample(example);
        if (authorities.size() == 1) {
            return authorities.get(0).getMode() == AuthorityMode.WRITABLE;
        } else {
            return false;
        }
    }

    @Transactional
    public void add(List<Long> readableUsers, List<Long> writeableUsers, List<Long> followingUsers, long workflowId) {
        Map<Long, WorkflowAuthority> authorities = new HashMap<Long, WorkflowAuthority>();
        for (long userId : readableUsers) {
            WorkflowAuthority authority = createOrFindAuthority(authorities, userId, workflowId);
            authority.setMode(AuthorityMode.READONLY);
        }
        for (long userId : writeableUsers) {
            WorkflowAuthority authority = createOrFindAuthority(authorities, userId, workflowId);
            authority.setMode(AuthorityMode.WRITABLE);
        }
        for (long userId : followingUsers) {
            WorkflowAuthority authority = createOrFindAuthority(authorities, userId, workflowId);
            authority.setFollow(true);
        }
        for (WorkflowAuthority authority : authorities.values()) {
            Date time = new Date();
            authority.setCreateTime(time);
            authority.setUpdateTime(time);
            this.workflowAuthorityDao.insert(authority);
        }
    }

    @Transactional
    public void update(List<Long> readableUsers, List<Long> writeableUsers, List<Long> followingUsers, long workflowId) {
        WorkflowAuthorityExample example = new WorkflowAuthorityExample();
        example.or().andWorkflowIdEqualTo(workflowId);
        this.workflowAuthorityDao.deleteByExample(example);
        add(readableUsers, writeableUsers, followingUsers, workflowId);
    }

    private WorkflowAuthority createOrFindAuthority(Map<Long, WorkflowAuthority> authorities, long userId,
                                                    long workflowId) {
        WorkflowAuthority authority = authorities.get(userId);
        if (authority == null) {
            authority = new WorkflowAuthority();
            authority.setUserId(userId);
            authority.setWorkflowId(workflowId);
            authority.setMode(AuthorityMode.NONE);
            authority.setFollow(false);
            authorities.put(userId, authority);
        }
        return authority;
    }

    public void deleteByUser(long userId) {
        WorkflowAuthorityExample example = new WorkflowAuthorityExample();
        example.createCriteria().andUserIdEqualTo(userId);
        this.workflowAuthorityDao.deleteByExample(example);
    }

    @SuppressWarnings("unchecked")
    public List<Long> listReadonlyWorkflowIds(long userId) {
        WorkflowAuthorityExample example = new WorkflowAuthorityExample();
        example.createCriteria().andUserIdEqualTo(userId).andModeEqualTo(AuthorityMode.READONLY);
        return new ArrayList<Long>(CollectionUtils.collect(this.workflowAuthorityDao.selectByExample(example),
                                                           new Transformer() {

                                                               public Object transform(Object input) {
                                                                   return ((WorkflowAuthority) input).getWorkflowId();
                                                               }
                                                           }));
    }

    @SuppressWarnings("unchecked")
    public List<Long> listWritableWorkflowIds(long userId) {
        WorkflowAuthorityExample example = new WorkflowAuthorityExample();
        example.createCriteria().andUserIdEqualTo(userId).andModeEqualTo(AuthorityMode.WRITABLE);
        return new ArrayList<Long>(CollectionUtils.collect(this.workflowAuthorityDao.selectByExample(example),
                                                           new Transformer() {

                                                               public Object transform(Object input) {
                                                                   return ((WorkflowAuthority) input).getWorkflowId();
                                                               }
                                                           }));
    }

}
