package com.javatechie.controller;

import com.javatechie.service.OrderSupportAIAssistantService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OrderSupportAIAssistantController {

    private OrderSupportAIAssistantService aiAssistantService;

    public OrderSupportAIAssistantController(OrderSupportAIAssistantService aiAssistantService) {
        this.aiAssistantService = aiAssistantService;
    }

    @GetMapping("/order-support")
    public String getOrderSupportResponse(@RequestParam String customerName, @RequestParam String orderId,@RequestParam String customerMessage) {
        return aiAssistantService.assistWithOrderSupport(customerName, orderId, customerMessage);
    }


    @GetMapping("/order-ai-support")
    public String talkToOrderAISupport(@RequestParam String customerName, @RequestParam String orderId,@RequestParam String customerMessage) {
        return aiAssistantService.talkToAISupport(customerName, orderId, customerMessage);
    }
}
