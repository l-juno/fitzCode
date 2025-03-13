package kr.co.fitzcode.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/community")
public class CommentController {

    @GetMapping("/list")
    public String list(){
        return "community/communityList";
    }

    @GetMapping("/detail")
    public String detail(){
        return "community/communityDetail";
    }
}
