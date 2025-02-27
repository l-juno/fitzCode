package kr.co.fitzcode.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception e, Model model) {
        model.addAttribute("error", "예상치 못한 오류가 발생했습니다.");
        log.error("예상치 못한 오류 발생: {}", e.getMessage(), e);
        return "errorPage";
    }
}