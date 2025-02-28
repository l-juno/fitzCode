package kr.co.fitzcode.order.service;

import java.util.HashMap;
import java.util.Objects;

public interface MypageService {

    HashMap<String, Objects> OrderList(String userId);
}
