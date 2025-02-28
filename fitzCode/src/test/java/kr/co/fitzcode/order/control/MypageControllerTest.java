package kr.co.fitzcode.order.control;

import kr.co.fitzcode.order.service.MypageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MypageController.class)
class MypageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MypageService mypageService;



}