package com.javatechie.controller;

import com.javatechie.service.MessageRolesDemoService;
import com.javatechie.service.OpenAIChatService;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OpenAIChatController {


    private final OpenAIChatService openAIChatService;

    private final MessageRolesDemoService messageRolesDemoService;

    public OpenAIChatController(OpenAIChatService openAIChatService,
                                MessageRolesDemoService messageRolesDemoService) {
        this.openAIChatService = openAIChatService;
        this.messageRolesDemoService = messageRolesDemoService;
    }


    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        return openAIChatService.chatWithOpenAILLM(message);
    }


    @GetMapping("/check/policy")
    public ChatResponse checkInsurancePolicy(@RequestParam String message) {
        return messageRolesDemoService.checkInsuranceV3Policy(message);
    }

    @GetMapping("/guide")
    public String guideUser(@RequestParam String topic , @RequestParam String level, @RequestParam int point) {
        return messageRolesDemoService.guideMe(topic, level, point);
    }
}
