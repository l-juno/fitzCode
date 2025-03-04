package kr.co.fitzcode.admin.handler;

import kr.co.fitzcode.common.enums.ProductStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductStatusTypeHandler extends BaseTypeHandler<ProductStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ProductStatus parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getCode());
    }

    @Override
    public ProductStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int code = rs.getInt(columnName);
        return rs.wasNull() ? null : ProductStatus.fromCode(code);
    }

    @Override
    public ProductStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int code = rs.getInt(columnIndex);
        return rs.wasNull() ? null : ProductStatus.fromCode(code);
    }

    @Override
    public ProductStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int code = cs.getInt(columnIndex);
        return cs.wasNull() ? null : ProductStatus.fromCode(code);
    }
}