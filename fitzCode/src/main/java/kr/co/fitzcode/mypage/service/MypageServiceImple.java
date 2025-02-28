package kr.co.fitzcode.mypage.service;

import kr.co.fitzcode.mypage.dao.MypageDao;
import kr.co.fitzcode.mypage.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MypageServiceImple implements MypageService{
    private final MypageDao mypageDao;


    @Override
    public HashMap<String, Objects> OrderList(String userId) {
        // 가져오는 값 :  p.product_name, p.price, p.image_url, o.order_status, o.created_at
        HashMap<String, Objects> map = mypageDao.getOderListByUserId(userId);

        // 뷰단에는 리스트가 반환되어야해
        // 그렇다면

        return map;
    }
}
