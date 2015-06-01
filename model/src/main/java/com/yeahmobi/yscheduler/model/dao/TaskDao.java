package com.yeahmobi.yscheduler.model.dao;

import java.util.List;

import com.yeahmobi.yscheduler.model.Task;
import com.yeahmobi.yscheduler.model.dao.mapper.TaskMapper;

public interface TaskDao extends TaskMapper {

    List<Task> selectAll();
}
