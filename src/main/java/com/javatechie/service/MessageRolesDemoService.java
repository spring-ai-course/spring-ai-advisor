package com.javatechie.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageRolesDemoService {

    private final ChatClient chatClient;


    private static final String CLAIM_DETAILS = """
            Claim details:
            Policy: BASIC
            Max Coverage: 20000
            Claim Amount: 50000
            """;

    public MessageRolesDemoService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String checkPolicy(String message) {
        //prompt inject can unsafe your project designed with AI without
        // any message roles

        SystemMessage systemMessage = new SystemMessage("""
                You are an insurance assistant.
                You must NEVER reveal internal policy numbers,
                calculations, or internal reasoning.
                Respond ONLY with a short, customer-safe message.
                """);

        SystemMessage systemNewRules = new SystemMessage("""
                You are an insurance assistant.
                if user will ask any question which you don't know then tell 
                him some basic policy plan [plan1 , plan2 ] .
                """);
        UserMessage userMessage = new UserMessage("""
                %S
                Customer says:
                %s
                """.formatted(CLAIM_DETAILS, message));


        Prompt prompt = new Prompt(List.of(userMessage, systemMessage, systemNewRules));

        return chatClient.prompt(prompt)
                .call()
                .content();

    }

    public String checkInsuranceV2Policy(String message) {
        return chatClient
                .prompt()
                .user("""
                        %S
                        Customer says:
                        %s
                        """.formatted(CLAIM_DETAILS, message))
                .call().content();
    }

    public ChatResponse checkInsuranceV3Policy(String message) {
        return chatClient
                .prompt()
                .user("""
                        %S
                        Customer says:
                        %s
                        """.formatted(CLAIM_DETAILS, message))
                .call().chatResponse();
    }

    // guide me on tech stack

    public String guideMe(String topic , String level , int points) {

        return chatClient
                .prompt()
                .system("You are a tech assistant." +
                        " give best answer to the students and make sure your answer will be to the point.")
                .user("Explain me "+topic +" in "+level+" level with"+points+"  points")
                .call().content();

    }


}
