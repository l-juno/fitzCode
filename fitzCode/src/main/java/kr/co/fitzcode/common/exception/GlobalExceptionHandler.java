package kr.co.fitzcode.common.exception;

import kr.co.fitzcode.admin.exception.NoticeNotFoundException;
import kr.co.fitzcode.common.enums.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.NoSuchElementException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception e, Model model) {
        model.addAttribute("error", "error");
        log.error("error msg: {}", e.getMessage(), e);
        return "404-error";
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    public String handleBadSqlGrammarException(BadSqlGrammarException ex, Model model) {
        model.addAttribute("orders", List.of());
        model.addAttribute("totalPages", 1);
        model.addAttribute("currentPage", 1);
        model.addAttribute("size", 10);
        model.addAttribute("status", null);
        model.addAttribute("sortBy", "createdAt");
        model.addAttribute("orderStatuses", OrderStatus.values());
        model.addAttribute("pageRange", new int[]{1});
        return "admin/order/orderList"; // 동일한 뷰로 렌더링
    }
}