package kr.co.fitzcode.common.exception;

import kr.co.fitzcode.admin.exception.NoticeNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NoticeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoticeNotFoundException(NoticeNotFoundException e, Model model) {
        model.addAttribute("error", e.getMessage());
        log.error("Notice를 찾을 수 없음: {}", e.getMessage(), e);
        return "errorPage";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception e, Model model) {
        model.addAttribute("error", "예상치 못한 오류가 발생했습니다.");
        log.error("예상치 못한 오류 발생: {}", e.getMessage(), e);
        return "errorPage";
    }
}