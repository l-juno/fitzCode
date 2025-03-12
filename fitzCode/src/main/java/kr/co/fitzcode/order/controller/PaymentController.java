package kr.co.fitzcode.order.controller;

import kr.co.fitzcode.common.util.SecurityUtils;
import kr.co.fitzcode.order.service.PriceService;
import kr.co.fitzcode.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PriceService priceService;

    @PostMapping("/getPostOneInformation")
    public ResponseEntity<Map<String, Object>> getPostOneInformation(@RequestParam int productId,
                                                                     @RequestParam int sizeCode,
                                                                     @RequestParam("finalPrice") int finalPrice) {
        Map<String, Object> response = new HashMap<>();

        //TODO use japsyt to encrypt storeId and channelKey
        response.put("storeId", "store-bdc48a9e-8830-47a5-a533-d720a26b1237");
        response.put("channelKey", "channel-key-de5c26f0-b9a4-4b9e-a433-78a2fdf6ba13");

        int userId = SecurityUtils.getUserId();
        String date = LocalDate.now().toString();
        String orderInformation = userId + "-" + date;

        // TODO add payment id to payment table
        String paymentId = orderInformation + "-" +UUID.randomUUID().toString();

        response.put("paymentId", paymentId);
        response.put("orderName", orderInformation);
        response.put("totalAmount", finalPrice);

        response.put("currency", "CURRENCY_KRW");
        response.put("payMethod", "CARD");
        return ResponseEntity.ok(response);
    }
}
