package kr.co.fitzcode.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/community")
public class CommunityController {

    @GetMapping("/list")
    public String list(){
        return "community/communityList";
    }

    @GetMapping("/form")
    public String form(){
        return "community/communityForm";
    }


    @GetMapping("/detail")
    public String detail(){
        return "community/communityDetail2";
    }
}
