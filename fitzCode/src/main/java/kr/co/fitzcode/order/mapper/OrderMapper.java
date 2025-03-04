package kr.co.fitzcode.order.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Mapper
public interface OrderMapper {
    public List<HashMap<String, Object>> getOderListByUserId(@Param("userId") int userId);
}
