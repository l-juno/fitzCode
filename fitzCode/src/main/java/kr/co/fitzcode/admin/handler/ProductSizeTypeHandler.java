package kr.co.fitzcode.admin.handler;

import kr.co.fitzcode.common.enums.ProductSize;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;

@MappedTypes(ProductSize.class)
@MappedJdbcTypes(JdbcType.INTEGER)
public class ProductSizeTypeHandler extends BaseTypeHandler<ProductSize> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ProductSize parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getCode()); // Enum을 DB에 저장할 때 코드 값 저장
    }

    @Override
    public ProductSize getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int code = rs.getInt(columnName);
        return ProductSize.fromCode(code); // DB에서 불러올 때 Enum으로 변환
    }

    @Override
    public ProductSize getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int code = rs.getInt(columnIndex);
        return ProductSize.fromCode(code);
    }

    @Override
    public ProductSize getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int code = cs.getInt(columnIndex);
        return ProductSize.fromCode(code);
    }
}