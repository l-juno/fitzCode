package kr.co.fitzcode.mypage.service;

import kr.co.fitzcode.mypage.dto.ProductDto;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public interface MypageService {

    HashMap<String, Objects> OrderList(String userId);
}
