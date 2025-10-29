package org.stellarpayments.service;

import org.springframework.stereotype.Component;
import org.stellar.sdk.*;
import org.stellar.sdk.exception.BadRequestException;
import org.stellar.sdk.exception.BadResponseException;
import org.stellar.sdk.exception.NetworkException;
import org.stellar.sdk.operations.PaymentOperation;
import org.stellar.sdk.responses.TransactionResponse;

import java.math.BigDecimal;

import static org.stellar.sdk.AbstractTransaction.MIN_BASE_FEE;

@Component
public class PaymentService {

    private final Server server;
    private final KeyPair sourceKeyPair;
    private final TransactionBuilderAccount sourceAccount;
    private final Network network = Network.TESTNET;

    public PaymentService() throws IllegalAccessException {
        this.server = new Server("https://horizon-testnet.stellar.org");

        String secret = System.getenv("MAIN_ACCOUNT_SECRET_KEY");
        if(secret == null) {
            throw new IllegalAccessException("MAIN_ACCOUNT_SECRET_KEY not set");
        }
        sourceKeyPair = KeyPair.fromSecretSeed(secret);
        System.out.println("Source KeyPair: " + sourceKeyPair.getAccountId());

        sourceAccount = server.loadAccount(sourceKeyPair.getAccountId());
    }

    public String sendPayment(String destination, String amount) {

        PaymentOperation paymentOperation = PaymentOperation.builder()
                .destination(destination)
                .asset(Asset.createNativeAsset())
                .amount(new BigDecimal(amount))
                .build();

        Transaction transaction = new TransactionBuilder(sourceAccount, network)
                .setBaseFee(MIN_BASE_FEE)
                .setTimeout(30)
                .addOperation(paymentOperation)
                .build();

        transaction.sign(sourceKeyPair);

        String response;

        try {
            TransactionResponse transactionResponse = server.submitTransaction(transaction);
            response = "Transaction successful! Hash: " + transactionResponse.getHash();
        }catch (BadRequestException e) {
            response = "Bad Request: " + e.getMessage();
        }catch (BadResponseException e) {
            response = "Bad Response: " + e.getMessage();
        }catch (NetworkException e) {
            response = "Network issue: " + e.getMessage();
        }
        return response;
        // server.close();
    }
}