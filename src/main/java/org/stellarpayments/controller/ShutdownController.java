package org.stellarpayments.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stellarpayments.response.Response;

@RestController
public class ShutdownController {

    private final ConfigurableApplicationContext context;

    @Autowired
    public ShutdownController(ConfigurableApplicationContext context) {
        this.context = context;
    }

    @PostMapping("/shutdown")
    public Response<String> shutdown() {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                context.close();
                System.out.println("Application shutting down...");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
        return new Response<>(true,"Shutting Down Application...",null);
    }
}
