package kr.co.fitzcode.mypage.control;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.fitzcode.mypage.dto.ProductDto;
import kr.co.fitzcode.mypage.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {
    private final MypageService mypageService;

    // 주문내역으로 이동하는 페이지
    @GetMapping("/orderlist")
    public String orderList(Model model, HttpSession session){
        String userId = (String) session.getAttribute("user");
        HashMap<String, Objects> map = mypageService.OrderList(userId);
        model.addAttribute("orderMap", map);
        return "mypage/orderList";
    }
}
