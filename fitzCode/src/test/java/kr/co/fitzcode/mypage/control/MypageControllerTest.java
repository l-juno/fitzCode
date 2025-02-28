package kr.co.fitzcode.mypage.control;

import kr.co.fitzcode.mypage.service.MypageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(MypageController.class)
class MypageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MypageService mypageService;



}