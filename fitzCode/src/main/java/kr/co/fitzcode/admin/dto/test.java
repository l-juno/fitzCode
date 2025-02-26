package kr.co.fitzcode.admin.dto;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class test {

    @GetMapping("/fragment/footer")
    public String footer() {
        return "/fragment/footer";
    }

    @GetMapping("/fragment/header")
    public String header() {
        return "/fragment/header";
    }

    @GetMapping("/admin/managerSidebar")
    public String sidebar() {
        return "/admin/managerSidebar";
    }
}
