package org.stellarpayments.model;

import lombok.Data;

@Data
public class PaymentRequest {
    private String toAddress;
    private String Amount;
}
