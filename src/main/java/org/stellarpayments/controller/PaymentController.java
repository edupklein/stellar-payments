package org.stellarpayments.controller;

import org.springframework.web.bind.annotation.*;
import org.stellar.sdk.responses.operations.OperationResponse;
import org.stellarpayments.model.PaymentRequest;
import org.stellarpayments.response.PaymentResponse;
import org.stellarpayments.response.Response;
import org.stellarpayments.service.PaymentService;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public Response<String> sendPayment(@RequestBody PaymentRequest paymentRequest) throws Exception {
        return paymentService.sendPayment(paymentRequest.getToAddress(), paymentRequest.getAmount());
    }

    @GetMapping
    public Response<List<PaymentResponse>> listPayments() {
        return paymentService.listPayments();
    }
}
