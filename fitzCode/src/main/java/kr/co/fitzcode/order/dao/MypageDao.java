package kr.co.fitzcode.order.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Objects;

@Repository
@Mapper
public interface MypageDao {
    HashMap<String, Objects> getOderListByUserId(String userId);
}
