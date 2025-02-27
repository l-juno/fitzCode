package kr.co.fitzcode;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FitzCodeController {

    @GetMapping("/")
    public String mainPage() {
        return "fitzCode";
    }
}