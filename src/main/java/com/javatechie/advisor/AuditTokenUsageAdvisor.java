package com.javatechie.advisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;

public class AuditTokenUsageAdvisor implements CallAdvisor {

    Logger logger = LoggerFactory.getLogger(AuditTokenUsageAdvisor.class);

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {

        //call the next advisor / LLM
        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);

        ChatResponse chatResponse = chatClientResponse.chatResponse();
        if(chatResponse!=null){
            Usage usage = chatResponse.getMetadata().getUsage();
            //Audit token usage here
            if(usage!=null){
                // extract (i/p token , o/p token , total token)
                int inputToken = usage.getPromptTokens();
                int outputToken = usage.getCompletionTokens();
                int totalTokens = usage.getTotalTokens();

                //log those details
                logger.info("Token Usage - input Tokens: {}, output Tokens: {}, Total Tokens: {}",
                        inputToken, outputToken, totalTokens);
            }
        }



        return chatClientResponse;
    }

    @Override
    public String getName() {
        return "AuditTokenUsageAdvisor";
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
