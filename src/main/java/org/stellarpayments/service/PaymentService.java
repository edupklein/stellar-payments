package org.stellarpayments.service;

import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import org.stellar.sdk.*;
import org.stellar.sdk.exception.BadRequestException;
import org.stellar.sdk.exception.BadResponseException;
import org.stellar.sdk.exception.NetworkException;
import org.stellar.sdk.operations.PaymentOperation;
import org.stellar.sdk.requests.RequestBuilder;
import org.stellar.sdk.responses.Page;
import org.stellar.sdk.responses.TransactionResponse;
import org.stellar.sdk.responses.operations.OperationResponse;
import org.stellarpayments.response.PaymentMapper;
import org.stellarpayments.response.PaymentResponse;
import org.stellarpayments.response.Response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public Response<String> sendPayment(String destination, String amount) {

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

        Response<String> response;

        try {
            transaction.sign(sourceKeyPair);
            TransactionResponse transactionResponse = server.submitTransaction(transaction);
            response = new Response(true, "Transaction successful!", "Hash: " + transactionResponse.getHash());
        }catch (BadRequestException e) {
            response = new Response(false, "Bad Request: " + e.getMessage(), null);
        }catch (BadResponseException e) {
            response = new Response(false, "Bad Response: " + e.getMessage(), null);
        }catch (NetworkException e) {
            response = new Response(false, "Network issue: " + e.getMessage(), null);
        }catch (Exception e) {
            response = new Response(false, "Unexpected error: " + e.getMessage(), null);
        }
        System.out.println(response);
        return response;
    }

    public Response<List<PaymentResponse>> listPayments() {
        Response<List<PaymentResponse>> response;
        try{
            Page<OperationResponse> paymentsPage = server.payments()
                    .forAccount(sourceKeyPair.getAccountId())
                    .limit(100)
                    .order(RequestBuilder.Order.DESC)
                    .execute();

            List<PaymentResponse> paymentsList = paymentsPage.getRecords()
                    .stream()
                    .map(PaymentMapper::fromOperation)
                    .collect(Collectors.toList());

            response = new Response<>(true, "List of Payments", paymentsList);
        }catch (Exception e) {
            response = new Response<>(false, "Error fetching payments: " + e.getMessage(), null);
        }
        System.out.println(response);
        return response;
    }

    @PreDestroy
    public void onShutdown() {
        if(server != null) {
            server.close();
            System.out.println("Stellar Server connection closed.");
        }
    }
}