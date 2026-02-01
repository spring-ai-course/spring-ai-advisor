package com.javatechie.service;

import com.javatechie.advisor.AuditTokenUsageAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderSupportAIAssistantService {

    @Value("classpath:prompts/order_system_template.st")
    private Resource orderSystemPrompt;

    @Value("classpath:prompts/order_user_template.st")
    private Resource orderUserPrompt;

    @Value("classpath:prompts/order_system_policy.st")
    private Resource orderSystemPolicyPrompt;


    private final ChatClient chatClient;

    public OrderSupportAIAssistantService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }


    public String assistWithOrderSupport(String customerName, String orderId, String customerMessage) {
        return chatClient
                .prompt()
                .system(orderSystemPrompt)
                .user(promptUserSpec -> promptUserSpec.text(orderUserPrompt)
                        .param("customerName", customerName)
                        .param("orderId", orderId)
                        .param("customerMessage", customerMessage))
                .call()
                .content();
    }

    public String talkToAISupport(String customerName, String orderId, String customerMessage) {
        return chatClient
                .prompt()
                .advisors(
                        List.of(
                                new SimpleLoggerAdvisor(),
                                new SafeGuardAdvisor(List.of("password", "otp", "cvv")
                                        , "For security reason , we never ask such sensitive information please talk to our customer support directly", 1),
                                new AuditTokenUsageAdvisor())
                )
                .system(orderSystemPolicyPrompt)
                .user(promptUserSpec -> promptUserSpec.text(orderUserPrompt)
                        .param("customerName", customerName)
                        .param("orderId", orderId)
                        .param("customerMessage", customerMessage))
                .call()
                .content();
    }

}
