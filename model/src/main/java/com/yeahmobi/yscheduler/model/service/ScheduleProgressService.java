package com.yeahmobi.yscheduler.model.service;

public interface ScheduleProgressService {

    Long getCurrentScheduleTime();

    void saveCurrentScheduleTime(long currentScheduleTime);

}
