package kr.co.fitzcode.user.mapper;

import kr.co.fitzcode.user.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    UserDto getUserOne(@Param("userId") int userId);
}
