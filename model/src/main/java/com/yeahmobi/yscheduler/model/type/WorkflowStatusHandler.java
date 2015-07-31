package com.yeahmobi.yscheduler.model.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class WorkflowStatusHandler extends BaseTypeHandler<WorkflowStatus> {

    public WorkflowStatusHandler() {
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, WorkflowStatus parameter, JdbcType jdbcType)
                                                                                                             throws SQLException {
        byte typeId = (byte) parameter.getId();
        if (jdbcType == null) {
            ps.setByte(i, typeId);
        } else {
            ps.setObject(i, typeId, jdbcType.TYPE_CODE);
        }
    }

    @Override
    public WorkflowStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        byte id = rs.getByte(columnName);
        return WorkflowStatus.valueOf(id);
    }

    @Override
    public WorkflowStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        byte id = cs.getByte(columnIndex);
        return WorkflowStatus.valueOf(id);
    }

}
