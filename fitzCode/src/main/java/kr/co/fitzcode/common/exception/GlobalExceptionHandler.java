package kr.co.fitzcode.common.exception;

import kr.co.fitzcode.admin.exception.NoticeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoticeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoticeNotFoundException(NoticeNotFoundException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "errorPage";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception e, Model model) {
        model.addAttribute("error", "예상치 못한 오류가 발생했습니다.");
        e.printStackTrace();
        return "errorPage";
    }
}