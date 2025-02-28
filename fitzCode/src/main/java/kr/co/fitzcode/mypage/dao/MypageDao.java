package kr.co.fitzcode.mypage.dao;

import kr.co.fitzcode.mypage.dto.ProductDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Repository
@Mapper
public interface MypageDao {
    HashMap<String, Objects> getOderListByUserId(String userId);
}
