package kr.co.fitzcode.user.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kr.co.fitzcode.common.dto.EmailMessageDTO;
import kr.co.fitzcode.common.dto.OrderDTO;
import kr.co.fitzcode.common.dto.UserOrderDetailDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final UserEmailService userEmailService;

    public String sendEmail(EmailMessageDTO emailMessage, String type) {
        String authNum = createCode();

        MimeMessage mimeMessage = mailSender.createMimeMessage();


        if (type.equals("password")) { // 패스워드 분실 시 발송할 메일
            userEmailService.setTempPassword(emailMessage.getTo(), authNum);
        }

        try {
            MimeMessageHelper mimeMessageHelper =
                    new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo());
            mimeMessageHelper.setSubject(emailMessage.getSubject());
            mimeMessageHelper.setText(setContext(authNum, type), true);
            mailSender.send(mimeMessage);
            log.info("인증 번호 >>>>>>>>>>" + authNum);
            log.info("메일 발송 성공");
            return authNum;
        } catch (MessagingException e) {
            log.info("메일 발송 실패");
            throw new RuntimeException(e);
        }
    }

    // thymeleaf 를 통한 html 적용
    private String setContext(String authNum, String type) {
        Context context = new Context();
        context.setVariable("authNum", authNum);
        context.setVariable("type", type);
        return templateEngine.process(type, context);
    }

    // 인증번호 및 임시 비밀번호 생성 메서드
    private String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) { // 8번 반복
            int index = random.nextInt(3); // 랜덤값 : 0, 1, 2
            switch (index) {
                case 0:
                    key.append((char) (random.nextInt(26) + 65));
                    break;
                case 1:
                    key.append((char) (random.nextInt(26) + 97));
                    break;
                case 2:
                    key.append(random.nextInt(10));
                    break;
            }
        }
        log.info("생성된 코드 : " + key.toString());
        return key.toString();
    }


    public String sendOrderConfirmationEmail(EmailMessageDTO emailMessage, OrderDTO orderDTO, List<UserOrderDetailDTO> orderDetailList) {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper =
                    new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo());
            mimeMessageHelper.setSubject(emailMessage.getSubject());
            mimeMessageHelper.setText(setOrderContext(orderDTO, orderDetailList), true);
            mailSender.send(mimeMessage);
            return "Order confirmation email sent successfully!";
        } catch (MessagingException e) {
            log.info("메일 발송 실패");
            throw new RuntimeException(e);
        }
    }


    private String setOrderContext(OrderDTO orderDTO, List<UserOrderDetailDTO> orderDetailList) {
        Context context = new Context();
        context.setVariable("orderDTO", orderDTO);  // order dto
        context.setVariable("orderDetailList", orderDetailList);  // order details
        return templateEngine.process("order/orderConfirmationEmail", context);  // Specify template name
    }


}
