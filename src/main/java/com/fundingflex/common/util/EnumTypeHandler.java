package com.fundingflex.common.util;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import com.fundingflex.common.enums.DeleteFlagEnum;
import com.fundingflex.funding.domain.enums.FundingsStatusEnum;

import java.sql.*;

public class EnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

    private final Class<E> type;

    public EnumTypeHandler(Class<E> type) {
        if (type == null) throw new IllegalArgumentException("Type argument cannot be null");
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        if (parameter instanceof DeleteFlagEnum) {
            ps.setString(i, parameter.name());
        } else if (parameter instanceof FundingsStatusEnum) {
            ps.setInt(i, parameter.ordinal());
        } else {
            throw new IllegalArgumentException("Unsupported enum type: " + parameter.getClass().getName());
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if (type == DeleteFlagEnum.class) {
            String value = rs.getString(columnName);
            return value == null ? null : Enum.valueOf(type, value);
        } else if (type == FundingsStatusEnum.class) {
            int ordinal = rs.getInt(columnName);
            return ordinal == -1 ? null : type.getEnumConstants()[ordinal];
        } else {
            throw new IllegalArgumentException("Unsupported enum type: " + type.getName());
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if (type == DeleteFlagEnum.class) {
            String value = rs.getString(columnIndex);
            return value == null ? null : Enum.valueOf(type, value);
        } else if (type == FundingsStatusEnum.class) {
            int ordinal = rs.getInt(columnIndex);
            return ordinal == -1 ? null : type.getEnumConstants()[ordinal];
        } else {
            throw new IllegalArgumentException("Unsupported enum type: " + type.getName());
        }
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (type == DeleteFlagEnum.class) {
            String value = cs.getString(columnIndex);
            return value == null ? null : Enum.valueOf(type, value);
        } else if (type == FundingsStatusEnum.class) {
            int ordinal = cs.getInt(columnIndex);
            return ordinal == -1 ? null : type.getEnumConstants()[ordinal];
        } else {
            throw new IllegalArgumentException("Unsupported enum type: " + type.getName());
        }
    }
}
