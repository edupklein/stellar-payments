package org.stellarpayments.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stellarpayments.model.PaymentRequest;
import org.stellarpayments.service.PaymentService;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public String sendPayment(@RequestBody PaymentRequest paymentRequest) throws Exception {
        return paymentService.sendPayment(paymentRequest.getToAddress(), paymentRequest.getAmount());
    }
}
