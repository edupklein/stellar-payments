package org.stellarpayments.response;

import org.stellar.sdk.responses.operations.CreateAccountOperationResponse;
import org.stellar.sdk.responses.operations.OperationResponse;
import org.stellar.sdk.responses.operations.PaymentOperationResponse;

import java.math.BigInteger;

public class PaymentMapper {

    public static PaymentResponse fromOperation(OperationResponse op) {
        PaymentResponse response = new PaymentResponse();

        response.setId(op.getId() != null ? BigInteger.valueOf(op.getId()) : null);
        response.setSourceAccount(op.getSourceAccount());
        response.setCreatedAt(op.getCreatedAt());
        response.setTransactionHash(op.getTransactionHash());
        response.setTransactionSuccessful(op.getTransactionSuccessful());
        response.setType(op.getType());

        if(op instanceof PaymentOperationResponse paymentOp) {
            response.setAmount(paymentOp.getAmount());
            response.setAssetType(paymentOp.getAssetType());
            response.setFrom(paymentOp.getFrom());
            response.setTo(paymentOp.getTo());
        } else if(op instanceof CreateAccountOperationResponse accountOp) {
            response.setFunder(accountOp.getFunder());
            response.setAccount(accountOp.getAccount());
            response.setStartingBalance(accountOp.getStartingBalance());
        }

        return response;
    }
}
