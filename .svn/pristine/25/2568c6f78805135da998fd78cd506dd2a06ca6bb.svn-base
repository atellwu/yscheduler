package com.yeahmobi.yscheduler.model.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class AuthorityModeHandler extends BaseTypeHandler<AuthorityMode> {

    public AuthorityModeHandler() {
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, AuthorityMode parameter, JdbcType jdbcType)
                                                                                                            throws SQLException {
        byte typeId = (byte) parameter.getId();
        if (jdbcType == null) {
            ps.setByte(i, typeId);
        } else {
            ps.setObject(i, typeId, jdbcType.TYPE_CODE);
        }
    }

    @Override
    public AuthorityMode getNullableResult(ResultSet rs, String columnName) throws SQLException {
        byte id = rs.getByte(columnName);
        return AuthorityMode.valueOf(id);
    }

    @Override
    public AuthorityMode getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        byte id = cs.getByte(columnIndex);
        return AuthorityMode.valueOf(id);
    }

}
