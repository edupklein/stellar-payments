package org.stellarpayments.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private BigInteger id;
    private String sourceAccount;
    private String createdAt;
    private String transactionHash;
    private Boolean transactionSuccessful;
    private String type;
    private String amount;
    private String assetType;
    private String from;
    private String to;
    private String account;
    private String funder;
    private String startingBalance;
}
