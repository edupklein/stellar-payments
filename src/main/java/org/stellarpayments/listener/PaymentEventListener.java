package org.stellarpayments.listener;

import org.stellar.sdk.requests.EventListener;
import org.stellar.sdk.responses.operations.OperationResponse;
import org.stellar.sdk.responses.operations.PaymentOperationResponse;
import org.stellarpayments.response.PaymentMapper;
import org.stellarpayments.response.PaymentResponse;

import java.util.Optional;

public class PaymentEventListener implements EventListener<OperationResponse> {

    @Override
    public void onEvent(OperationResponse response) {
        PaymentResponse paymentResponse = PaymentMapper.fromOperation(response);
        System.out.println("New payment detected!");
        System.out.printf("From: %s -> To: %s | Amount: %s%n", paymentResponse.getFrom(), paymentResponse.getTo(), paymentResponse.getAmount());
    }

    @Override
    public void onFailure(Optional<Throwable> optional, Optional<Integer> optional1) {
        optional.ifPresentOrElse(
                e -> {
                    System.err.println("Stream Error: " + e.getMessage());
                    e.printStackTrace();
                },
                () -> System.err.println("Stream Unknown Error.")
        );
    }
}
