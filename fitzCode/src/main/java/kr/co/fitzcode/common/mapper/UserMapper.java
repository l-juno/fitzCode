package kr.co.fitzcode.common.mapper;

import kr.co.fitzcode.common.dto.UserDTO;
import kr.co.fitzcode.common.enums.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface UserMapper {

    UserDTO getUserByNickname(String nickname);

}
