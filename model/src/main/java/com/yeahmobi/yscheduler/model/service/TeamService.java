package com.yeahmobi.yscheduler.model.service;

import java.util.List;

import com.yeahmobi.yscheduler.common.Paginator;
import com.yeahmobi.yscheduler.model.Team;

/**
 * @author Leo Liang
 */
public interface TeamService {

    Team get(long id);

    List<Team> list(int pageNum, Paginator paginator);

    List<Team> list();

    Team get(String teamName);

    void add(Team team);

    void remove(long id);

    void updateName(long id, String name);

}
