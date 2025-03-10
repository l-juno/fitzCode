package kr.co.fitzcode.inquiry.control;


import jakarta.servlet.http.HttpSession;
import kr.co.fitzcode.inquiry.dto.InquiryDTO;
import kr.co.fitzcode.inquiry.dto.ProductDTO;
import kr.co.fitzcode.inquiry.service.InquiryService;
import kr.co.fitzcode.inquiry.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/inquiry")
@RequiredArgsConstructor
public class InquiryController {
    private final InquiryService inquiryService;

    // 문의 메뉴로 이동
    @GetMapping("/inquiryForm")
    public String inquiryForm(Model model, HttpSession session) {
        session.setAttribute("userId", 20);
        int userId = (int) session.getAttribute("userId");
        UserDTO dto = inquiryService.getUserOne(userId);
        model.addAttribute("userDTO", dto);
        return "inquiry/inquiryForm";
    }


    // 문의 데이터 저장
    @PostMapping("/inquiryForm")
    public String inquiryForm(@ModelAttribute InquiryDTO inquiryDTO) {
        inquiryService.registInquiry(inquiryDTO);
        return "redirect:/inquiry/inquiryForm";
    }

    // 개인 문의 내역 보기
    // 나중에 세션 생기면 @SessionAttribute("userId") int userId 사용하기
    @GetMapping("/inquiryList")
    public String inquiryList(Model model, HttpSession session) {
        session.setAttribute("userId", 20);
        int userId = (int) session.getAttribute("userId");
        List<HashMap<String, Object>> list = inquiryService.getInquiryList(userId);
        model.addAttribute("list", list);
        return "inquiry/inquiryList";
    }

    // 문의 상세보기
    @GetMapping("/detail")
    public String detail(@RequestParam("inquiryId") int inquiryId, Model model) {
        HashMap<String, Object> list = inquiryService.getInquiryDetail(inquiryId);
        model.addAttribute("inquiryDetail", list);
        return "inquiry/inquiryDetail";
    }

    // 상품검색
    @GetMapping("/searchProduct")
    @ResponseBody // AJAX 에 사용할 거니까
    public List<HashMap<String, Object>> searchProduct(@RequestParam("userInputProductName") String userInputProductName) {
        List<HashMap<String, Object>> list = inquiryService.searchProduct(userInputProductName);
        return list;
    }

    // 선택된 상품 가져오기
    @GetMapping("/selectedProduct")
    @ResponseBody
    public HashMap<String, Object> selectedProduct(@RequestParam("productId") int productId) {
        HashMap<String, Object> selectedProduct = inquiryService.selectedProduct(productId);
        return selectedProduct;
    }

    // 주문 내역
    @GetMapping("/searchOrderList")
    @ResponseBody
    public List<HashMap<String, Object>> searchOrderList(@RequestParam("userId") int userId) {
        List<HashMap<String, Object>> orderList = inquiryService.getUserAndOrderList(userId);
        return orderList;
    }

}
