package com.yeahmobi.yscheduler.model.service;

import java.util.List;

import com.yeahmobi.yscheduler.common.Paginator;
import com.yeahmobi.yscheduler.model.User;
import com.yeahmobi.yscheduler.model.common.NameValuePair;

public interface UserService {

    User get(long id);

    User get(String username);

    User getAdmin();

    String getTeamName(long id);

    List<User> list(int pageNum, Paginator paginator);

    List<User> listByTeam(long teamId, int pageNum, Paginator paginator);

    List<User> listByTeam(long teamId);

    boolean hasTeamUser(long teamId);

    List<NameValuePair> list();

    void add(User user);

    void update(User user);

    void remove(long userId);

    void resetPassword(long userId);

    void regenToken(long userId);

}
