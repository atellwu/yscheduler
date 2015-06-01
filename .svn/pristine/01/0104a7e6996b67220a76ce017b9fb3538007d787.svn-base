package com.yeahmobi.yscheduler.model.service;

import java.util.List;

import com.yeahmobi.yscheduler.common.Paginator;
import com.yeahmobi.yscheduler.model.Attempt;

public interface AttemptService {

    Attempt get(long id);

    Attempt getLastOne(long instanceId);

    void archiveExistsAttempts(long instanceId);

    List<Attempt> list(long instanceId, int pageNum, Paginator paginator);

    void save(Attempt attempt);

    List<Attempt> getAllUncompleteds();

    int countActive(Long instanceId);

    void update(Attempt attempt);

}
