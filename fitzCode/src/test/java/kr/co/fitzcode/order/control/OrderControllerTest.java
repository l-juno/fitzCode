package kr.co.fitzcode.order.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.fitzcode.order.mapper.OrderMapper;
import kr.co.fitzcode.order.service.OrderService;
import kr.co.fitzcode.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private OrderMapper orderMapper;


    private MockHttpSession session;


    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
    }

    @DisplayName("사용자 세션 주기")
    @Test
    void setSession() {
        session.setAttribute("userId", 1);
        assertNotNull(session.getAttribute("userId"));
    }

  
}