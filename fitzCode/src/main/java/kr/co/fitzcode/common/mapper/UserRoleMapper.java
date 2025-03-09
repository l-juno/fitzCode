package kr.co.fitzcode.common.mapper;

import kr.co.fitzcode.common.enums.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface UserRoleMapper {



    List<Integer> findRolesByUserId(@Param("userId") int userId);


    default List<UserRole> findRolesInStringByUserId(int userId) {
        return this.findRolesByUserId(userId).stream()
                .map(UserRole::fromCode)  // Convert DB role_id to Enum
                .collect(Collectors.toList());
    }
}
